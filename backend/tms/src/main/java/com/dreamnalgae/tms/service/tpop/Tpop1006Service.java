package com.dreamnalgae.tms.service.tpop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tpop.tpop1006.ChulpanSujumVO;
import com.dreamnalgae.tms.model.tpop.tpop1006.SujumVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tpop1006Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<SujumVO> selectSujumList(String userCetCd, String keyword, String chulpanCd, String useYn) {
        
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT 
                NULL AS row_seq,
                :chulpanCd AS chulpan_cd,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = :chulpanCd) AS chulpan_nm,
                T1.CUST_CD AS sujum_cd,
                NULL AS basic_danga_rt,
                NULL AS singan_qty,
                NULL AS bok_cnv_cd,
                '01' AS baebon_cd,
                NULL AS bok_cnv_cd2,
                NULL AS part_cd,
                NULL AS as_part_cd,
                T1.CUST_CD,
                T1.CUST_ABBR_NM,
                T1.CUST_NM,
                T1.TEL_NO,
                T1.FAX_NO,
                T1.JIYUK_NM,
                T1.GULJAE_GB,
                T1.CHULGO_GB,
                T1.DELIV_PATH_CD,
                T1.BIGO,
                T1.USE_YN,
                T1.ADDR1,
                T1.ADDR2,
                T1.POST_NO,
                T1.TAKBAE_CD,
                DECODE(T1.USE_YN, '1', '사용', '미사용') AS as_use_yn,
                '0' AS chk
            FROM TMS_CUST T1
            WHERE 1=1
            AND USER_CET_CD = :userCetCd
            AND (T1.CUST_NM LIKE '%' || :keyword || '%'
                OR T1.CUST_ABBR_NM LIKE '%' || :keyword || '%'
                OR T1.CUST_CD LIKE :keyword || '%'
                OR T1.DAMDANG_NM LIKE '%' || :keyword || '%'
                OR T1.PARENT_CUST_CD LIKE :keyword || '%')
        """);

        if (useYn != null && !useYn.isEmpty()) {
            sql.append(" AND T1.USE_YN = :useYn\n");
        }

        sql.append("""
            ORDER BY DECODE(INSTR(T1.CUST_NM, :keyword), 0, 999999, INSTR(T1.CUST_NM, :keyword)), T1.CUST_NM
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("chulpanCd", chulpanCd);
        params.put("keyword", keyword);
        if (useYn != null && !useYn.isEmpty()) {
            params.put("useYn", useYn);
        }

        return jdbc.query(sql.toString(), new MapSqlParameterSource(params), new BeanPropertyRowMapper<>(SujumVO.class));
    }


    public List<ChulpanSujumVO> selectChulpanSujumList(String userCetCd, String chulpanCd, String keyword, String useYn) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT 
                T.ROW_SEQ,
                T.CHULPAN_CD,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = T.CHULPAN_CD) AS CHULPAN_NM,
                T.SUJUM_CD,
                T.BASIC_DANGA_RT,
                T.SINGAN_QTY,
                T.BOK_CNV_CD,
                T.BAEBON_CD,
                T.BOK_CNV_CD2,
                T.PART_CD,
                SF_NM_CODE(T.PART_CD, '243') AS AS_PART_CD,
                C.CUST_CD,
                C.CUST_ABBR_NM,
                C.CUST_NM,
                C.TEL_NO,
                C.FAX_NO,
                C.JIYUK_NM,
                C.GULJAE_GB,
                C.CHULGO_GB,
                SF_NM_CODE(C.CHULGO_GB, '173') AS CHULGO_GB_NM,
                C.DELIV_PATH_CD,
                C.BIGO,
                C.USE_YN,
                C.ADDR1,
                C.ADDR2,
                C.POST_NO,
                C.TAKBAE_CD,
                DECODE(C.USE_YN, '1', '사용', '미사용') AS AS_USE_YN,
                '0' AS CHK
            FROM TMS_CHULPAN_SUJUM T
            LEFT OUTER JOIN TMS_CUST C ON T.SUJUM_CD = C.CUST_CD
            WHERE T.CHULPAN_CD = :chulpanCd
                AND T.USER_CET_CD = :userCetCd
              AND (
                   C.CUST_NM LIKE '%' || :keyword || '%'
                OR C.CUST_ABBR_NM LIKE '%' || :keyword || '%'
                OR C.CUST_CD LIKE :keyword || '%'
                OR C.DAMDANG_NM LIKE '%' || :keyword || '%'
                OR C.PARENT_CUST_CD LIKE :keyword || '%'
              )
        """);

        if (useYn != null && !useYn.isBlank()) {
            sql.append(" AND C.USE_YN = :useYn ");
        }

        sql.append("""
            ORDER BY DECODE(INSTR(C.CUST_NM, :keyword), 0, 999999, INSTR(C.CUST_NM, :keyword)), C.CUST_NM, T.SUJUM_CD
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("chulpanCd", chulpanCd);
        params.put("keyword", keyword);
        if (useYn != null && !useYn.isBlank()) {
            params.put("useYn", useYn);
        }

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(ChulpanSujumVO.class));
    }



    
}
