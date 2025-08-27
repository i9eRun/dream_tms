package com.dreamnalgae.tms.controller.tbas;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tbas.tbas1011.TmsSubSaveRequest;
import com.dreamnalgae.tms.model.tbas.tbas1011.TmsSubVO;
import com.dreamnalgae.tms.service.tbas.Tbas1011Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas/1011")
public class Tbas1011Controller {
    private final Tbas1011Service tbas1011Service;

    @GetMapping("/list")
    public ResponseEntity<List<TmsSubVO>> getList(
        @RequestParam String userCetCd,
        @RequestParam String cusCd
    ) {
        List<TmsSubVO> list = tbas1011Service.selectList(userCetCd, cusCd);
        return ResponseEntity.ok(list);
    }
    


    @PostMapping("/save")
    public ResponseEntity<?> saveAll(@RequestBody Map<String, List<TmsSubVO>> data) {
        tbas1011Service.saveAll(data);
        return ResponseEntity.ok().body(Map.of("success",true));
    }






}
