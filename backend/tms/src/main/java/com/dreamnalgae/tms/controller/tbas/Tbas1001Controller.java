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

import com.dreamnalgae.tms.model.tbas.TmsCustInfoVO;
import com.dreamnalgae.tms.model.tbas.TmsCustVO;
import com.dreamnalgae.tms.service.tbas.Tbas1001Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas/1001/")
public class Tbas1001Controller {
    private final Tbas1001Service tbas1001Service;

    @GetMapping("/custlist")
    public List<TmsCustInfoVO> getCustList(TmsCustInfoVO vo) {
        return tbas1001Service.selectCustList(vo);
    }

    @PostMapping("/custinsert")
    public ResponseEntity<?> insertCustomer(@RequestBody TmsCustVO vo) {
        boolean result = tbas1001Service.insertCust(vo);
        return ResponseEntity.ok(result ? "등록 성공" : "등록 실패");
    }
    
    @PutMapping("/custupdate")
    public ResponseEntity<?> updateCustomer(@RequestBody TmsCustVO vo) {
        boolean result = tbas1001Service.updateCust(vo);
        return ResponseEntity.ok(result ? "수정 성공" : "수정 실패");
    }

    @DeleteMapping("/custdelete")
    public ResponseEntity<?> deleteCustomer(@RequestParam(name = "userCetCd", required = true) String userCetCd,
                                            @RequestParam(name = "custCd", required = true) String custCd) {
        boolean result = tbas1001Service.deleteCust(userCetCd, custCd);
        return ResponseEntity.ok(result ? "삭제 성공" : "삭제 실패");
    }
}
