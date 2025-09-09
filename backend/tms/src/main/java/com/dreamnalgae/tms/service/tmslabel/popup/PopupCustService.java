package com.dreamnalgae.tms.service.tmslabel.popup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PopupCustService {
    private final NamedParameterJdbcTemplate jdbc;

    public List<Map<String, Object>> selectCustList(String keyword, String custDivGb, String useYn) {
        StringBuilder sql = new StringBuilder();

        sql.append("""
            SELECT
                CUST_CD, CUST_GB, CUST_DIV_GB,
                SF_NM_CODE(CUST_DIV_GB, '139') AS AS_CUST_DIV_GB,
                CUST_ABBR_NM, CUST_NM, BIZ_REG_NO, DAEPYO_NM, UPTAE_NM, JONGMOK_NM,
                TEL_NO, FAX_NO, EMAIL_ADDR, POST_NO, ADDR1, ADDR2,
                TRADE_FR_DT, TRADE_TO_DT, SINYONG_AMT, MISU_AMT, MAGAM_DD,
                CONTR_STORE_YN, DECODE(CONTR_STORE_YN, '1', 'Y', 'N') AS AS_CONTR_STORE_YN,
                JIYUK_NM, DELIV_PATH_CD, DELIV_POS_CD, CHARGE_EMP_ID, BIGO,
                SALE_TYPE_GB, SF_NM_CODE(SALE_TYPE_GB, '171') AS AS_SALE_TYPE_GB,
                GULJAE_GB, CHULGO_GB, SOSOK_NM, BANPOM_YN,
                DECODE(BANPOM_YN, '1', 'Y', 'N') AS AS_BANPOM_YN,
                CUST_DIV_CD1, CUST_DIV_CD2, BILL_YN,
                DECODE(BILL_YN, '1', 'Y', 'N') AS AS_BILL_YN,
                DM_SEND_YN, DECODE(DM_SEND_YN, '1', 'Y', 'N') AS AS_DM_SEND_YN,
                BIZ_NM, TEAM_CD, TEAM_NM, DAMDANG_NM, DELIV_DIV_CD,
                BUNJI_PRT_YN, DECODE(BUNJI_PRT_YN, '1', 'Y', 'N') AS AS_BUNJI_PRT_YN,
                SEND_EMAIL_ADDR, WORK_USER_ID, WORK_DT, USE_YN,
                BANP_DAMDANG_NM, BANP_TEAM_NM, ORD_METHOD_GB,
                SF_NM_CODE(ORD_METHOD_GB, '184') AS AS_ORD_METHOD_GB,
                BOK_INI_BOKWAN_AMT, CHUNGU_AMT_PRT_YN,
                DECODE(CHUNGU_AMT_PRT_YN, '1', 'Y', 'N') AS AS_CHUNGU_AMT_PRT_YN,
                CHULPAN_SUB_CD, BANP_EMAIL_ADDR, BONSA_SALE_NM, BOX_AUTO_YN,
                DECODE(BOX_AUTO_YN, '1', 'Y', 'N') AS AS_BOX_AUTO_YN,
                HOMEPAGE_ADDR, HOMENAME_CUST_NM, HOMEPAGE_USER_ID, HOMEPAGE_YN,
                DECODE(HOMEPAGE_YN, '1', 'Y', 'N') AS AS_HOMEPAGE_YN,
                BAEBON_CD, SQUARE_SIZE, GARO_SIZE, SERO_SIZE, DEPTH_SIZE,
                RECV_POST_NO, RECV_ADDR1, RECV_DAMDANG_NM, RECV_NICK_NM,
                WEB_ORD_GB, CHULGO_SORT_GB, CHUNGU_DAMDANG_NM, CHUNGU_NICK_NM,
                JAKUP_IND_CNT, BOX_BOK_QTY_YN, BOK_WMS_CNV_YN, TAKBAE_CD,
                SF_NM_CODE(TAKBAE_CD, '191') AS AS_TAKBAE_CD,
                PARENT_CUST_CD, SF_NM_CD(PARENT_CUST_CD, 'TMS_CUST') AS AS_PARENT_CUST_CD,
                MGT_LEVEL, DAEPYO_EMAIL_ADDR, DAEPYO_NM_YN,
                DECODE(DAEPYO_NM_YN, '1', 'Y', 'N') AS AS_DAEPYO_NM_YN,
                DAEPYO_CP_TEL_NO, ACC_CD, ACC_EMAIL_ADDR, BOK_PANSWAE_YN
            FROM TMS_CUST
            WHERE 1 = 1
            AND (
                CUST_NM LIKE '%' || :keyword || '%'
                OR CUST_ABBR_NM LIKE '%' || :keyword || '%'
                OR CUST_CD LIKE :keyword || '%'
                OR DAMDANG_NM LIKE '%' || :keyword || '%'
                OR JIYUK_NM LIKE :keyword || '%'
                OR PARENT_CUST_CD LIKE :keyword || '%'
            )
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);

        if (custDivGb != null && !custDivGb.isEmpty()) {
            sql.append(" AND CUST_DIV_GB = :custDivGb\n");
            params.put("custDivGb", custDivGb);
        }

        if (useYn != null && !useYn.isEmpty()) {
            sql.append(" AND USE_YN = :useYn\n");
            params.put("useYn", useYn);
        }

        sql.append("""
            ORDER BY
                DECODE(INSTR(CUST_ABBR_NM, :keywordOrder), 0, 999999, INSTR(CUST_ABBR_NM, :keywordOrder)),
                CUST_ABBR_NM,
                CUST_CD
        """);
        params.put("keywordOrder", keyword);

        return jdbc.query(sql.toString(), params, new ColumnMapRowMapper());
    }
}
