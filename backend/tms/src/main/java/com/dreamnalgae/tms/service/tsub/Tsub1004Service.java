package com.dreamnalgae.tms.service.tsub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.tsub.tsub1004.TmsBaechaVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsub1004Service {
    private final NamedParameterJdbcTemplate jdbc;


    public List<TmsBaechaVO> selectList(String userCetCd, String cusDt, String cusGb, String cusAmpmGb) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                C.CUS_CD,             
                B.CUS_DT,             
                C.CUS_NM,             
                B.USER_ID,            
                U.USER_NM,            
                U.TEL_NO,            
                B.CAR_CD,             
                CAR.CAR_REGNUM,            
                CAR.CAR_NM
            FROM
                TMS_CUS C
            LEFT OUTER JOIN
                TMS_BAECHA B
                ON C.USER_CET_CD = B.USER_CET_CD
                AND C.CUS_CD = B.CUS_CD
                AND B.CUS_DT = :cusDt
            LEFT OUTER JOIN
                TMS_COM_USER U
                ON B.USER_ID = U.USER_ID
                AND B.USER_CET_CD = U.USER_CET_CD
            LEFT OUTER JOIN
                TMS_CAR CAR
                ON B.CAR_CD = CAR.CAR_CD
                AND B.USER_CET_CD = CAR.USER_CET_CD
            WHERE
                C.USER_CET_CD = :userCetCd
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("cusDt", cusDt);

        if (StringUtils.hasText(cusGb)) {
            sql.append(" AND C.CUS_GB = :cusGb");
            params.put("cusGb", cusGb);
        }

        if (StringUtils.hasText(cusAmpmGb)) {
            sql.append(" AND C.CUS_AMPM_GB = :cusAmpmGb");
            params.put("cusAmpmGb", cusAmpmGb);
        }

        sql.append(" ORDER BY C.CUS_CD");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsBaechaVO.class));
    }

  
    public boolean exists(TmsBaechaVO vo) {
        String sql = """
            SELECT COUNT(*) FROM TMS_BAECHA
            WHERE USER_CET_CD = :userCetCd
                AND CUS_DT    = :cusDt
                AND CUS_CD    = :cusCd
        """;

        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        Integer count = jdbc.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }



    @Transactional
    public void insert(TmsBaechaVO vo) {
        String sql = """
            INSERT INTO TMS_BAECHA (
                USER_CET_CD,
                CUS_DT,
                CUS_CD,
                USER_ID,
                CAR_CD,
                INSERT_ID,
                INSERT_DT
            ) VALUES (
                :userCetCd,
                :cusDt,
                :cusCd,
                :userId,
                :carCd,
                :insertId,
                SYSDATE
            )
        """;

        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        jdbc.update(sql, params);
    }


    @Transactional
    public void update(TmsBaechaVO vo) {
        String sql = """
            UPDATE TMS_BAECHA
            SET
                USER_ID     = :userId,
                CAR_CD      = :carCd,
                UPDATE_ID   = :updateId,
                UPDATE_DT   = SYSDATE
            WHERE
                USER_CET_CD = :userCetCd
            AND CUS_DT      = :cusDt
            AND CUS_CD      = :cusCd
        """;

        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        jdbc.update(sql, params);
    }
    

    @Transactional
    public void delete(TmsBaechaVO vo) {
        String sql = """
            DELETE FROM TMS_BAECHA
            WHERE
                USER_CET_CD = :userCetCd
            AND CUS_DT      = :cusDt
            AND CUS_CD      = :cusCd
        """;

        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        jdbc.update(sql, params);
    }



}
