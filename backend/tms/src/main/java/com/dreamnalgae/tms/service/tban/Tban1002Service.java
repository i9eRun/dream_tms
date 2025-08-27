package com.dreamnalgae.tms.service.tban;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.tban.tban1002.TmsBanpIbgoMstVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tban1002Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsBanpIbgoMstVO> selectList(String userCetCd, String chulpanCd, String sujumCd, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                M.BANP_IBGO_NO,
                M.BANP_IBGO_DT,
                M.SUBUL_GB ,
                M.SUBUL_TYPE_GB ,
                M.BANP_GULJAE_GB ,
                M.CHULPAN_CD ,
                M.SUJUM_CD ,
                M.BANP_QTY ,
                M.BANP_BOX ,
                M.BIGO,
                M.INSERT_ID,
                M.INSERT_DT ,
                M.UPDATE_ID ,
                M.UPDATE_DT ,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = M.CHULPAN_CD AND ROWNUM = 1) AS CHULPAN_NM,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = M.SUJUM_CD AND ROWNUM = 1) AS SUJUM_NM
            FROM TMS_BANP_IBGO_MST M
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
            sql.append(" AND M.BANP_IBGO_DT >= :startDate ");
            params.put("startDate", startDate);
        }

        if (StringUtils.hasText(endDate)) {
            sql.append(" AND M.BANP_IBGO_DT <= :endDate ");
            params.put("endDate", endDate);
        }

        sql.append(" ORDER BY M.BANP_IBGO_DT, M.BANP_IBGO_NO DESC");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsBanpIbgoMstVO.class));
    }




    public String generateBanpIbgoNo(String insertId) {
        String sql = """
            SELECT SF_TMS_NO(:insertId, TO_CHAR(SYSDATE, 'YYYYMMDD'), 'TMS_BANP_IBGO_MST', 'BANP_IBGO_NO') AS BANP_IBGO_NO
            FROM DUAL
        """;

        Map<String, Object> params = Map.of("insertId", insertId);
        return jdbc.queryForObject(sql, params, String.class);
    }



    @Transactional
    public void insertBanpIbgo(TmsBanpIbgoMstVO vo) {
        String sql = """
            INSERT INTO TMS_BANP_IBGO_MST (
                USER_CET_CD,
                BANP_IBGO_NO,
                BANP_IBGO_DT, 
                SUBUL_GB,
                SUBUL_TYPE_GB,
                BANP_GULJAE_GB,
                CHULPAN_CD,
                SUJUM_CD,
                BANP_QTY,
                BANP_BOX,
                BIGO,
                INSERT_DT,
                INSERT_ID
            ) VALUES (
                :userCetCd, :banpIbgoNo, :banpIbgoDt, :subulGb, :subulTypeGb,
                :banpGuljaeGb, :chulpanCd, :sujumCd, :banpQty, :banpBox,
                :bigo, SYSDATE, :insertId
            )
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("userCetCd", vo.getUserCetCd())
            .addValue("banpIbgoNo", vo.getBanpIbgoNo())
            .addValue("banpIbgoDt", vo.getBanpIbgoDt())
            .addValue("subulGb", vo.getSubulGb())
            .addValue("subulTypeGb", vo.getSubulTypeGb())
            .addValue("banpGuljaeGb", vo.getBanpGuljaeGb())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("banpQty", vo.getBanpQty())
            .addValue("banpBox", vo.getBanpBox())
            .addValue("bigo", vo.getBigo())
            .addValue("insertId", vo.getInsertId());

        jdbc.update(sql, param);
    }


    @Transactional
    public void updateBanpIbgo(TmsBanpIbgoMstVO vo) {
        String sql = """
            UPDATE TMS_BANP_IBGO_MST
            SET
                BANP_IBGO_DT = :banpIbgoDt,
                SUBUL_GB = :subulGb,
                SUBUL_TYPE_GB = :subulTypeGb,
                BANP_GULJAE_GB = :banpGuljaeGb,
                CHULPAN_CD = :chulpanCd,
                SUJUM_CD = :sujumCd,
                BANP_QTY = :banpQty,
                BANP_BOX = :banpBox,
                BIGO = :bigo,
                UPDATE_DT = SYSDATE,
                UPDATE_ID = :updateId
            WHERE BANP_IBGO_NO = :banpIbgoNo
            AND USER_CET_CD = :userCetCd
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("banpIbgoDt", vo.getBanpIbgoDt())
            .addValue("subulGb", vo.getSubulGb())
            .addValue("subulTypeGb", vo.getSubulTypeGb())
            .addValue("banpGuljaeGb", vo.getBanpGuljaeGb())
            .addValue("chulpanCd", vo.getChulpanCd())
            .addValue("sujumCd", vo.getSujumCd())
            .addValue("banpQty", vo.getBanpQty())
            .addValue("banpBox", vo.getBanpBox())
            .addValue("bigo", vo.getBigo())
            .addValue("updateId", vo.getUpdateId())
            .addValue("banpIbgoNo", vo.getBanpIbgoNo())
            .addValue("userCetCd", vo.getUserCetCd());

        jdbc.update(sql, param);
    }



    @Transactional
    public void deleteBanpIbgo(String banpIbgoNo, String userCetCd) {
        String sql = """
            DELETE FROM TMS_BANP_IBGO_MST
            WHERE BANP_IBGO_NO = :banpIbgoNo
            AND USER_CET_CD = :userCetCd
        """;

        Map<String, Object> params = Map.of(
            "banpIbgoNo", banpIbgoNo,
            "userCetCd", userCetCd
        );

        jdbc.update(sql, params);
    }


    
}
