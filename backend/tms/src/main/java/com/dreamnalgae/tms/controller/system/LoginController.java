package com.dreamnalgae.tms.controller.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system")
//@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        
        String userId = request.get("userid").trim();
        String password = request.get("password").trim();

        String sql = "SELECT USER_ID, USER_NM, USER_CET_CD FROM TMS_COM_USER WHERE USER_ID = ? AND PASS_WD = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userId, password);

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
                "userId", user.get("USER_ID"),
                "userName", user.get("USER_NM"),
                "userCetCd", user.get("USER_CET_CD")
            );
        }
    }



}
