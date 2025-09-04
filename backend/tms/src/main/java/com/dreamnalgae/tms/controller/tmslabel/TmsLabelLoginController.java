package com.dreamnalgae.tms.controller.tmslabel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tmslabel")
public class TmsLabelLoginController {
    private final NamedParameterJdbcTemplate jdbc;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String userId = request.get("userId").trim();
        String password = request.get("password").trim();

        String sql = """
            SELECT
                 CUST_LOG_ID,
                 CUST_NM,
                 CUST_CD,
                 USER_CET_CD,
                 CUST_DIV_GB,
                 CUST_NM
            FROM TMS_COM_USER_CUST
            WHERE CUST_LOG_ID =:userId AND CUST_PASS =:password
        """;

        Map<String,Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("password", password);

        List<Map<String, Object>> result = jdbc.queryForList(sql, params);

        if (result.isEmpty()) {
            return Map.of(
                "success", false,
                "message", "아이디 또는 비밀번호가 올바르지 않습니다."
            );
        } else {
            Map<String, Object> user = result.get(0);
            return Map.of(
                "success", true,
                "message", "로그인 성공",
                "custId", user.get("CUST_LOG_ID"),
                "custName", user.get("CUST_NM"),
                "custCode", user.get("CUST_CD"),
                "userCetCd", user.get("USER_CET_CD"),
                "custDivGb", user.get("CUST_DIV_GB")
            );
        }
    }
    
}
