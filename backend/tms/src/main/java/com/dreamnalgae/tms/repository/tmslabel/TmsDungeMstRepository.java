package com.dreamnalgae.tms.repository.tmslabel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dreamnalgae.tms.entity.tmslabel.TmsDungeMst;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TmsDungeMstRepository {

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * 주문번호 생성 함수 호출
     * SF_TMS_NO(insertId, YYYYMMDD, 'TMS_DUNGE_MST', 'ORD_NO')
     */
    public String generateOrderNumber(String insertId) {
        String sql = """
            SELECT SF_TMS_CHUL_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_DUNGE_MST', 'ORD_NO') AS ORD_NO
            FROM DUAL
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("insertId", insertId);

        try {
            return jdbc.queryForObject(sql, params, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null; // 결과 없을 때
        }
    }


    // INSERT
    public void insert(TmsDungeMst vo) {
        String sql = """
            INSERT INTO TMS_WMS.TMS_DUNGE_MST (
                USER_CET_CD, CET_CD, ORD_NO, SUJUM_CD, SUJUM_NM, JIYUK_NM,
                COURSE_CD, TEL_NO, DUNGE, CHUL_NM, QTY,
                BIGO, DUNGE_DT, OUT_YN, DAY_GB, TRANS_GB,
                DREAM_UPDATE, CHULPAN_CD, DAE_NO,
                INSERT_ID
            ) VALUES (
                :userCetCd, :cetCd, :ordNo, :sujumCd, :sujumNm, :jiyukNm,
                :courseCd, :telNo, :dunge, :chulNm, :qty,
                :bigo, :dungeDt, :outYn, :dayGb, :transGb,
                NVL(:dreamUpdate,'0'), :chulpanCd, :daeNo,
                :insertId
            )
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userCetCd", vo.getUserCetCd())
                .addValue("cetCd", vo.getCetCd())
                .addValue("ordNo", vo.getOrdNo())
                .addValue("sujumCd", vo.getSujumCd())
                .addValue("sujumNm", vo.getSujumNm())
                .addValue("jiyukNm", vo.getJiyukNm())
                .addValue("courseCd", vo.getCourseCd())
                .addValue("telNo", vo.getTelNo())
                .addValue("dunge", vo.getDunge())
                .addValue("chulNm", vo.getChulNm())
                .addValue("qty", vo.getQty())
                .addValue("bigo", vo.getBigo())
                .addValue("dungeDt", vo.getDungeDt())
                .addValue("outYn", vo.getOutYn())
                .addValue("dayGb", vo.getDayGb())
                .addValue("transGb", vo.getTransGb())
                .addValue("dreamUpdate", vo.getDreamUpdate())
                .addValue("chulpanCd", vo.getChulpanCd())
                .addValue("daeNo", vo.getDaeNo())
                .addValue("insertId", vo.getInsertId());

        jdbc.update(sql, params);
    }

    // UPDATE
    public void update(TmsDungeMst vo) {
        String sql = """
            UPDATE TMS_WMS.TMS_DUNGE_MST
            SET SUJUM_CD   = :sujumCd,
                SUJUM_NM   = :sujumNm,
                JIYUK_NM   = :jiyukNm,
                COURSE_CD  = :courseCd,
                TEL_NO     = :telNo,
                DUNGE      = :dunge,
                CHUL_NM    = :chulNm,
                QTY        = :qty,
                BIGO       = :bigo,
                DUNGE_DT   = :dungeDt,
                OUT_YN     = :outYn,
                DAY_GB     = :dayGb,
                TRANS_GB   = :transGb,
                CHULPAN_CD = :chulpanCd,
                DAE_NO     = :daeNo,
                UPDATE_DT  = SYSDATE,
                UPDATE_ID  = :updateId
            WHERE USER_CET_CD = :userCetCd
              AND ROW_SEQ     = :rowSeq
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("sujumCd", vo.getSujumCd())
                .addValue("sujumNm", vo.getSujumNm())
                .addValue("jiyukNm", vo.getJiyukNm())
                .addValue("courseCd", vo.getCourseCd())
                .addValue("telNo", vo.getTelNo())
                .addValue("dunge", vo.getDunge())
                .addValue("chulNm", vo.getChulNm())
                .addValue("qty", vo.getQty())
                .addValue("bigo", vo.getBigo())
                .addValue("dungeDt", vo.getDungeDt())
                .addValue("outYn", vo.getOutYn())
                .addValue("dayGb", vo.getDayGb())
                .addValue("transGb", vo.getTransGb())
                .addValue("chulpanCd", vo.getChulpanCd())
                .addValue("daeNo", vo.getDaeNo())
                .addValue("updateId", vo.getUpdateId())
                .addValue("userCetCd", vo.getUserCetCd())
                .addValue("rowSeq", vo.getRowSeq());

        jdbc.update(sql, params);
    }

    // DELETE
    public void delete(String userCetCd, Long rowSeq) {
        String sql = """
            DELETE FROM TMS_WMS.TMS_DUNGE_MST
             WHERE USER_CET_CD = :userCetCd
               AND ROW_SEQ = :rowSeq
        """;

        jdbc.update(sql, Map.of("userCetCd", userCetCd, "rowSeq", rowSeq));
    }






}
