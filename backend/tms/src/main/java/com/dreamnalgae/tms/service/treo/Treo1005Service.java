package com.dreamnalgae.tms.service.treo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.treo.treo1005.TmsBanpRetnMstVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Treo1005Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsBanpRetnMstVO> selectList(String userCetCd, String chulpanCd, String sujumCd, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                M.*,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = M.CHULPAN_CD AND ROWNUM = 1) AS CHULPAN_NM,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = M.SUJUM_CD AND ROWNUM = 1) AS SUJUM_NM
            FROM TMS_BANP_RETN_MST M
            WHERE M.USER_CET_CD = :userCetCd
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        if (StringUtils.hasText(chulpanCd)) {
            sql.append(" AND M.CHULPAN_CD = :chulpanCd");
            params.put("chulpanCd", chulpanCd);
        }

        if (StringUtils.hasText(sujumCd)) {
            sql.append(" AND M.SUJUM_CD = :sujumCd");
            params.put("sujumCd", sujumCd);
        }

        if (StringUtils.hasText(startDate)) {
            sql.append(" AND M.BANP_RETN_DT >= :startDate ");
            params.put("startDate", startDate);
        }

        if (StringUtils.hasText(endDate)) {
            sql.append(" AND M.BANP_RETN_DT <= :endDate ");
            params.put("endDate", endDate);
        }

        sql.append(" ORDER BY M.BANP_RETN_DT, M.BANP_RETN_NO DESC");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsBanpRetnMstVO.class));
    }


    public String generateBanpRetnNo(String insertId) {
        String sql = """
            SELECT SF_TMS_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_BANP_RETN_MST', 'BANP_RETN_NO') AS BANP_RETN_NO
            FROM DUAL
        """;

        Map<String, Object> params = Map.of("insertId", insertId);
        return jdbc.queryForObject(sql, params, String.class);
    }



    @Transactional
    public void insertBanpRetn(TmsBanpRetnMstVO vo) {
        String sql = """
            INSERT INTO TMS_BANP_RETN_MST (
                USER_CET_CD, BANP_RETN_NO, BANP_IBGO_REG_DT, CUST_IBGO_NO, BANP_IBGO_SCAN_NO,
                BANP_RETN_DT, BANP_GULJAE_GB, CHULPAN_CD, SUJUM_CD, REGI_USER_ID,
                BANP_QTY, BANP_BOX, DELIV_CD, DELIV_PATH_CD, TRANS_GB,
                UNSONG_EXP_AMT, BIGO, INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :banpRetnNo, :banpIbgoRegDt, :custIbgoNo, :banpIbgoScanNo,
                :banpRetnDt, :banpGuljaeGb, :chulpanCd, :sujumCd, :regiUserId,
                :banpQty, :banpBox, :delivCd, :delivPathCd, :transGb,
                :unsongExpAmt, :bigo, SYSDATE, :insertId
            )
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("userCetCd", vo.getUserCetCd())
            .addValue("banpRetnNo", vo.getBanpRetnNo())
            .addValue("banpIbgoRegDt", vo.getBanpIbgoRegDt())
            .addValue("custIbgoNo", vo.getCustIbgoNo())
            .addValue("banpIbgoScanNo", vo.getBanpIbgoScanNo())
            .addValue("banpRetnDt", vo.getBanpRetnDt())
            .addValue("banpGuljaeGb", vo.getBanpGuljaeGb())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("regiUserId", vo.getRegiUserId())
            .addValue("banpQty", vo.getBanpQty())
            .addValue("banpBox", vo.getBanpBox())
            .addValue("delivCd", vo.getDelivCd())
            .addValue("delivPathCd", vo.getDelivPathCd())
            .addValue("transGb", vo.getTransGb())
            .addValue("unsongExpAmt", vo.getUnsongExpAmt())
            .addValue("bigo", vo.getBigo())
            .addValue("insertId", vo.getInsertId());

        jdbc.update(sql, param);
    }

    @Transactional
    public void updateBanpRetn(TmsBanpRetnMstVO vo) {
        String sql = """
            UPDATE TMS_BANP_RETN_MST
            SET
                BANP_IBGO_REG_DT = :banpIbgoRegDt,
                CUST_IBGO_NO = :custIbgoNo,
                BANP_IBGO_SCAN_NO = :banpIbgoScanNo,
                BANP_RETN_DT = :banpRetnDt,
                BANP_GULJAE_GB = :banpGuljaeGb,
                CHULPAN_CD = :chulpanCd,
                SUJUM_CD = :sujumCd,
                REGI_USER_ID = :regiUserId,
                BANP_QTY = :banpQty,
                BANP_BOX = :banpBox,
                DELIV_CD = :delivCd,
                DELIV_PATH_CD = :delivPathCd,
                TRANS_GB = :transGb,
                UNSONG_EXP_AMT = :unsongExpAmt,
                BIGO = :bigo,
                UPDATE_DT = SYSDATE,
                UPDATE_ID = :updateId
            WHERE BANP_RETN_NO = :banpRetnNo
            AND USER_CET_CD = :userCetCd
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("banpIbgoRegDt", vo.getBanpIbgoRegDt())
            .addValue("custIbgoNo", vo.getCustIbgoNo())
            .addValue("banpIbgoScanNo", vo.getBanpIbgoScanNo())
            .addValue("banpRetnDt", vo.getBanpRetnDt())
            .addValue("banpGuljaeGb", vo.getBanpGuljaeGb())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("regiUserId", vo.getRegiUserId())
            .addValue("banpQty", vo.getBanpQty())
            .addValue("banpBox", vo.getBanpBox())
            .addValue("delivCd", vo.getDelivCd())
            .addValue("delivPathCd", vo.getDelivPathCd())
            .addValue("transGb", vo.getTransGb())
            .addValue("unsongExpAmt", vo.getUnsongExpAmt())
            .addValue("bigo", vo.getBigo())
            .addValue("updateId", vo.getUpdateId())
            .addValue("banpRetnNo", vo.getBanpRetnNo())
            .addValue("userCetCd", vo.getUserCetCd());

        jdbc.update(sql, param);
    }

    @Transactional
    public void deleteBanpRetn(String banpRetnNo, String userCetCd) {
        String sql = """
            DELETE FROM TMS_BANP_RETN_MST
            WHERE BANP_RETN_NO = :banpRetnNo
            AND USER_CET_CD = :userCetCd
        """;

        Map<String, Object> params = Map.of(
            "banpRetnNo", banpRetnNo,
            "userCetCd", userCetCd
        );

        jdbc.update(sql, params);
    }





}
