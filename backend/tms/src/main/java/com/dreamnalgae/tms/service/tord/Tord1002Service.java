package com.dreamnalgae.tms.service.tord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tord.OrderVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tord1002Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<OrderVO> selectMain(String frDt, String toDt, String chulpanCd, String sujumCd) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n")
           .append("      T1.ORD_NO,\n")
           .append("      T1.ORD_PO_NO,\n")
           .append("      T1.ORD_DT,\n")
           //.append("      TO_DATE(T1.ORD_DT, 'YYYYMMDD') AS ORD_DT,\n")
           .append("      NVL(T1.ORD_BOX, 0) AS ORD_BOX,\n")
           .append("      T1.CHULPAN_CD,\n")
           .append("      T1.SUJUM_CD,\n")
           .append("      T1.ORD_DIV_GB,\n")
           .append("      T1.CHULGO_GB,\n")
           .append("      T1.GULJAE_GB,\n")
           .append("      T1.ORD_INPUT_ID,\n")
           .append("      T1.ORD_CHK_GB,\n")
           .append("      T1.CHULGO_DT,\n")
           .append("      T1.SLIP_GB,\n")
           //.append("      TO_CHAR(T1.UNSONG_DT, 'YYYYMMDD') AS UNSONG_DT,\n")
           .append("      T1.UNSONG_DT,\n")
           .append("      T1.UNSONG_EXP_AMT,\n")
           .append("      T1.UNSONG_BIGO,\n")
           .append("      T1.BIGO,\n")
           //.append("      TO_CHAR(T1.LABEL_PRT_DT, 'YYYYMMDDHH24MISS') AS LABEL_PRT_DT,\n")
           .append("      T1.LABEL_PRT_DT,\n")
           .append("      T1.LABEL_PRT_ID,\n")
           .append("      TO_CHAR(T1.UNSONG_RECV_DT, 'YYYYMMDD') AS UNSONG_RECV_DT,\n")
           .append("      T1.UNSONG_RECV_ID,\n")
           .append("      T1.SINGAN_GB,\n")
           .append("      T1.TAKBAE_TEL_NO,\n")
           .append("      T1.TAKBAE_CUST_NM,\n")
           .append("      T1.TAKBAE_POST_NO,\n")
           .append("      T1.TAKBAE_ADDR,\n")
           .append("      T1.TAKBAE_BIGO,\n")
           .append("      T1.OLD_LABEL_PRT_ID,\n")
           .append("      T1.OLD_BOX_QTY,\n")
           .append("      TO_CHAR(T1.OLD_LABEL_PRT_DT, 'YYYYMMDD') AS OLD_LABEL_PRT_DT,\n")
           .append("      T1.ORD_ITM_QTY,\n")
           .append("      T1.ORD_WEIGHT,\n")
           .append("      T1.TRANS_GB,\n")
           //.append("      TO_CHAR(T1.ORD_REGI_TIME, 'YYYYMMDDHH24MISS') AS ORD_REGI_TIME,\n")
           .append("      TO_CHAR(ORD_REGI_TIME, 'HH24:MI:SS') AS ORD_REGI_TIME, ")
           .append("      NVL(T1.ORD_QTY, 0) AS ORD_QTY,\n")
           .append("      T1.BOX_SUM_QTY,\n")
           .append("      TO_CHAR(T1.ORD_SHEET_DT, 'YYYYMMDDHH24MISS') AS ORD_SHEET_DT,\n")
           .append("      T1.JEGO_JUNGSAN_YN,\n")
           .append("      T1.TAKBAE_INVOICE_NO,\n")
           .append("      T1.SALE_YN,\n")
           .append("      T1.STORE_CD,\n")
           .append("      T1.SPEC_PRT_CNT,\n")
           .append("      T1.DELIV_CD,\n")
           .append("      T1.DELIV_PATH_CD,\n")
           .append("      T1.TAKBAE_CP_TEL_NO,\n")
           .append("      T1.CHULPAN_GB,\n")
           .append("      T1.CHULPAN_ORD_DT,\n")
           .append("      T1.ORD_CNT,\n")
           .append("      T1.OMS_ROW_NO,\n")
           .append("      T1.ORD_NO_CHULPAN,\n")
           .append("      T1.JISI_NO,\n")
           .append("      T1.JISI_USER_ID,\n")
           .append("      T1.ITEM_GB,\n")
           .append("      T1.ORG_ORD_NO,\n")
           .append("      T1.ORD_AMT,\n")
           .append("      T1.SUGEO_ID,\n")
           .append("      SF_NM_CD(T1.SUGEO_ID,'TMS_COM_USER') AS SUGEO_NM,\n")
           .append("      T1.BAESONG_ID,\n")
           .append("      SF_NM_CD(T1.BAESONG_ID,'TMS_COM_USER') AS BAESONG_NM,\n")
           .append("      T1.MICHAK_GB,\n")
           .append("      T2.CUST_NM AS CHULPAN_NM,\n")
           .append("      T2.JIYUK_NM AS CHULPAN_JIYUK_NM,\n")
           .append("      T3.CUST_NM AS SUJUM_NM,\n")
           .append("      T3.JIYUK_NM AS SUJUM_JIYUK_NM,\n")
           //.append("      T4.USER_NM AS ORD_INPUT_NM,\n")
           .append("      ROW_NUMBER() OVER(ORDER BY T3.CUST_NM, T3.JIYUK_NM) AS RNUM\n")
           .append(" FROM TMS_ORDER_MST T1\n")
           .append(" JOIN TMS_CUST T2 ON T1.CHULPAN_CD = T2.CUST_CD AND T2.CUST_DIV_GB = '1'\n")
           .append(" JOIN TMS_CUST T3 ON T1.SUJUM_CD = T3.CUST_CD AND T3.CUST_DIV_GB = '2'\n")
           //.append(" JOIN TMS_COM_USER T4 ON T1.ORD_INPUT_ID = T4.USER_ID\n")
           .append(" WHERE T1.ORD_DIV_GB = '02'\n")
           .append(" AND T1.ORD_DT >= :frDt\n")
           .append(" AND T1.ORD_DT <= :toDt\n");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("frDt", frDt);
        params.addValue("toDt", toDt);

        if (chulpanCd != null && !chulpanCd.isEmpty()) {
            sql.append(" AND T1.CHULPAN_CD = :chulpanCd\n");
            params.addValue("chulpanCd", chulpanCd);
        }

        if (sujumCd != null && !sujumCd.isEmpty()) {
            sql.append(" AND T1.SUJUM_CD = :sujumCd\n");
            params.addValue("sujumCd", sujumCd);
        }

        System.out.println(sql.toString());

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(OrderVO.class));
    }
    



    // insert
    @Transactional
    public void insertOrder(OrderVO dto) {

        String ordNo = generateOrderNumber(dto.getInsertId());
        dto.setOrdNo(ordNo);
        dto.setCetCd("00001");

        String sql = """
 
          INSERT INTO TMS_ORDER_MST (
          USER_CET_CD, CET_CD, ORD_NO, ORD_DT, ORD_REGI_TIME, LABEL_PRT_DT,
          ORD_DIV_GB, ORD_PO_NO, CHULPAN_CD, SUJUM_CD,     
          BIGO, ORD_CHK_GB, ORD_INPUT_ID, ITEM_GB, CHULGO_GB,  
          GULJAE_GB, SINGAN_GB, ORD_QTY, ORD_BOX, ORD_AMT, MICHAK_GB,
          BAESONG_ID, SUGEO_ID, INSERT_ID, INSERT_DT, TRANS_GB, DELIV_PATH_CD
          ) 
          VALUES 
          (
          :userCetCd, :cetCd, :ordNo, :ordDt, SYSDATE, :labelPrtDt,
          '02', :ordPoNo, :chulpanCd, :sujumCd,
          :bigo, '01', :ordInputId, '01', :chulgoGb,
          '01', :singanGb, :ordQty, :ordBox, :ordAmt, :michakGb,
          :baesongId, :sugeoId, :insertId, :insertDt, '01',''
           )
        """;

         jdbc.update(sql, new BeanPropertySqlParameterSource(dto));
    }


    // 주문번호 생성
    public String generateOrderNumber(String insertId) {
         String sql = """
         SELECT SF_TMS_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_ORDER_MST', 'ORD_NO') AS ORD_NO
           FROM DUAL
           """;


        Map<String, Object> params = Map.of("insertId", insertId); // 또는 실제 사용자 구분값

        return jdbc.queryForObject(sql, params, String.class);
    }



    // update
    @Transactional
    public void updateOrder(OrderVO dto) {

        String sql = """
            UPDATE TMS_ORDER_MST SET
                ORD_DT = :ordDt,
                ORD_REGI_TIME = TO_DATE(:ordRegiTime, 'HH24:MI:SS'),
                LABEL_PRT_DT = :labelPrtDt,
                ORD_PO_NO = :ordPoNo,
                CHULPAN_CD = :chulpanCd,
                SUJUM_CD = :sujumCd,
                BIGO = :bigo,
                ORD_CHK_GB = :ordChkGb,
                ORD_INPUT_ID = :ordInputId,
                ITEM_GB = :itemGb,
                CHULGO_GB = :chulgoGb,
                GULJAE_GB = :guljaeGb,
                SINGAN_GB = :singanGb,
                ORD_QTY = :ordQty,
                ORD_BOX = :ordBox,
                ORD_AMT = :ordAmt,
                MICHAK_GB = :michakGb,
                BAESONG_ID = :baesongId,
                SUGEO_ID = :sugeoId,
                UPDATE_ID = :updateId,
                UPDATE_DT = SYSDATE
            WHERE USER_CET_CD = :userCetCd
            AND ORD_NO = :ordNo
        """;

        jdbc.update(sql, new BeanPropertySqlParameterSource(dto));
    }


    @Transactional
    public void deleteOrder(String userCetCd, String ordNo) {
        String sql = """
            DELETE FROM TMS_ORDER_MST
            WHERE USER_CET_CD = :userCetCd
            AND ORD_NO = :ordNo
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("ordNo", ordNo);

        jdbc.update(sql, params);
    }


        
}
