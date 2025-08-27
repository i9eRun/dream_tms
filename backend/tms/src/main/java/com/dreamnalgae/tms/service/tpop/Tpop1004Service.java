package com.dreamnalgae.tms.service.tpop;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tpop.CarSearchVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tpop1004Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<CarSearchVO> selectCarList(CarSearchVO vo) {
        String keyword = vo.getKeyword();
        String userCetCd = vo.getUserCetCd();

        StringBuilder sql = new StringBuilder("""
            SELECT 
                c.USER_CET_CD,
                c.CAR_CD,
                c.CAR_REGNUM,
                c.CAR_KIND,
                c.CAR_USES,
                c.CAR_NM,
                c.CAR_CHUL_YEAR,
                c.USER_ID,
                c.CAR_CHADAENUM,
                c.CAR_JUH_YEAR,
                c.CAR_INWON,
                c.CAR_COMP,
                c.CAR_PUR_DT,
                c.CAR_PURWAY,
                c.CAR_PURMONEY,
                c.CAR_CHK_DT,
                c.CAR_YOUNG_DT,
                c.CAR_BAE_DT,
                c.CAR_INSUNAME,
                c.CAR_CHAINSU,
                c.CAR_TOTINSU,
                c.CAR_TON,
                c.CAR_LOADAGE,
                c.CAR_GUBUN,
                c.CAR_AMPM,
                c.COURSEGUBUN,
                c.INSERT_DT,
                c.INSERT_ID,
                c.UPDATE_DT,
                c.UPDATE_ID,
                u.USER_NM
            FROM TMS_CAR c
            LEFT JOIN TMS_COM_USER u ON c.USER_ID = u.USER_ID
            WHERE c.USER_CET_CD = :userCetCd
        """);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userCetCd", userCetCd);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append("""
                AND (
                    c.CAR_NM LIKE :keyword OR
                    c.CAR_REGNUM LIKE :keyword
                )
            """);
            params.addValue("keyword", "%" + keyword + "%");
        }

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(CarSearchVO.class));
    }
    
}
