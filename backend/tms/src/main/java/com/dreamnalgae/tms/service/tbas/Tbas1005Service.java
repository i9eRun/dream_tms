package com.dreamnalgae.tms.service.tbas;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tsys.TmsCusVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1005Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsCusVO> selectCourseList(String userCetCd, String keyword) {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                c.CUS_CD,
                c.CUS_NM,
                c.CUS_GB,
                c.CAR_CD,
                c.CUS_USE,
                c.CUS_AMPM_GB,
                c.CUS_BONSA,
                c.CUS_TMS_GB,
                c.USER_ID,
                c.CUS_AMPM,
                c.PARENT_CUS_CD,
                c.USER_CET_CD,
                c.INSERT_ID,
                c.INSERT_DT,
                c.UPDATE_ID,
                c.UPDATE_DT,
                u.USER_NM,
                car.CAR_NM,
                car.CAR_REGNUM
            FROM TMS_CUS c
            LEFT JOIN TMS_COM_USER u ON c.USER_ID = u.USER_ID
            LEFT JOIN TMS_CAR car ON c.CAR_CD = car.CAR_CD
            WHERE c.USER_CET_CD = :userCetCd
        """);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userCetCd", userCetCd);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND CUS_NM LIKE :keyword");
            params.addValue("keyword", "%" + keyword + "%");
        }

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsCusVO.class));
    }
    

    @Transactional
    public boolean insertCus(TmsCusVO vo) {
        // 0. 필수 파라미터 유효성 검사
        if (vo.getUserCetCd() == null || vo.getUserCetCd().trim().isEmpty()) {
            throw new IllegalArgumentException("USER_CET_CD는 필수 항목입니다.");
        }

        if (vo.getCusCd() == null || vo.getCusCd().trim().isEmpty()) {
            throw new IllegalArgumentException("CUS_CD는 필수 항목입니다.");
        }

        // 1. 중복 여부 확인
        String checkSql = """
            SELECT COUNT(*)
            FROM TMS_CUS
            WHERE USER_CET_CD = :userCetCd AND CUS_CD = :cusCd
        """;

        MapSqlParameterSource checkParams = new MapSqlParameterSource()
            .addValue("userCetCd", vo.getUserCetCd())
            .addValue("cusCd", vo.getCusCd());

        Integer count = jdbc.queryForObject(checkSql, checkParams, Integer.class);
        if (count != null && count > 0) {
            throw new IllegalArgumentException("이미 존재하는 코스입니다. (USER_CET_CD + CUS_CD 중복)");
        }

        // 2. INSERT 실행
        String insertSql = """
            INSERT INTO TMS_CUS (
                USER_CET_CD, CUS_CD, CUS_NM, CUS_GB, CAR_CD,
                CUS_USE, CUS_AMPM_GB, CUS_BONSA, CUS_TMS_GB,
                USER_ID, CUS_AMPM, PARENT_CUS_CD, INSERT_ID, INSERT_DT
            ) VALUES (
                :userCetCd, :cusCd, :cusNm, :cusGb, :carCd,
                :cusUse, :cusAmpmGb, :cusBonsa, :cusTmsGb,
                :userId, :cusAmpm, :parentCusCd, :insertId, SYSDATE
            )
        """;

        BeanPropertySqlParameterSource insertParams = new BeanPropertySqlParameterSource(vo);
        return jdbc.update(insertSql, insertParams) > 0;
    }


    @Transactional
    public boolean updateCus(TmsCusVO vo) {
        if (vo.getUserCetCd() == null || vo.getUserCetCd().trim().isEmpty()) {
            throw new IllegalArgumentException("USER_CET_CD는 필수 항목입니다.");
        }

        if (vo.getCusCd() == null || vo.getCusCd().trim().isEmpty()) {
            throw new IllegalArgumentException("CUS_CD는 필수 항목입니다.");
        }

        String sql = """
            UPDATE TMS_CUS
            SET 
                CUS_NM = :cusNm,
                CUS_GB = :cusGb,
                CAR_CD = :carCd,
                CUS_USE = :cusUse,
                CUS_AMPM_GB = :cusAmpmGb,
                CUS_BONSA = :cusBonsa,
                CUS_TMS_GB = :cusTmsGb,
                USER_ID = :userId,
                CUS_AMPM = :cusAmpm,
                PARENT_CUS_CD = :parentCusCd,
                UPDATE_ID = :updateId,
                UPDATE_DT = SYSDATE
            WHERE USER_CET_CD = :userCetCd AND CUS_CD = :cusCd
        """;

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        return jdbc.update(sql, params) > 0;
    }


    @Transactional
    public boolean deleteCus(String userCetCd, String cusCd) {
        if (userCetCd == null || userCetCd.trim().isEmpty()) {
            throw new IllegalArgumentException("USER_CET_CD는 필수입니다.");
        }
        if (cusCd == null || cusCd.trim().isEmpty()) {
            throw new IllegalArgumentException("CUS_CD는 필수입니다.");
        }

        String sql = """
            DELETE FROM TMS_CUS
            WHERE USER_CET_CD = :userCetCd AND CUS_CD = :cusCd
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userCetCd", userCetCd)
            .addValue("cusCd", cusCd);

        return jdbc.update(sql, params) > 0;
    }


}
