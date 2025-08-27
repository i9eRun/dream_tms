package com.dreamnalgae.tms.service.tbas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas20Service {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getCarList(Map<String, String> params) {
        String car_regnum = params.getOrDefault("CAR_REGNUM", "");
        String user_cet_cd = params.getOrDefault("USER_CET_CD", "");

        if (user_cet_cd.isEmpty()) {
            throw new IllegalArgumentException("USER_CET_CD 파라미터는 필수입니다.");
        }

        StringBuilder sql = new StringBuilder("""
            SELECT 
                c.car_cd,
                c.car_regnum,
                u.user_nm as car_driver,
                c.car_nm
            FROM TMS_CAR c
            LEFT JOIN TMS_COM_USER u ON c.USER_ID = u.USER_ID
            WHERE c.user_cet_cd = ?
        """);

        List<Object> queryParams = new ArrayList<>();
        queryParams.add(user_cet_cd);

        if (!car_regnum.isEmpty()) {
            sql.append(" AND c.car_regnum LIKE ? ");
            queryParams.add("%" + car_regnum + "%");
        }

        return jdbcTemplate.queryForList(sql.toString(), queryParams.toArray());
    }
    
}
