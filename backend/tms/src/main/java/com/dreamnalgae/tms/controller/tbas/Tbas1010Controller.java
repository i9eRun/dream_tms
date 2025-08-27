package com.dreamnalgae.tms.controller.tbas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tbas.tbas1010.DochakChulpanVO;
import com.dreamnalgae.tms.model.tbas.tbas1010.DochakVO;
import com.dreamnalgae.tms.service.tbas.Tbas1010Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas/1010")
public class Tbas1010Controller {
    private final Tbas1010Service tbas1010Service;

    @GetMapping("/list")
    public List<DochakVO> getList(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String keyword
    ) {
        return tbas1010Service.selectList(userCetCd, type, keyword);
    }

    
    @GetMapping("/chulpan-list")
    public List<DochakChulpanVO> getChulpanList(
        @RequestParam String userCetCd,
        @RequestParam String sujumCd,
        @RequestParam String chulpanGb
    ) {
        return tbas1010Service.selectChulpanList(userCetCd, sujumCd, chulpanGb);
    }


    @PostMapping("/chulpan-insert")
    public ResponseEntity<?> chulpanInsert(@RequestBody DochakChulpanVO vo) {
    if (!StringUtils.hasText(vo.getUserCetCd()) ||
        !StringUtils.hasText(vo.getSujumCd()) ||
        !StringUtils.hasText(vo.getChulpanCd()) ||
        !StringUtils.hasText(vo.getInsertId())) {

        return ResponseEntity
                .badRequest()
                .body("필수 값(USER_CET_CD, SUJUM_CD, CHULPAN_CD)이 누락되었습니다.");
    }

        boolean result = tbas1010Service.chulpanInsert(vo);
        return ResponseEntity.ok(result ? "등록 성공" : "등록 실패");
    }



    @DeleteMapping("/chulpan-delete")
    public ResponseEntity<?> chulpanDelete(@RequestBody DochakChulpanVO vo) {
        if (!StringUtils.hasText(vo.getUserCetCd()) ||
            !StringUtils.hasText(vo.getSujumCd()) ||
            !StringUtils.hasText(vo.getChulpanCd())) {

            return ResponseEntity
                    .badRequest()
                    .body("필수 값(USER_CET_CD, SUJUM_CD, CHULPAN_CD)이 누락되었습니다.");
        }

        boolean result = tbas1010Service.chulpanDelete(vo);
        return ResponseEntity.ok(result ? "삭제 성공" : "삭제 실패");
    }




    @PostMapping("/chulpan-insert2")
    public ResponseEntity<?> chulpanInsert2(@RequestBody DochakChulpanVO vo) {
    if (!StringUtils.hasText(vo.getUserCetCd()) ||
        !StringUtils.hasText(vo.getSujumCd()) ||
        !StringUtils.hasText(vo.getChulpanCd()) ||
        !StringUtils.hasText(vo.getInsertId()) ||
        !StringUtils.hasText(vo.getChulpanBasicGb())
        ) {

        return ResponseEntity
                .badRequest()
                .body("필수 값(USER_CET_CD, SUJUM_CD, CHULPAN_CD, CHULPAN_BASIC_GB)이 누락되었습니다.");
    }

        boolean result = tbas1010Service.chulpanInsert2(vo);
        return ResponseEntity.ok(result ? "등록 성공" : "등록 실패");
    }



    @DeleteMapping("/chulpan-delete2")
    public ResponseEntity<?> chulpanDelete2(@RequestBody DochakChulpanVO vo) {
        if (!StringUtils.hasText(vo.getUserCetCd()) ||
            !StringUtils.hasText(vo.getSujumCd()) ||
            !StringUtils.hasText(vo.getChulpanCd())) {

            return ResponseEntity
                    .badRequest()
                    .body("필수 값(USER_CET_CD, SUJUM_CD, CHULPAN_CD)이 누락되었습니다.");
        }

        boolean result = tbas1010Service.chulpanDelete2(vo);
        return ResponseEntity.ok(result ? "삭제 성공" : "삭제 실패");
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody DochakVO vo) {
    if (!StringUtils.hasText(vo.getUserCetCd()) ||
        !StringUtils.hasText(vo.getSujumCd()) ||
        !StringUtils.hasText(vo.getUpdateId())
        ) {

        return ResponseEntity
                .badRequest()
                .body("필수 값(USER_CET_CD, SUJUM_CD, UPDATE_ID)이 누락되었습니다.");
    }

        boolean result = tbas1010Service.save(vo);
        return ResponseEntity.ok(result ? "저장 성공" : "저장 실패");
    }



}
