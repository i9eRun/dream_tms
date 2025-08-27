package com.dreamnalgae.tms.service.tsys;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tsys.TmsCusMenuVO;
import com.dreamnalgae.tms.model.tsys.TmsCusUserVO;
import com.dreamnalgae.tms.model.tsys.TmsCusVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsys1004Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsCusVO> selectCourseList(String userCetCd) {
        String sql = """
                SELECT * FROM TMS_CUS
                WHERE USER_CET_CD = :userCetCd
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userCetCd", userCetCd);

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(TmsCusVO.class));
    }


    public List<String> selectUserCusList(String userCetCd, String userId) {
        String sql = """
                SELECT CUS_CD FROM TMS_CUS_MENU
                WHERE USER_CET_CD = :userCetCd AND USER_ID = :userId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userCetCd", userCetCd);
        params.addValue("userId", userId);

        return jdbc.queryForList(sql, params, String.class);
    }


    public List<TmsCusUserVO> selectUserList(String userCetCd, String keyword) {
        StringBuilder sql = new StringBuilder("""
            SELECT u.USER_ID, u.USER_NM, d.DEPT_NM
            FROM TMS_COM_USER u
            LEFT JOIN TMS_DEPT d ON u.N_DEPT_CD = d.N_DEPT_CD
            WHERE u.USER_CET_CD = :userCetCd
        """);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userCetCd", userCetCd);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                AND (
                    u.USER_ID LIKE '%' || :keyword || '%'
                    OR u.USER_NM LIKE '%' || :keyword || '%'
                )
            """);
            params.addValue("keyword", keyword);
        }

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsCusUserVO.class));
    }



    @Transactional
    public void insertCusMenus(List<TmsCusMenuVO> list) {
        if (list == null || list.isEmpty()) return;

        // 사용자 고유 ID + USER_CET_CD 조합 수집
        Set<String> userIds = list.stream()
            .map(TmsCusMenuVO::getUserId)
            .collect(Collectors.toSet());

        String userCetCd = list.get(0).getUserCetCd(); // 공통값이라고 가정

        // 1단계: 기존 권한 삭제
        String deleteSql = """
            DELETE FROM TMS_CUS_MENU
            WHERE USER_CET_CD = :userCetCd
            AND USER_ID IN (:userIds)
        """;
        Map<String, Object> deleteParams = Map.of(
            "userCetCd", userCetCd,
            "userIds", userIds
        );
        jdbc.update(deleteSql, deleteParams);

        // 2단계: 새로 선택된 권한 INSERT
        String insertSql = """
            INSERT INTO TMS_CUS_MENU (
                USER_CET_CD, USER_ID, CUS_CD, INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :userId, :cusCd, SYSDATE, :insertId
            )
        """;

        for (TmsCusMenuVO vo : list) {
            Map<String, Object> param = Map.of(
                "userCetCd", vo.getUserCetCd(),
                "userId", vo.getUserId(),
                "cusCd", vo.getCusCd(),
                "insertId", vo.getInsertId()
            );
            jdbc.update(insertSql, param);
        }

    }

    


    
}
