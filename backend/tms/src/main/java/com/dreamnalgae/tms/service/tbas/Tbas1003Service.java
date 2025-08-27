package com.dreamnalgae.tms.service.tbas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tbas.tbas1003.CetListVO;
import com.dreamnalgae.tms.model.tbas.tbas1003.CetPublishVO;
import com.dreamnalgae.tms.model.tbas.tbas1003.PublishListVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1003Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<CetListVO> selectCetList(String userCetCd, String keyword) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                T.CUST_CD,                                                
                T.CUST_GB,                                                
                T.CUST_DIV_GB,                                            
                T.CUST_ABBR_NM,                                           
                T.CUST_NM,
                T.JIYUK_NM,
                T.CHULGO_GB,
                T.TEAM_NM,
                T.DAMDANG_NM,
                T.CHARGE_EMP_ID,
                (SELECT USER_NM FROM TMS_COM_USER WHERE USER_ID=T.CHARGE_EMP_ID) AS CHARGE_EMP_NM,
                T.TEL_NO,
                T.FAX_NO,
                T.BANP_TEAM_NM,
                T.BAEBON_CD
            FROM TMS_CUST T
            WHERE T.USE_YN = '1'
                AND T.TRADE_TO_DT IS NULL
                AND T.USER_CET_CD = :userCetCd
                AND CUST_DIV_GB = '10'
        """);

        Map<String,Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        if (keyword !=null && !keyword.isBlank()) {
            sql.append("""
                AND (
                    T.CUST_CD LIKE '%' || :keyword || '%'
                    OR T.CUST_NM LIKE '%' || :keyword || '%'
                )    
            """);
            params.put("keyword", keyword);
        }

        sql.append("ORDER BY T.CUST_CD, T.CUST_NM ");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(CetListVO.class));
    }


    public List<PublishListVO> selectPublishList(String userCetCd, String keyword) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                T.CUST_CD,
                T.CUST_DIV_GB,
                T.CUST_ABBR_NM,
                T.CUST_NM,
                T.DAEPYO_NM,
                T.TEL_NO,
                T.FAX_NO,
                T.ADDR1,
                T.JIYUK_NM,
                T.CHULGO_GB,
                CASE WHEN T.BAEBON_CD IS NULL
                    THEN CASE WHEN T.CHULGO_GB IN ('01', '02')
                            THEN '01'
                            ELSE '03'
                        END
                    ELSE T.BAEBON_CD
                END AS BAEBON_CD

                FROM TMS_CUST T
                WHERE T.CUST_DIV_GB = '1'
                    AND T.TRADE_TO_DT IS NULL
                    AND T.USE_YN = '1'
                    AND USER_CET_CD = :userCetCd
        """);

        Map<String,Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

            if (keyword != null && !keyword.isBlank()) {
        sql.append("""
            AND (
                T.CUST_CD = :keyword
                OR T.CUST_ABBR_NM LIKE '%' || :keyword || '%'
                OR T.TEL_NO LIKE '%' || :keyword || '%'
                OR T.FAX_NO LIKE '%' || :keyword || '%'
                OR T.BIGO LIKE '%' || :keyword || '%'
                OR T.DAEPYO_NM LIKE '%' || :keyword || '%'
            )
        """);
        params.put("keyword", keyword);
    }

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(PublishListVO.class));  
    }


    @Transactional
    public void insertAll(List<CetPublishVO> list) {
        for (CetPublishVO vo : list) {
            insert(vo);
        }
    }


    public void insert(CetPublishVO vo) {
        String sql = """
            INSERT INTO TMS_CENTER_CHULPAN (
                USER_CET_CD,
                ROW_SEQ,
                CHULPAN_CD,
                CET_CD,
                BASIC_DANGA_RT,
                SINGAN_QTY,
                BOK_CNV_CD,
                BAEBON_CD,
                BOK_CNV_CD2,
                PART_CD,
                CNV_SUJUM_CD,
                CENTER_PRT_YN,
                CENTER_PRT_GB,
                CHULPAN_PRT_GB,
                BANP_NOT_YN,
                INSERT_DT,
                INSERT_ID
            ) VALUES (
                :userCetCd,
                (SELECT NVL(MAX(ROW_SEQ), 0) + 1 FROM TMS_CENTER_CHULPAN),
                :chulpanCd,
                :cetCd,
                :basicDangaRt,
                :singanQty,
                :bokCnvCd,
                :baebonCd,
                :bokCnvCd2,
                :partCd,
                :cnvSujumCd,
                NVL(:centerPrtYn, '1'),
                NVL(:centerPrtGb, '3'),
                NVL(:chulpanPrtGb, '2'),
                NVL(:banpNotYn, '0'),
                SYSDATE,
                :insertId
            )
        """;

        jdbc.update(sql, new BeanPropertySqlParameterSource(vo));
    }



    public List<CetPublishVO> selectCetPublishList(String userCetCd, String cetCd) {
        String sql = """
            SELECT
                '0' AS chk,
                T.ROW_SEQ,
                T.CHULPAN_CD,
                T.CET_CD,
                T.BASIC_DANGA_RT,
                T.SINGAN_QTY,
                T.BOK_CNV_CD,
                T.BAEBON_CD,
                T.BOK_CNV_CD2,
                T.PART_CD,
                T.CNV_SUJUM_CD,
                NVL(T.CENTER_PRT_YN, '1') AS CENTER_PRT_YN,
                NVL(T.CENTER_PRT_GB, '3') AS CENTER_PRT_GB,
                NVL(T.CHULPAN_PRT_GB, '2') AS CHULPAN_PRT_GB,
                NVL(T.BANP_NOT_YN, '0') AS BANP_NOT_YN,
                T2.CUST_ABBR_NM,
                T2.JIYUK_NM
            FROM TMS_CENTER_CHULPAN T
            JOIN TMS_CUST T2 ON T.CHULPAN_CD = T2.CUST_CD
            WHERE T.CET_CD = :cetCd AND T.USER_CET_CD = :userCetCd
            ORDER BY T.CHULPAN_CD
        """;

        Map<String, Object> params = Map.of("cetCd", cetCd, "userCetCd",userCetCd);

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(CetPublishVO.class));
    }


    @Transactional
    public void deleteCetPublishList(List<CetPublishVO> list) {
        for (CetPublishVO vo : list) {
            Map<String, Object> params = Map.of(
                "userCetCd", vo.getUserCetCd(),
                "chulpanCd", vo.getChulpanCd(),
                "cetCd", vo.getCetCd(),
                "rowSeq", vo.getRowSeq()
            );

            String sql = """
                DELETE FROM TMS_CENTER_CHULPAN
                WHERE USER_CET_CD = :userCetCd
                AND CHULPAN_CD = :chulpanCd
                AND CET_CD = :cetCd
                AND ROW_SEQ = :rowSeq
            """;

            jdbc.update(sql, params);
        }
    }


    @Transactional
    public void updateCetPublishList(List<CetPublishVO> list) {
        for (CetPublishVO vo : list) {
            String sql = """
                UPDATE TMS_CENTER_CHULPAN
                SET BASIC_DANGA_RT = :basicDangaRt,
                    SINGAN_QTY = :singanQty,
                    BOK_CNV_CD = :bokCnvCd,
                    BAEBON_CD = :baebonCd,
                    BOK_CNV_CD2 = :bokCnvCd2,
                    PART_CD = :partCd,
                    CNV_SUJUM_CD = :cnvSujumCd,
                    CENTER_PRT_YN = :centerPrtYn,
                    CENTER_PRT_GB = :centerPrtGb,
                    CHULPAN_PRT_GB = :chulpanPrtGb,
                    BANP_NOT_YN = :banpNotYn,
                    UPDATE_DT = SYSDATE,
                    UPDATE_ID = :updateId
                WHERE USER_CET_CD = :userCetCd
                AND CHULPAN_CD = :chulpanCd
                AND CET_CD = :cetCd
                AND ROW_SEQ = :rowSeq
            """;

            SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
            jdbc.update(sql, params);
        }
    }
}
