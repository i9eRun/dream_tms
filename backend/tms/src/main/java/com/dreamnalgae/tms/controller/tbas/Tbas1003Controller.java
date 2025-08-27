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

import com.dreamnalgae.tms.model.tbas.tbas1003.CetListVO;
import com.dreamnalgae.tms.model.tbas.tbas1003.CetPublishVO;
import com.dreamnalgae.tms.model.tbas.tbas1003.PublishListVO;
import com.dreamnalgae.tms.service.tbas.Tbas1003Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas/1003")
public class Tbas1003Controller {
    private final Tbas1003Service tbas1003Service;

    @GetMapping("/cet-list")
    public List<CetListVO> getCetList(
        @RequestParam(name = "userCetCd") String userCetCd,
        @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return tbas1003Service.selectCetList(userCetCd, keyword);
    }


    @GetMapping("/publish-list")
    public List<PublishListVO> getSujumList(
        @RequestParam(name = "userCetCd") String userCetCd,
        @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return tbas1003Service.selectPublishList(userCetCd, keyword);
    }


    @PostMapping("/insert")
    public ResponseEntity<?> save(@RequestBody List<CetPublishVO> list) {
        tbas1003Service.insertAll(list);
        return ResponseEntity.ok("등록 완료");
    }


    @GetMapping("/cet-publish-list")
    public List<CetPublishVO> getCetPublishList(
            @RequestParam String cetCd,
            @RequestParam String userCetCd) {

        return tbas1003Service.selectCetPublishList(userCetCd, cetCd);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteCetPublish(@RequestBody List<CetPublishVO> list) {
        try {
            tbas1003Service.deleteCetPublishList(list);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("삭제 실패: " + e.getMessage());
        }
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateChulpanSujum(@RequestBody List<CetPublishVO> list) {
        try {
            tbas1003Service.updateCetPublishList(list);
            return ResponseEntity.ok("수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("수정 실패: " + e.getMessage());
        }
    }
    
}
