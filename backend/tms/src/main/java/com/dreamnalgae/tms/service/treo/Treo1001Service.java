package com.dreamnalgae.tms.service.treo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.treo.treo1001.TmsReorderMstVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Treo1001Service {

    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsReorderMstVO> selectReorderList(String userCetCd, String chulpanCd, String sujumCd, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                REORD_NO,
                REORD_GB,
                REORD_DT,
                CHK_GB,
                REORD_FAULT_GB,
                REORD_REASON_GB,
                SUJUM_CD,
                CHULPAN_CD,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = SUJUM_CD AND ROWNUM = 1) AS SUJUM_NM,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = CHULPAN_CD AND ROWNUM = 1) AS CHULPAN_NM,
                REORD_QTY,
                REORD_BOX,
                ORDER_BOX,
                INSERT_ID,
                BIGO
            FROM TMS_REORDER_MST
            WHERE USER_CET_CD = :userCetCd
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        if (StringUtils.hasText(chulpanCd)) {
            sql.append(" AND CHULPAN_CD = :chulpanCd");
            params.put("chulpanCd", chulpanCd);
        }

        if (StringUtils.hasText(sujumCd)) {
            sql.append(" AND SUJUM_CD = :sujumCd");
            params.put("sujumCd", sujumCd);
        }

        if (StringUtils.hasText(startDate)) {
            sql.append(" AND REORD_DT >= :startDate");
            params.put("startDate", startDate);
        }

        if (StringUtils.hasText(endDate)) {
            sql.append(" AND REORD_DT <= :endDate");
            params.put("endDate", endDate);
        }

        sql.append(" ORDER BY REORD_DT DESC, REORD_NO DESC");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsReorderMstVO.class));
    }


    public String generateReordNo(String insertId) {
        String sql = """
            SELECT SF_TMS_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_REORDER_MST', 'REORD_NO') AS REORD_NO
            FROM DUAL
        """;
        Map<String, Object> params = Map.of("insertId", insertId);
        return jdbc.queryForObject(sql, params, String.class);
    }



    @Transactional
    public void insertReorder(TmsReorderMstVO vo) {
        String sql = """
            INSERT INTO TMS_REORDER_MST (
                REORD_NO, REORD_DT, REORD_GB, CHK_GB,
                REORD_FAULT_GB, REORD_REASON_GB, REORD_QTY, REORD_BOX,
                ORDER_BOX, CHULPAN_CD, SUJUM_CD, BIGO,
                INSERT_ID, INSERT_DT, USER_CET_CD
            ) VALUES (
                :reordNo, :reordDt, :reordGb, :chkGb, :reordBox, 
                :reordFaultGb, :reordReasonGb, :reordQty,
                :orderBox, :chulpanCd, :sujumCd, :bigo,
                :insertId, SYSDATE, :userCetCd
            )
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("reordNo", vo.getReordNo())
            .addValue("reordDt", vo.getReordDt())
            .addValue("reordGb", vo.getReordGb())
            .addValue("chkGb", vo.getChkGb())

            .addValue("reordFaultGb", vo.getReordFaultGb())
            .addValue("reordReasonGb", vo.getReordReasonGb())
            .addValue("reordQty", vo.getReordQty())
            .addValue("reordBox", vo.getReordBox())

            .addValue("orderBox", vo.getOrderBox())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("bigo", vo.getBigo())

            .addValue("insertId", vo.getInsertId())
            .addValue("userCetCd", vo.getUserCetCd());

        jdbc.update(sql, param);
    }




    @Transactional
    public void updateReorder(TmsReorderMstVO vo) {
        String sql = """
            UPDATE TMS_REORDER_MST
            SET
                REORD_DT = :reordDt,
                REORD_GB = :reordGb,
                CHK_GB = :chkGb,
                REORD_FAULT_GB = :reordFaultGb,
                REORD_REASON_GB = :reordReasonGb,
                REORD_QTY = :reordQty,
                REORD_BOX = :reordBox,
                ORDER_BOX = :orderBox,
                CHULPAN_CD = :chulpanCd,
                SUJUM_CD = :sujumCd,
                BIGO = :bigo,
                UPDATE_ID = :updateId,
                UPDATE_DT = SYSDATE
            WHERE
                REORD_NO = :reordNo
                AND USER_CET_CD = :userCetCd
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("reordDt", vo.getReordDt())
            .addValue("reordGb", vo.getReordGb())
            .addValue("chkGb", vo.getChkGb())
            .addValue("reordFaultGb", vo.getReordFaultGb())
            .addValue("reordReasonGb", vo.getReordReasonGb())
            .addValue("reordQty", vo.getReordQty())
            .addValue("reordBox", vo.getReordBox())
            .addValue("orderBox", vo.getOrderBox())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("bigo", vo.getBigo())
            .addValue("updateId", vo.getUpdateId())
            .addValue("reordNo", vo.getReordNo())
            .addValue("userCetCd", vo.getUserCetCd());

        jdbc.update(sql, param);
    }



    @Transactional
    public void deleteReorder(String reordNo, String userCetCd) {
        String sql = """
            DELETE FROM TMS_REORDER_MST
            WHERE REORD_NO = :reordNo
            AND USER_CET_CD = :userCetCd
        """;

        Map<String, Object> params = Map.of(
            "reordNo", reordNo,
            "userCetCd", userCetCd
        );

        jdbc.update(sql, params);
    }



    
}
