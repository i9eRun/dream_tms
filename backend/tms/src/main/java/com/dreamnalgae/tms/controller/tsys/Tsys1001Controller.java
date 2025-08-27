package com.dreamnalgae.tms.controller.tsys;

import java.util.List;

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

import com.dreamnalgae.tms.model.tsys.TmsComUserVO;
import com.dreamnalgae.tms.service.tsys.Tsys1001Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsys")
public class Tsys1001Controller {
    private final Tsys1001Service tsys1001Service;

    @GetMapping("/user")
    public List<TmsComUserVO> searchUsers(
        @RequestParam(name = "search_keyword", required = false) String searchKeyword,
        @RequestParam(name = "user_chk_gb", required = false) String userChkGb,
        @RequestParam(name = "cbo_use_yn_sch", required = false) String useYn,
        @RequestParam(name = "cbo_geunmoo_jo_sch", required = false) String geunmooJo
    ) {
        return tsys1001Service.selectMain(searchKeyword, userChkGb, useYn, geunmooJo);
    }

    @PostMapping("/user/insert")
    public ResponseEntity<String> insertUser(@RequestBody TmsComUserVO user) {
        boolean result = tsys1001Service.insertUser(user);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }
    
    @PutMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody TmsComUserVO user) {
        return tsys1001Service.updateUser(user)
            ? ResponseEntity.ok("UPDATED")
            : ResponseEntity.status(500).body("UPDATE FAILED");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        return tsys1001Service.deleteUser(userId)
            ? ResponseEntity.ok("DELETED")
            : ResponseEntity.status(500).body("DELETE FAILED");
    }


}
