package com.dreamnalgae.tms.controller.tsub;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsub.tsub1005.TmsIlSubVO;
import com.dreamnalgae.tms.model.tsub.tsub1005.TransferVO;
import com.dreamnalgae.tms.service.tsub.Tsub1005Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsub/1005")
@Slf4j
public class Tsub1005Controller {
    private final Tsub1005Service tsub1005Service;

    @GetMapping("/list")
    public ResponseEntity<List<TmsIlSubVO>> getList(
        @RequestParam String userCetCd,
        @RequestParam String cusDt,
        @RequestParam(required = false, defaultValue = "") String cusGb,
        @RequestParam(required = false, defaultValue = "") String cusAmpmGb,
        @RequestParam(required = false, defaultValue = "") String cusCd
    ) {
        List<TmsIlSubVO> list = tsub1005Service.selectList(userCetCd, cusDt, cusGb, cusAmpmGb, cusCd);
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/item-list")
    public Map<String, Object> getItemList(
            @RequestParam String userCetCd,
            @RequestParam String cusDt,
            @RequestParam String cusCd
    ) {
        List<Map<String, Object>> resultList = tsub1005Service.selectItemList(userCetCd, cusDt, cusCd);

        return Map.of(
            "success", true,
            "data", resultList
        );
    }



    @PostMapping("/transfer")
    public ResponseEntity<?> transferItems(@RequestBody List<TransferVO> list) {
        if (list == null || list.isEmpty()) {
            return ResponseEntity.badRequest().body("이동할 데이터가 없습니다.");
        }

        for (TransferVO vo : list) {
            if (!StringUtils.hasText(vo.getOldCusCd()) ||
                !StringUtils.hasText(vo.getNewCusCd()) ||
                !StringUtils.hasText(vo.getSujumCd()) ||
                !StringUtils.hasText(vo.getOldDt()) ||
                !StringUtils.hasText(vo.getNewDt())) {
                return ResponseEntity.badRequest().body("필수값이 누락되었습니다.");
            }

            if (vo.getOldCusCd().equals(vo.getNewCusCd()) && vo.getOldDt().equals(vo.getNewDt())) {
                return ResponseEntity.badRequest().body("이전과 동일한 코스 및 날짜로는 이동할 수 없습니다.");
            }
        }

        try {
            tsub1005Service.transferItems(list);
            return ResponseEntity.ok("이동 완료");
        } catch (Exception e) {
            log.error("이동 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("이동 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
