package com.dreamnalgae.tms.service.tpop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tpop1002Service {
    private final NamedParameterJdbcTemplate jdbc;

        // String teamGb , String strWhere , String methodGb 필요없어서 파라미터인자에서 제거
        public List<Map<String, Object>> selectTable(String strKeyword, String useYn) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append("""
            SELECT
                DEPT_CD,
                N_DEPT_CD,
                DEPT_NM,
                JIYUK_CD,
                DEPT_CD_OLD,
                DEPT_NM_OLD,
                DEPT_GWA_NM_OLD,
                DEPT_BAN_NM_OLD,
                DEPT_CHO_NM_OLD,
                DEPT_ACC_CD,
                TEAM_HEAD_NM_OLD,
                USE_YN,
                DECODE(USE_YN, '1', '사용', '미사용') AS AS_USE_YN,
                TEAM_NM,
                OLD_TEAM_NM,
                TEAM_GB,
                ARANGE_SEQ1,
                ARANGE_SEQ2,
                NEW_TEAM_ABBR_NM,
                NEW_TEAM_NM,
                NEW_CHO_NM,
                NEW_TEAM_HEAD_NM,
                JO_HEAD_NM,
                TOT_HEAD_NM,
                MGR_HEAD_NM,
                INSA_DEPT_CD_OLD
            FROM TMS_DEPT
            WHERE 1 = 1
              AND (N_DEPT_CD = :keyword OR DEPT_NM LIKE '%' || :keyword || '%')
        """);

        params.put("keyword", strKeyword);

        // if (teamGb != null && !teamGb.trim().isEmpty()) {
        //     if ("1".equals(methodGb)) {
        //         sql.append(" AND (TEAM_GB IN (:teamGb, '3')) \n");
        //         params.put("teamGb", teamGb);
        //     } else {
        //         sql.append(" AND TEAM_GB = :teamGb \n");
        //         params.put("teamGb", teamGb);
        //     }
        // }

        if (useYn != null && !useYn.trim().isEmpty()) {
            sql.append(" AND USE_YN = :useYn \n");
            params.put("useYn", useYn);
        }

        // if (strWhere != null && !strWhere.trim().isEmpty()) {
        //     sql.append(" AND (").append(strWhere).append(") \n");
        // }

        sql.append("""
            ORDER BY
                DECODE(INSTR(DEPT_NM, :keywordOrder), 0, 999999, INSTR(DEPT_NM, :keywordOrder)),
                N_DEPT_CD
        """);

        params.put("keywordOrder", strKeyword);

        return jdbc.query(sql.toString(), params, new ColumnMapRowMapper());
    }
}
