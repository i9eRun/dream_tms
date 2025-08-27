package com.dreamnalgae.tms.controller.tbas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.service.tbas.Tbas20Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas")
//@CrossOrigin(origins = "*")
public class Tbas20Controller {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Tbas20Service tbas20Service;

    @GetMapping("/carlist")
    public List<Map<String, Object>> getCList(@RequestParam Map<String, String> params) {
        System.out.println(params);
        return tbas20Service.getCarList(params);
    }







    @GetMapping("/carlist/test1")
     public List<Map<String, Object>> getCarList_test1(
        @RequestParam(name = "CAR_REGNUM", required = false) String carRegnum,
        @RequestParam(name = "USER_CET_CD", required = true) String userCetCd) {

        // USER_CET_CD가 없을 경우 예외 처리
        if (userCetCd == null || userCetCd.trim().isEmpty()) {
            throw new IllegalArgumentException("USER_CET_CD 파라미터는 필수입니다.");
        }

        StringBuilder sql = new StringBuilder("""
            SELECT 
                c.car_cd,
                c.car_regnum,
                u.user_nm,
                c.car_nm
            FROM TMS_CAR c
            LEFT JOIN TMS_COM_USER u ON c.USER_ID = u.USER_ID
            WHERE c.user_cet_cd = ?
        """);

        List<Object> params = new ArrayList<>();

        params.add(userCetCd.trim());

        if (carRegnum != null && !carRegnum.trim().isEmpty()) {
            sql.append(" AND c.car_regnum LIKE ? ");
            params.add("%" + carRegnum.trim() + "%");
        }

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }


    @GetMapping("/carlist/test2")
    public List<Map<String,Object>> getCarList_test2 (@RequestParam Map<String, String> params) {
        String carRegnum = params.getOrDefault("CAR_REGNUM", "").trim();
        String userCetCd = params.getOrDefault("USER_CET_CD", "").trim();

        if (userCetCd.isEmpty()) {
            throw new IllegalArgumentException("USER_CET_CD 파라미터는 필수입니다.");
        }

        StringBuilder sql = new StringBuilder("""
             SELECT 
                c.car_cd,
                c.car_regnum,
                u.user_nm,
                c.car_nm
            FROM TMS_CAR c
            LEFT JOIN TMS_COM_USER u ON c.USER_ID = u.USER_ID
            WHERE 1=1
        """
        );

        List<Object> queryParams = new ArrayList<>();

        if (!carRegnum.isEmpty()) {
            sql.append(" AND c.car_regnum LIKE ? ");
            queryParams.add("%" + carRegnum + "%");
        }

        if (!userCetCd.isEmpty()) {
            sql.append(" AND c.user_cet_cd = ? ");
            queryParams.add(userCetCd);
        }

        return jdbcTemplate.queryForList(sql.toString(), queryParams.toArray());

    }
    
}
