package com.dreamnalgae.tms.controller.tord;

import java.time.LocalDateTime;
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

import com.dreamnalgae.tms.model.tord.OrderVO;
import com.dreamnalgae.tms.service.tord.Tord1002Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tord/1002")
public class Tord1002Controller {
    private final Tord1002Service tord1002Service;

    /**
     * 주문 목록을 조회합니다.
     * 
     * @param frDt       시작일자 (YYYYMMDD)
     * @param toDt       종료일자 (YYYYMMDD)
     * @param chulpanCd  출판사 코드 (선택)
     * @param sujumCd    서점 코드 (선택)
     * @return 주문 목록 JSON 배열
     */
    @GetMapping("/order-list")
    public List<OrderVO> getOrders(
            @RequestParam String frDt,
            @RequestParam String toDt,
            @RequestParam(required = false) String chulpanCd,
            @RequestParam(required = false) String sujumCd
    ) {
        return tord1002Service.selectMain(frDt, toDt, chulpanCd, sujumCd);
    }


    @PostMapping("/insert")
    public ResponseEntity<String> insertOrder(@RequestBody OrderVO dto) {
        if (dto.getOrdNo() != null && !dto.getOrdNo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("이미 주문번호가 존재합니다. 등록할 수 없습니다.");
        }

        tord1002Service.insertOrder(dto);
        return ResponseEntity.ok("등록 완료");
    }


    @PutMapping("/update")
    public ResponseEntity<String> updateOrder(@RequestBody OrderVO dto) {
        if (dto.getOrdNo() == null || dto.getOrdNo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("주문번호가 없습니다. 수정할 수 없습니다.");
        }

        try {
            dto.setUpdateDt(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            tord1002Service.updateOrder(dto);
            return ResponseEntity.ok("수정 완료");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("수정 실패: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOrder(
            @RequestParam String userCetCd,
            @RequestParam String ordNo
    ) {
        if (ordNo == null || ordNo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("주문번호가 없습니다. 삭제할 수 없습니다.");
        }

        try {
            tord1002Service.deleteOrder(userCetCd, ordNo);
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("삭제 실패: " + e.getMessage());
        }
    }
    



    
}
