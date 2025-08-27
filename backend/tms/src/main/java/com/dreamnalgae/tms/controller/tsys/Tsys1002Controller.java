package com.dreamnalgae.tms.controller.tsys;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsys.TmsMenuVO;
import com.dreamnalgae.tms.service.tsys.Tsys1002Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsys")
public class Tsys1002Controller {
    private final Tsys1002Service tsys1002Service;

    @GetMapping("/getmenutree")
    public List<TmsMenuVO> getAllMenu(@RequestParam(required = true) String userCetCd) {
        
        return tsys1002Service.makeMenuTree(userCetCd);
    }

    @PostMapping("/menuadd")
    public ResponseEntity<String> addMenu(@RequestBody TmsMenuVO vo) {
        boolean result = tsys1002Service.insertMenu(vo);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }

    @PostMapping("/menudelete")
    public ResponseEntity<String> deleteMenu(@RequestBody TmsMenuVO vo) {
        String menuId = vo.getMenuId();
        String userCetCd = vo.getUserCetCd();

        if (menuId == null || userCetCd == null) {
            return ResponseEntity.badRequest().body( "menuId 또는 userCetCd가 누락되었습니다.");
        }

        boolean result = tsys1002Service.deleteMenu(menuId, userCetCd);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }

}
