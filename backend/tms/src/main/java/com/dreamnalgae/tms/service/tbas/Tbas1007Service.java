package com.dreamnalgae.tms.service.tbas;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tbas.tbas1007.HolidayVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1007Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<HolidayVO> selectMain(HolidayVO vo) {
        String sql = """
            SELECT 
                DDAY,
                DDAY_YO,
                BIGO
            FROM TMS_HOLIDAY
            WHERE DDAY BETWEEN :startDate AND :endDate
            AND USER_CET_CD = :userCetCd
            ORDER BY DDAY
        """;

        return jdbc.query(sql, new BeanPropertySqlParameterSource(vo), new BeanPropertyRowMapper<>(HolidayVO.class));
    }



    @Transactional
    public void insertHoliday(HolidayVO vo) {
        String sql = """
            MERGE INTO TMS_HOLIDAY A
            USING (SELECT :dday AS DDAY FROM DUAL) B
            ON (A.DDAY = B.DDAY AND A.USER_CET_CD = :userCetCd)
            WHEN MATCHED THEN
                UPDATE SET BIGO = :bigo
            WHEN NOT MATCHED THEN
                INSERT (DDAY, BIGO, USER_CET_CD)
                VALUES (:dday, :bigo, :userCetCd)
        """;
        jdbc.update(sql, new BeanPropertySqlParameterSource(vo));
    }



    @Transactional
    public void deleteHoliday(HolidayVO vo) {
        String sql = """
            DELETE FROM TMS_HOLIDAY
            WHERE DDAY = :dday AND USER_CET_CD = :userCetCd
        """;
        jdbc.update(sql, new BeanPropertySqlParameterSource(vo));
    }




    
}
