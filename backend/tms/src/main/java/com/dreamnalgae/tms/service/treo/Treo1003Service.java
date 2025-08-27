package com.dreamnalgae.tms.service.treo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.treo.treo1003.OrderRetnMstVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Treo1003Service {

    private final NamedParameterJdbcTemplate jdbc;

    public List<OrderRetnMstVO> selectList(String userCetCd, String chulpanCd, String sujumCd, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                USER_CET_CD,
                ORD_RETN_NO,
                ORD_RETN_DT,
                CHULPAN_CD,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = M.CHULPAN_CD AND ROWNUM = 1) AS CHULPAN_NM,
                SUJUM_CD,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = M.SUJUM_CD AND ROWNUM = 1) AS SUJUM_NM,
                REGI_USER_ID,
                TRANS_GB,
                UNSONG_EXP_AMT,
                DELIV_CD,
                DELIV_PATH_CD,
                ORD_QTY,
                ORD_BOX,
                INSERT_ID,
                BIGO
            FROM TMS_ORDER_RETN_MST M
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
            sql.append(" AND ORD_RETN_DT >= :startDate ");
            params.put("startDate", startDate);
        }

        if (StringUtils.hasText(endDate)) {
            sql.append(" AND ORD_RETN_DT <= :endDate ");
            params.put("endDate", endDate);
        }

        sql.append(" ORDER BY ORD_RETN_DT DESC, ORD_RETN_NO DESC");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(OrderRetnMstVO.class));
    }

    public String generateOrdRetnNo(String insertId) {
        String sql = """
            SELECT SF_TMS_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_ORDER_RETN_MST', 'ORD_RETN_NO') AS ORD_RETN_NO
            FROM DUAL
        """;
        Map<String, Object> params = Map.of("insertId", insertId);
        return jdbc.queryForObject(sql, params, String.class);
    }


    @Transactional
    public void insertOrderRetn(OrderRetnMstVO vo) {
        String sql = """
            INSERT INTO TMS_ORDER_RETN_MST (
                USER_CET_CD, ORD_RETN_NO, ORD_RETN_DT, CHULPAN_CD, SUJUM_CD,
                REGI_USER_ID, TRANS_GB, UNSONG_EXP_AMT, DELIV_CD, DELIV_PATH_CD,
                ORD_QTY, ORD_BOX, BIGO, INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :ordRetnNo, :ordRetnDt, :chulpanCd, :sujumCd,
                :insertId, :transGb, :unsongExpAmt, :delivCd, :delivPathCd,
                :ordQty, :ordBox, :bigo, SYSDATE, :insertId
            )
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("userCetCd", vo.getUserCetCd())
            .addValue("ordRetnNo", vo.getOrdRetnNo())
            .addValue("ordRetnDt", vo.getOrdRetnDt())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("insertId", vo.getInsertId())
            .addValue("transGb", vo.getTransGb())
            .addValue("unsongExpAmt", vo.getUnsongExpAmt())
            .addValue("delivCd", vo.getDelivCd())
            .addValue("delivPathCd", vo.getDelivPathCd())
            .addValue("ordQty", vo.getOrdQty())
            .addValue("ordBox", vo.getOrdBox())
            .addValue("bigo", vo.getBigo());

        jdbc.update(sql, param);
    }


    @Transactional
    public void update(OrderRetnMstVO vo) {
        String sql = """
            UPDATE TMS_ORDER_RETN_MST
            SET
                ORD_RETN_DT = :ordRetnDt,
                CHULPAN_CD = :chulpanCd,
                SUJUM_CD = :sujumCd,
                TRANS_GB = :transGb,
                UNSONG_EXP_AMT = :unsongExpAmt,
                DELIV_CD = :delivCd,
                DELIV_PATH_CD = :delivPathCd,
                ORD_QTY = :ordQty,
                ORD_BOX = :ordBox,
                BIGO = :bigo,
                UPDATE_ID = :updateId,
                UPDATE_DT = SYSDATE
            WHERE ORD_RETN_NO = :ordRetnNo
              AND USER_CET_CD = :userCetCd
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("ordRetnDt", vo.getOrdRetnDt())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("transGb", vo.getTransGb())
            .addValue("unsongExpAmt", vo.getUnsongExpAmt())
            .addValue("delivCd", vo.getDelivCd())
            .addValue("delivPathCd", vo.getDelivPathCd())
            .addValue("ordQty", vo.getOrdQty())
            .addValue("ordBox", vo.getOrdBox())
            .addValue("bigo", vo.getBigo())
            .addValue("updateId", vo.getUpdateId())
            .addValue("ordRetnNo", vo.getOrdRetnNo())
            .addValue("userCetCd", vo.getUserCetCd());

        jdbc.update(sql, param);
    }




    @Transactional
    public void delete(String ordRetnNo, String userCetCd) {
        String sql = """
            DELETE FROM TMS_ORDER_RETN_MST
            WHERE ORD_RETN_NO = :ordRetnNo
              AND USER_CET_CD = :userCetCd
        """;

        Map<String, Object> params = Map.of(
            "ordRetnNo", ordRetnNo,
            "userCetCd", userCetCd
        );

        jdbc.update(sql, params);
    }

    
}
