package com.dreamnalgae.tms.controller.tsys;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsys.TmsGroupMVO;
import com.dreamnalgae.tms.model.tsys.TmsGroupUserMVO;
import com.dreamnalgae.tms.service.tsys.Tsys1003Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsys")
public class Tsys1003Controller {
    private final Tsys1003Service tsys1003Service;

    @GetMapping("/group/grouplist")
    public List<TmsGroupMVO> selectGroup(
        @RequestParam(name = "userCetCd", required = true) String userCetCd,
        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword
    ) {
       return tsys1003Service.selectGroup(userCetCd, keyword);
    }

    @GetMapping("/group/{groupCd}")
    public ResponseEntity<List<String>> getCheckedMenus(
            @RequestParam(name = "groupCd", required = true) String groupCd,
            @RequestParam(name = "userCetCd", required = true) String userCetCd) {

        List<String> menuIds = tsys1003Service.findMenuIdsByGroup(userCetCd, groupCd);
        return ResponseEntity.ok(menuIds);
    }

    @PostMapping("/group/insert/{groupCd}")
    public ResponseEntity<Void> saveGroupMenus(
            @PathVariable String groupCd,
            @RequestParam String userCetCd,
            @RequestParam String userId,
            @RequestBody List<String> menuIds // 프론트에서 jsonData로 넘기는 menuId 배열
    ) {
        tsys1003Service.saveGroupMenus(userCetCd, groupCd, userId, menuIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/group/groupuserlist")
    public List<Map<String, Object>> getGroupUserList(
        @RequestParam(name = "groupCd", required = true) String groupCd,
        @RequestParam(name = "userCetCd", required = true) String userCetCd) {
        
        return tsys1003Service.selectGroupUserList(groupCd, userCetCd);

    }


    @PostMapping("/group/insertuser")
    public ResponseEntity<String> insertGroupUser(@RequestBody TmsGroupUserMVO vo) {
        try {
            tsys1003Service.insertGroupUser(vo);
            return ResponseEntity.ok("사용자 추가 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 추가 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/group/deleteuser")
    public ResponseEntity<String> deleteGroupUser(@RequestBody TmsGroupUserMVO vo) {
        try {
            tsys1003Service.deleteGroupUser(vo);
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping("/group/insertgroup")
    public ResponseEntity<String> insertGroup(@RequestBody TmsGroupMVO vo) {
        boolean result = tsys1003Service.insertGroup(vo);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }


    @PutMapping("/group/updategroup")
    public ResponseEntity<String> updateGroup(@RequestBody TmsGroupMVO vo) {
        boolean result = tsys1003Service.updateGroup(vo);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }

    @DeleteMapping("/group/deletegroup")
    public ResponseEntity<String> deleteGroup(@RequestBody TmsGroupMVO vo) {
        boolean result = tsys1003Service.deleteGroup(vo);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }


}
