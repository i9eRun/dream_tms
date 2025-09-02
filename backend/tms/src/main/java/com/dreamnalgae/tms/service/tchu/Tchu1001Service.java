package com.dreamnalgae.tms.service.tchu;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeMstVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tchu1001Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsDungeMstVO> selectList(String dungeDt, String userCetCd, String chulgoGb, String chulpanCd) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                A.USER_CET_CD,
                A.ROW_SEQ,
                A.CHULPAN_CD,
                C.CUST_NM AS CHULPAN_NM,
                A.ORD_NO,
                A.SUJUM_CD,
                A.SUJUM_NM,
                A.JIYUK_NM,
                A.COURSE_CD,
                A.TEL_NO,
                A.DUNGE,
                A.CHUL_NM,
                A.QTY,
                A.BIGO,
                A.DUNGE_DT,
                A.OUT_YN,
                A.DAY_GB,
                A.TRANS_GB,
                A.INSERT_DT,
                A.DREAM_UPDATE,
                A.DAE_CD,
                A.DAE_NO,
                A.INSERT_ID,
                A.UPDATE_DT,
                A.UPDATE_ID
            FROM TMS_DUNGE_MST A
            LEFT JOIN TMS_CUST C ON A.CHULPAN_CD = C.CUST_CD
            WHERE A.DUNGE_DT = :dungeDt
            AND A.USER_CET_CD = :userCetCd
            """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dungeDt", dungeDt)
                .addValue("userCetCd", userCetCd);

        if (chulgoGb != null && !chulgoGb.isEmpty()) {
            sql.append("""
                AND C.CHULGO_GB = :chulgoGb
                """);
            params.addValue("chulgoGb", chulgoGb);
        }
 
        if (chulpanCd != null && !chulpanCd.isEmpty()) {
            sql.append("""
                AND A.CHULPAN_CD = :chulpanCd
                """);
            params.addValue("chulpanCd", chulpanCd);
        }

        sql.append("""
            ORDER BY A.ROW_SEQ
            """);

        System.out.println(sql.toString());

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsDungeMstVO.class));
    }


    
}
