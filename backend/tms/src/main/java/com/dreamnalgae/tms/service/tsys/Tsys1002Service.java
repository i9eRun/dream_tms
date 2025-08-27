package com.dreamnalgae.tms.service.tsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.dreamnalgae.tms.model.tsys.TmsMenuVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsys1002Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsMenuVO> selectAllMenu(String userCetCd) {
        String sql = """
                SELECT * FROM TMS_MENU_M
                WHERE USER_CET_CD = :userCetCd
                AND PGM_YN = '1'
                """;
    
        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(TmsMenuVO.class));
    }

    public List<TmsMenuVO> makeMenuTree(String userCetCd) {
        List<TmsMenuVO> allMenus = selectAllMenu(userCetCd);
        Map<String, TmsMenuVO> menuMap = new HashMap<>();
        List<TmsMenuVO> roots = new ArrayList<>();

        for (TmsMenuVO menu : allMenus) {
            menuMap.put(menu.getMenuId(), menu);
            menu.setChildren(new ArrayList<>());
        }

        for (TmsMenuVO menu : allMenus) {
            if (menu.getParentMenuId() == null || menu.getParentMenuId().isEmpty()) {
                roots.add(menu);
            } else {
                TmsMenuVO parent = menuMap.get(menu.getParentMenuId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    roots.add(menu);
                }
            }
        }

        return roots;
    }

    
    // 메뉴 등록
    @Transactional
    public boolean insertMenu(TmsMenuVO vo) {

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(vo);

        String checkSql = "SELECT COUNT(*) FROM TMS_MENU_M WHERE USER_CET_CD = :userCetCd AND MENU_ID = :menuId";
        Integer  countObj = jdbc.queryForObject(checkSql, params, Integer.class);
        int count = (countObj != null) ? countObj : 0;

        if (count > 0) {
            throw new DuplicateKeyException("이미 존재하는 메뉴 ID 입니다.");
        }

        String sql = """
                INSERT INTO TMS_MENU_M (
                    USER_CET_CD,
                    MENU_SEQ,
                    MENU_ID,
                    MENU_NM,
                    MENU_LEVEL,
                    PARENT_MENU_ID,
                    PGM_YN,
                    USE_YN,
                    MENU_ORD,
                    INSERT_DT,
                    INSERT_ID )
                VALUES (
                    :userCetCd,
                    (SELECT NVL(MAX(MENU_SEQ), 0)+1 FROM TMS_MENU_M),
                    :menuId,
                    :menuNm,
                    :menuLevel,
                    :parentMenuId,
                    :pgmYn,
                    :useYn,
                    (SELECT NVL(MAX(MENU_ORD), 0)+1 FROM TMS_MENU_M),
                    SYSDATE,
                    :insertId )
                """;

        return jdbc.update(sql, params) > 0;
    }
    
    @Transactional
    public boolean deleteMenu(String menuId, String userCetCd) {
        // 1. 하위 메뉴 존재 여부 확인
        String checkSql = """
            SELECT COUNT(*) FROM TMS_MENU_M
            WHERE PARENT_MENU_ID = :menuId AND USER_CET_CD = :userCetCd
        """;

        Map<String, Object> params = Map.of(
            "menuId", menuId,
            "userCetCd", userCetCd
        );

        int childCount = Optional.ofNullable(jdbc.queryForObject(checkSql, params, Integer.class)).orElse(0);

        if (childCount > 0) {
            throw new IllegalStateException("하위 메뉴가 존재하여 삭제할 수 없습니다.");
        }

           String deleteSql = """
                DELETE FROM TMS_MENU_M
                WHERE MENU_ID = :menuId AND USER_CET_CD = :userCetCd
            """;

            return jdbc.update(deleteSql, params) > 0;
    }




}
