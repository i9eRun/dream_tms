package com.dreamnalgae.tms.controller.tbas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsys.TmsCusVO;
import com.dreamnalgae.tms.service.tbas.Tbas1005Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas")
public class Tbas1005Controller {
    private final Tbas1005Service tbas1005Service;

    @GetMapping("/courselist")
    public List<TmsCusVO> getCourseList(
        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
        @RequestParam(name = "userCetCd", required = true) String userCetCd ) {        

        return tbas1005Service.selectCourseList(userCetCd, keyword);
        
    }

    @PostMapping("/1005/courseinsert")
    public ResponseEntity<String> insertCus(@RequestBody TmsCusVO vo) {
        boolean result = tbas1005Service.insertCus(vo);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }
    
    @PutMapping("/1005/courseupdate")
    public ResponseEntity<String> updateCus(@RequestBody TmsCusVO vo) {
        boolean result = tbas1005Service.updateCus(vo);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }

    @DeleteMapping("/1005/coursedelete")
    public ResponseEntity<String> deleteCus(@RequestParam String userCetCd, @RequestParam String cusCd) {
        boolean result = tbas1005Service.deleteCus(userCetCd, cusCd);
        return result ? ResponseEntity.ok("SUCCESS") : ResponseEntity.status(500).body("FAIL");
    }
}
