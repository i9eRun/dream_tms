package com.dreamnalgae.tms.controller.tbas;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tbas.tbas1004.ChulpanSujumVO;
import com.dreamnalgae.tms.model.tbas.tbas1004.PublishListVO;
import com.dreamnalgae.tms.model.tbas.tbas1004.SujumVO;
import com.dreamnalgae.tms.service.tbas.Tbas1004Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas/1004")
public class Tbas1004Controller {
    private final Tbas1004Service tbas1004Service;

    @GetMapping("/publish-list")
    public List<PublishListVO> getPublishList(
        @RequestParam(name = "userCetCd") String userCetCd,
        @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return tbas1004Service.selectPublishList(userCetCd, keyword);
    }


    @GetMapping("/sujum-list")
    public List<SujumVO> getSujumList(
        @RequestParam(name = "userCetCd") String userCetCd,
        @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return tbas1004Service.selectSujumList(userCetCd, keyword);
    }


    @PostMapping("/insert")
    public ResponseEntity<?> save(@RequestBody List<ChulpanSujumVO> list) {
        tbas1004Service.insertAll(list);
        return ResponseEntity.ok("등록 완료");
    }


    @GetMapping("/chulpan-sujum-list")
    public List<ChulpanSujumVO> getChulpanSujumList(
            @RequestParam String chulpanCd,
            @RequestParam String userCetCd) {

        return tbas1004Service.selectChulpanSujumList(userCetCd, chulpanCd);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteChulpanSujum(@RequestBody List<ChulpanSujumVO> list) {
        try {
            tbas1004Service.deleteChulpanSujumList(list);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("삭제 실패: " + e.getMessage());
        }
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateChulpanSujum(@RequestBody List<ChulpanSujumVO> list) {
        try {
            tbas1004Service.updateChulpanSujumList(list);
            return ResponseEntity.ok("수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("수정 실패: " + e.getMessage());
        }
    }
    
}
