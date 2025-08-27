package com.dreamnalgae.tms.controller.tbas;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tbas.TmsDeptVO;
import com.dreamnalgae.tms.service.tbas.Tbas1008Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas")
public class Tbas1008Controller {

    private final Tbas1008Service tbas1008Service;
    
    @GetMapping("/deptcodelist")
    public List<Map<String, Object>> searchUsers(
        @RequestParam(name = "search_keyword", required = false) String searchKeyword,
        @RequestParam(name = "use_yn", required = false) String useYn
    ) {
        return tbas1008Service.selectMain(searchKeyword, useYn);
    }
    
    
    @PostMapping("/dept/insert")
    public ResponseEntity<?> create(@RequestBody TmsDeptVO vo) {
        boolean result = tbas1008Service.insert(vo);
        return result ? ResponseEntity.ok("Inserted") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insert failed");
    }

    @PostMapping("/dept/update")
    public ResponseEntity<?> updateDept(@RequestBody TmsDeptVO dto) {
        int result = tbas1008Service.update(dto);
        return result > 0
            ? ResponseEntity.ok("수정 성공")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정 실패");
    }

    @PostMapping("/dept/delete")
    public ResponseEntity<?> deleteDept(@RequestBody Map<String, Object> param) {
        String nDeptCd = (String) param.get("nDeptCd");
        String userCetcd = "00001";
        int result = tbas1008Service.delete(userCetcd, nDeptCd);
        return result > 0
            ? ResponseEntity.ok("삭제 성공")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 실패");
    }





}
