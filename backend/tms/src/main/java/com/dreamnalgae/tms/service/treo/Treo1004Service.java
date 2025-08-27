package com.dreamnalgae.tms.service.treo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.treo.treo1004.TmsJaechulgoVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Treo1004Service {
    private final NamedParameterJdbcTemplate jdbc;


    public List<TmsJaechulgoVO> selectList(String userCetCd, String chulpanCd, String sujumCd, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder("""
                SELECT J.*,
                    S.CUST_NM AS SUJUM_NM,
                    C.CUST_NM AS CHULPAN_NM
                FROM TMS_JAECHULGO J
                LEFT JOIN TMS_CUST S ON S.CUST_CD = J.SUJUM_CD
                LEFT JOIN TMS_CUST C ON C.CUST_CD = J.CHULPAN_CD
                WHERE J.USER_CET_CD = :userCetCd
                """);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

         if (StringUtils.hasText(chulpanCd)) {
            sql.append(" AND J.CHULPAN_CD = :chulpanCd");
            params.put("chulpanCd", chulpanCd);
        }

        if (StringUtils.hasText(sujumCd)) {
            sql.append(" AND J.SUJUM_CD = :sujumCd");
            params.put("sujumCd", sujumCd);
        }

        if (StringUtils.hasText(startDate)) {
            sql.append(" AND J.JAECHULGO_DT >= :startDate");
            params.put("startDate", startDate);
        }

        if (StringUtils.hasText(endDate)) {
            sql.append(" AND J.JAECHULGO_DT <= :endDate");
            params.put("endDate", endDate);
        }

        sql.append(" ORDER BY J.JAECHULGO_DT DESC, J.JAECHULGO_NO DESC");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsJaechulgoVO.class));        
    }


    public String generateNo(String insertId) {
        String sql = """
            SELECT SF_TMS_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_JAECHULGO', 'JAECHULGO_NO') AS JAECHULGO_NO
            FROM DUAL
        """;
        Map<String, Object> params = Map.of("insertId", insertId);
        return jdbc.queryForObject(sql, params, String.class);
    }


    @Transactional
    public void insert(TmsJaechulgoVO vo) {
        String sql = """
            INSERT INTO TMS_JAECHULGO (
                USER_CET_CD, JAECHULGO_NO, PRE_ORD_NO, JAECHULGO_GB, JAECHULGO_DT, CHK_GB,
                CHULPAN_CD, SUJUM_CD, JAECHULGO_CNT, JAECHULGO_REQ_ID, JAECHULGO_APP_ID,
                JAECHULGO_QTY, JAECHULGO_BOX, ORDER_BOX, BOX_COUNT,
                JAECHULGO_DAMDANG_ID, JAECHULGO_J_DAMDANG_ID, BIGO,
                JAECHULGO_FAULT_GB, JAECHULGO_REASON_GB,
                CUS_CD, CUS_DT,
                JAECHULGO_JAECHECK, JAECHULGO_OCHECK, JAECHULGO_JUNCHECK, JAECHULGO_ENDCHECK,
                JAECHULGO_SEQ, JAECHULGO_SAGOGU, TOTAL_BOX, JAECHULGO_ORDER,
                JAECHULGO_NALGAE, JAECHULGO_NALGAE_CHK, PARENT_CUST_CD,
                INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :jaechulgoNo, :preOrdNo, :jaechulgoGb, :jaechulgoDt, :chkGb,
                :chulpanCd, :sujumCd, :jaechulgoCnt, :jaechulgoReqId, :jaechulgoAppId,
                :jaechulgoQty, :jaechulgoBox, :orderBox, :boxCount,
                :jaechulgoDamdangId, :jaechulgoJDamdangId, :bigo,
                :jaechulgoFaultGb, :jaechulgoReasonGb,
                :cusCd, :cusDt,
                :jaechulgoJaecheck, :jaechulgoOcheck, :jaechulgoJuncheck, NVL(:jaechulgoEndcheck, '무'),
                NVL(:jaechulgoSeq, '0'), NVL(:jaechulgoSagogu, '0'), NVL(:totalBox, 0), NVL(:jaechulgoOrder, 0),
                :jaechulgoNalgae, NVL(:jaechulgoNalgaeChk, '무'), :parentCustCd,
                SYSDATE, :insertId
            )
        """;

        jdbc.update(sql, new BeanPropertySqlParameterSource(vo));
    }


    @Transactional
    public void update(TmsJaechulgoVO vo) {
        String sql = """
            UPDATE TMS_JAECHULGO SET
                PRE_ORD_NO        = :preOrdNo,
                JAECHULGO_GB      = :jaechulgoGb,
                JAECHULGO_DT      = :jaechulgoDt,
                CHK_GB            = :chkGb,
                CHULPAN_CD        = :chulpanCd,
                SUJUM_CD          = :sujumCd,
                JAECHULGO_CNT     = :jaechulgoCnt,
                JAECHULGO_REQ_ID  = :jaechulgoReqId,
                JAECHULGO_APP_ID  = :jaechulgoAppId,
                JAECHULGO_QTY     = :jaechulgoQty,
                JAECHULGO_BOX     = :jaechulgoBox,
                ORDER_BOX         = :orderBox,
                BOX_COUNT         = :boxCount,
                JAECHULGO_DAMDANG_ID   = :jaechulgoDamdangId,
                JAECHULGO_J_DAMDANG_ID = :jaechulgoJDamdangId,
                BIGO              = :bigo,
                JAECHULGO_FAULT_GB= :jaechulgoFaultGb,
                JAECHULGO_REASON_GB= :jaechulgoReasonGb,
                CUS_CD            = :cusCd,
                CUS_DT            = :cusDt,
                JAECHULGO_JAECHECK= :jaechulgoJaecheck,
                JAECHULGO_OCHECK  = :jaechulgoOcheck,
                JAECHULGO_JUNCHECK= :jaechulgoJuncheck,
                JAECHULGO_ENDCHECK= :jaechulgoEndcheck,
                JAECHULGO_SEQ     = :jaechulgoSeq,
                JAECHULGO_SAGOGU  = :jaechulgoSagogu,
                TOTAL_BOX         = :totalBox,
                JAECHULGO_ORDER   = :jaechulgoOrder,
                JAECHULGO_NALGAE  = :jaechulgoNalgae,
                JAECHULGO_NALGAE_CHK = :jaechulgoNalgaeChk,
                PARENT_CUST_CD    = :parentCustCd,
                UPDATE_DT         = SYSDATE,
                UPDATE_ID         = :updateId
            WHERE USER_CET_CD = :userCetCd
              AND JAECHULGO_NO = :jaechulgoNo
        """;

        jdbc.update(sql, new BeanPropertySqlParameterSource(vo));
    }

    /**
     * 삭제
     */
    @Transactional
    public void delete(String userCetCd, String jaechulgoNo) {
        String sql = """
            DELETE FROM TMS_JAECHULGO
            WHERE USER_CET_CD = :userCetCd
              AND JAECHULGO_NO = :jaechulgoNo
        """;

        Map<String, Object> params = Map.of(
            "userCetCd", userCetCd,
            "jaechulgoNo", jaechulgoNo
        );

        jdbc.update(sql, params);
    }




    
}
