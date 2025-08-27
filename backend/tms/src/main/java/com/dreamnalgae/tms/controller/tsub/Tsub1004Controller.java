package com.dreamnalgae.tms.controller.tsub;

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

import com.dreamnalgae.tms.model.tsub.tsub1004.TmsBaechaVO;
import com.dreamnalgae.tms.service.tsub.Tsub1004Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsub/1004")
public class Tsub1004Controller {
    private final Tsub1004Service tsub1004Service;

    @GetMapping("/list")
    public ResponseEntity<List<TmsBaechaVO>> getList(
        @RequestParam String userCetCd,
        @RequestParam String cusDt,
        @RequestParam(required = false, defaultValue = "") String cusGb,
        @RequestParam(required = false, defaultValue = "") String cusAmpmGb
    ) {
        List<TmsBaechaVO> list = tsub1004Service.selectList(userCetCd, cusDt, cusGb, cusAmpmGb);
        return ResponseEntity.ok(list);
    }


    @PostMapping("/save")
    public ResponseEntity<?> upsert(@RequestBody TmsBaechaVO vo) {
        boolean exists = tsub1004Service.exists(vo);

        if (exists) {
            tsub1004Service.update(vo);
            return ResponseEntity.ok("수정 완료");
        } else {
            tsub1004Service.insert(vo);
            return ResponseEntity.ok("등록 완료");
        }
    }


    @PostMapping("/insert")
    public ResponseEntity<?> save(@RequestBody TmsBaechaVO vo) {
        tsub1004Service.insert(vo);
        return ResponseEntity.ok("등록 완료");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TmsBaechaVO vo) {
        tsub1004Service.update(vo);
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody TmsBaechaVO vo) {
        tsub1004Service.delete(vo);
        return ResponseEntity.ok("삭제 완료");
    }



}
