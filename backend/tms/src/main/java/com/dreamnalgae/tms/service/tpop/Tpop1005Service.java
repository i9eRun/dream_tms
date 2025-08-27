package com.dreamnalgae.tms.service.tpop;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tpop.CusSearchVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tpop1005Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<CusSearchVO> selectCusList(CusSearchVO vo) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        String keyword = vo.getKeyword();        
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT CUS_CD, CUS_NM, CUS_GB
            FROM TMS_CUS
            WHERE USER_CET_CD = :userCetCd
            """);

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
                AND (
                    CUS_CD LIKE '%' || :keyword || '%' OR
                    CUS_NM LIKE '%' || :keyword || '%'
                )
                """);
        }
        
        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(CusSearchVO.class));
    }
}
