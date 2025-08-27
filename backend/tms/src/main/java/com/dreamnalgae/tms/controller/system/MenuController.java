package com.dreamnalgae.tms.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.entity.system.Menu;
import com.dreamnalgae.tms.service.system.MenuService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/checkss")
    public Map<String, Object> checkSession(HttpSession session) {
        Object userCetCd = session.getAttribute("userCetCd");

        if (userCetCd == null) {
            return Map.of("loggedIn", false);
        }

        return Map.of(
            "loggedIn", true,
            "userCetCd", userCetCd
        );
    }

    @PostMapping("/menu/tree")
    public List<Menu> getMenuTree(@RequestBody Map<String, String> request) {
        String user_id = request.get("user_id");
        String user_cet_cd = request.get("user_cet_cd");
        return menuService.getMenuTree(user_id, user_cet_cd);
    }

    @GetMapping("/menu/tab")
    public List<Menu> getTopMenus() {
        return menuService.getTopMenus();
    }

    @GetMapping("/menu/sub/{parentMenuId}")
    public List<Menu> getSubMenus(@PathVariable("parentMenuId") String parentMenuId) {
        return menuService.getSubMenus(parentMenuId);
    }

    @GetMapping("/menu/{menuId}")
    public Map<String, String> getXtypeByMenuId(@PathVariable("menuId") String menuId) {
        Menu menu = menuService.getMenuByMenuId(menuId);
        Map<String, String> result = new HashMap<>();
        result.put("menu_seq", menu.getMenuId());
        result.put("menu_name", menu.getMenuNm());
        return result;
    }

    @GetMapping("/menu/all")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }
    
}
