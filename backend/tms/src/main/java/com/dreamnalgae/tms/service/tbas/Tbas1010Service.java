package com.dreamnalgae.tms.service.tbas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.tbas.tbas1010.DochakChulpanVO;
import com.dreamnalgae.tms.model.tbas.tbas1010.DochakVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1010Service {
    private final NamedParameterJdbcTemplate jdbc;

    // type → 실제 컬럼명 매핑
    private static final Map<String, String> TYPE_COLUMN_MAP = Map.of(
        "code", "D.SUJUM_CD",
        "name", "C.CUST_NM",
        "abbr", "C.CUST_ABBR_NM",
        "jiyuk", "C.JIYUK_NM",
        "tel", "C.TEL_NO"
    );

    public List<DochakVO> selectList(String userCetCd, String type, String keyword) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT
                D.USER_CET_CD,
                D.SUJUM_CD,
                C.CUST_NM,
                C.CUST_ABBR_NM,
                C.JIYUK_NM,
                C.TEL_NO,
                D.DOCHAK_GB,
                D.DOCHAK_OVER_QTY,
                D.DOCHAK_BASIC_GB,
                D.DOCHAK_BASIC_DANGA,
                D.DOCHAK_BIGO,
                D.INSERT_DT,
                D.INSERT_ID,
                D.UPDATE_DT,
                D.UPDATE_ID
            FROM TMS_DOCHAK D
            LEFT JOIN TMS_CUST C ON D.SUJUM_CD = C.CUST_CD
            WHERE D.USER_CET_CD = :userCetCd
            AND C.CUST_DIV_GB = '2'
            """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        
        if (StringUtils.hasText(type) && StringUtils.hasText(keyword)) {
            String column = TYPE_COLUMN_MAP.get(type);
            if (column != null) {
                sql.append(" AND ").append(column).append(" LIKE :keyword ");
                params.put("keyword", "%" + keyword + "%");
            }
        }

        return jdbc.query(sql.toString(), params, BeanPropertyRowMapper.newInstance(DochakVO.class));
    }
    


    public List<DochakChulpanVO> selectChulpanList(String userCetCd, String sujumCd, String chulpanGb) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT
                D.SUJUM_CD,
                D.CHULPAN_CD,
                C.CUST_NM AS CHULPAN_NM,
                D.CHULPAN_GB,
                D.CHULPAN_BASIC_GB,
                D.CHULPAN_BASIC_DANGA,
                D.CHULPAN_BASIC_QTY,
                D.CHULPAN_BIGO,
                D.INSERT_DT,
                D.INSERT_ID,
                D.UPDATE_DT,
                D.UPDATE_ID
            FROM TMS_DOCHAK_CHULPAN D
            LEFT JOIN TMS_CUST C ON D.CHULPAN_CD = C.CUST_CD
            WHERE D.USER_CET_CD = :userCetCd
            AND D.SUJUM_CD = :sujumCd
            AND D.CHULPAN_GB = :chulpanGb
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("sujumCd", sujumCd);
        params.put("chulpanGb", chulpanGb);

        return jdbc.query(sql.toString(), params, BeanPropertyRowMapper.newInstance(DochakChulpanVO.class));
    }




    @Transactional
    public boolean chulpanInsert(DochakChulpanVO vo) {
        String sql = """
            INSERT INTO TMS_DOCHAK_CHULPAN
                (USER_CET_CD, SUJUM_CD, CHULPAN_CD, CHULPAN_GB, INSERT_ID, INSERT_DT)
            VALUES
                (:userCetCd, :sujumCd, :chulpanCd, '1', :insertId, SYSDATE)
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }


    @Transactional
    public boolean chulpanDelete(DochakChulpanVO vo) {
        String sql = """
            DELETE FROM TMS_DOCHAK_CHULPAN
            WHERE USER_CET_CD = :userCetCd
              AND SUJUM_CD = :sujumCd
              AND CHULPAN_CD = :chulpanCd
              AND CHULPAN_GB = '1'
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }

    @Transactional
    public boolean chulpanInsert2(DochakChulpanVO vo) {
        String sql = """
            INSERT INTO TMS_DOCHAK_CHULPAN
                (USER_CET_CD, SUJUM_CD, CHULPAN_CD, CHULPAN_GB, CHULPAN_BASIC_GB, CHULPAN_BASIC_QTY, CHULPAN_BASIC_DANGA, INSERT_ID, INSERT_DT)
            VALUES
                (:userCetCd, :sujumCd, :chulpanCd, '2', :chulpanBasicGb, :chulpanBasicQty, :chulpanBasicDanga, :insertId, SYSDATE)
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }


    @Transactional
    public boolean chulpanDelete2(DochakChulpanVO vo) {
        String sql = """
            DELETE FROM TMS_DOCHAK_CHULPAN
            WHERE USER_CET_CD = :userCetCd
              AND SUJUM_CD = :sujumCd
              AND CHULPAN_CD = :chulpanCd
              AND CHULPAN_GB = '2'
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }



    public boolean save(DochakVO vo) {
        String sql = """
            UPDATE TMS_WMS.TMS_DOCHAK
               SET DOCHAK_GB         = :dochakGb,
                   DOCHAK_OVER_QTY   = :dochakOverQty,
                   DOCHAK_BASIC_GB   = :dochakBasicGb,
                   DOCHAK_BASIC_DANGA= :dochakBasicDanga,
                   DOCHAK_BIGO       = :dochakBigo,
                   UPDATE_DT         = SYSDATE,
                   UPDATE_ID         = :updateId
             WHERE USER_CET_CD = :userCetCd
               AND SUJUM_CD    = :sujumCd
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    } 


}
