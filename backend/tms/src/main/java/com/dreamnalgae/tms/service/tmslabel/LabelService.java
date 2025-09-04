package com.dreamnalgae.tms.service.tmslabel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final NamedParameterJdbcTemplate jdbc;

    public List<Map<String, Object>> getAgency(String userCetCd) {
        String sql = """
            SELECT
                CUST_CD AS "custCd",
                CUST_NM AS "custNm"
            FROM TMS_CUST
            WHERE USER_CET_CD = :userCetCd
              AND CUST_DIV_GB = '10'
            ORDER BY CUST_NM
        """;

        Map<String,Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        return jdbc.queryForList(sql, params);
    }
    
}
