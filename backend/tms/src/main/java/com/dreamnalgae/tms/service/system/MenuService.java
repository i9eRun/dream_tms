package com.dreamnalgae.tms.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.entity.system.Menu;
import com.dreamnalgae.tms.repository.system.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Menu> getMenusByUserId(String userId) {
        String sql = """
            SELECT M.MENU_ID, M.MENU_NM, M.PARENT_MENU_ID, M.MENU_LEVEL, M.EXEC_URL, M.PGM_YN
            FROM TMS_MENU_M M
            JOIN TMS_GROUP_MENU_M GM
            ON M.MENU_ID = GM.MENU_ID AND M.USER_CET_CD = GM.USER_CET_CD
            JOIN TMS_GROUP_USER_M GU
            ON GM.GROUP_CD = GU.GROUP_CD AND GM.USER_CET_CD = GU.USER_CET_CD
            WHERE GU.USER_ID = ?
            AND M.USE_YN = '1'
            ORDER BY M.MENU_ORD
        """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            Menu menu = new Menu();
            menu.setMenuId(rs.getString("MENU_ID"));
            menu.setMenuNm(rs.getString("MENU_NM"));
            menu.setParentMenuId(rs.getString("PARENT_MENU_ID"));
            menu.setMenuLevel(rs.getInt("MENU_LEVEL"));
            menu.setExecUrl(rs.getString("EXEC_URL"));
            menu.setPgmYn(rs.getString("PGM_YN"));
            return menu;
        });
    }

    public List<Menu> getMenuTree(String user_id, String user_cet_cd) {

        List<Menu> allMenus = menuRepository.findAll();

        // Map<String, Menu> uniqueMenus = new LinkedHashMap<>();
        // for (Menu menu : getMenusByUserId(user_id)) {
        //     uniqueMenus.putIfAbsent(menu.getMenuId(), menu);  // 중복 제거
        // }

        // List<Menu> allMenus = new ArrayList<>(uniqueMenus.values());


        Map<String, Menu> menuMap = new HashMap<>();
        List<Menu> roots = new ArrayList<>();

        for (Menu menu : allMenus) {
            if (!"1".equals(menu.getPgmYn())) continue;  // pgmYn이 '1'인 경우만 통과

            menuMap.put(menu.getMenuId(), menu);
            menu.setChildren(new ArrayList<>());
        }

        for (Menu menu : allMenus) {
            if (!"1".equals(menu.getPgmYn())) continue;  // pgmYn이 '1'인 경우만 통과

            if (menu.getParentMenuId() == null || menu.getParentMenuId().isEmpty()) {
                roots.add(menu);
            } else {
                Menu parent = menuMap.get(menu.getParentMenuId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    roots.add(menu);
                }
            }
        }

        return roots;
    }

    public List<Menu> getAllMenus() {
        //원래코드
        //return menuRepository.findAllByUseYn("1");


        List<Menu> allMenus = menuRepository.findAll();
        Map<String, Menu> menuMap = new HashMap<>();
        List<Menu> roots = new ArrayList<>();

        for (Menu menu : allMenus) {
            if (!"1".equals(menu.getPgmYn())) continue;  // pgmYn이 '1'인 경우만 통과
            //if (!"1".equals(menu.getUseYn())) continue;

            menuMap.put(menu.getMenuId(), menu);
            menu.setChildren(new ArrayList<>());
            menu.setChecked(false); // ✅ 체크 초기값
        }

        for (Menu menu : allMenus) {
            if (!"1".equals(menu.getPgmYn())) continue;  // pgmYn이 '1'인 경우만 통과
            //if (!"1".equals(menu.getUseYn())) continue;

            if (menu.getParentMenuId() == null || menu.getParentMenuId().isEmpty()) {
                roots.add(menu);
            } else {
                Menu parent = menuMap.get(menu.getParentMenuId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    roots.add(menu);
                }
            }
        }

        return roots;
    }

    public List<Menu> getTopMenus() {
        return menuRepository.findByMenuLevel(1).stream()
                .filter(menu -> "1".equals(menu.getUseYn()))
                .collect(Collectors.toList());
    }

    public List<Menu> getSubMenus(String parentMenuId) {
        return menuRepository.findByParentMenuId(parentMenuId).stream()
                .filter(menu -> "1".equals(menu.getUseYn()))
                .collect(Collectors.toList());
    }

    public Menu getMenuByMenuId(String menuId) {
        return menuRepository.findByMenuId(menuId);
    }
}
