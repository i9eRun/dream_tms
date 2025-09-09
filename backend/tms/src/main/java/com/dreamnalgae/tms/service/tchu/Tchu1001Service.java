package com.dreamnalgae.tms.service.tchu;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeDeleteReq;
import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeMstVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tchu1001Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsDungeMstVO> selectList(String dungeDt, String userCetCd, String chulgoGb, String chulpanCd, String cetCd) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                A.USER_CET_CD,
                A.ROW_SEQ,
                A.CET_CD,
                C.CUST_NM AS CHULPAN_NM,
                A.ORD_NO,
                A.SUJUM_CD,
                C2.CHULGO_GB,
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
                A.CHULPAN_CD,
                A.DAE_NO,
                A.INSERT_ID,
                A.UPDATE_DT,
                A.UPDATE_ID
            FROM TMS_DUNGE_MST A
            LEFT JOIN TMS_CUST C ON A.CHULPAN_CD = C.CUST_CD
            LEFT JOIN TMS_CUST C2 ON A.SUJUM_CD = C2.CUST_CD
            WHERE A.DUNGE_DT = :dungeDt
            AND A.USER_CET_CD = :userCetCd
            """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dungeDt", dungeDt)
                .addValue("userCetCd", userCetCd);

        if (chulgoGb != null && !chulgoGb.isEmpty()) {
            sql.append("""
                AND C2.CHULGO_GB = :chulgoGb
                """);
            params.addValue("chulgoGb", chulgoGb);
        }
 
        if (chulpanCd != null && !chulpanCd.isEmpty()) {
            sql.append("""
                AND A.CHULPAN_CD = :chulpanCd
                """);
            params.addValue("chulpanCd", chulpanCd);
        }

        if (cetCd != null && !cetCd.isEmpty()) {
            sql.append("""
                AND A.CET_CD = :cetCd
                """);
            params.addValue("cetCd", cetCd);
        }

        sql.append("""
            ORDER BY A.ROW_SEQ
            """);

        System.out.println(sql.toString());

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsDungeMstVO.class));
    }



    /** transGb 조회 (TRIM 보정) */
    public Optional<String> findTransGb(String userCetCd, long rowSeq, String ordNo) {
        final String sql = """
            SELECT TRIM(TRANS_GB) AS TRANS_GB
             FROM TMS_DUNGE_MST
            WHERE USER_CET_CD = :userCetCd
              AND ROW_SEQ     = :rowSeq
              AND ORD_NO      = :ordNo
              AND ROWNUM = 1
        """;

        var params = new MapSqlParameterSource()
            .addValue("userCetCd", userCetCd)
            .addValue("rowSeq", rowSeq)
            .addValue("ordNo", ordNo);

        try {
            String v = jdbc.queryForObject(sql, params, String.class);
            return Optional.ofNullable(v);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    /** 단건 삭제 (transGb='1' 이면 예외) */
    public int deleteOne(TmsDungeDeleteReq req) {
        // 전송여부 확인
        var transGbOpt = findTransGb(req.getUserCetCd(), req.getRowSeq(), req.getOrdNo());
        
        if (transGbOpt.isEmpty()) {
            throw new IllegalStateException("데이터를 찾을 수 없습니다.");
        }

        if ("1".equals(transGbOpt.get())) {
            throw new IllegalStateException("전송된 데이터는 삭제가 불가합니다.");
        }

        // 안전 삭제 (경합 방지 위해 WHERE에 trnsGB <> '1' 추가)
        final String delSql = """
            DELETE FROM TMS_DUNGE_MST
             WHERE USER_CET_CD = :userCetCd
               AND ROW_SEQ     = :rowSeq
               AND ORD_NO      = :ordNo
               AND (TRANS_GB IS NULL OR TRIM(TRANS_GB) <> '1')
        """;

        var params = new MapSqlParameterSource()
                .addValue("userCetCd", req.getUserCetCd())
                .addValue("rowSeq", req.getRowSeq())
                .addValue("ordNo", req.getOrdNo());
        return jdbc.update(delSql, params);
    }



    
}
