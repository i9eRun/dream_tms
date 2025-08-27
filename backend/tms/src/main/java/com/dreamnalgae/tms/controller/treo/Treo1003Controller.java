package com.dreamnalgae.tms.controller.treo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.treo.treo1003.OrderRetnMstVO;
import com.dreamnalgae.tms.service.treo.Treo1003Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treo/1003")
public class Treo1003Controller {
    private final Treo1003Service treo1003Service;

    @GetMapping("/list")
    public ResponseEntity<List<OrderRetnMstVO>> getReorderList(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String chulpanCd,
        @RequestParam(required = false) String sujumCd,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        List<OrderRetnMstVO> list = treo1003Service.selectList(userCetCd, chulpanCd, sujumCd, startDate, endDate);
        return ResponseEntity.ok(list);
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insertOrderRetn(@RequestBody OrderRetnMstVO vo) {
        try {
            // 회송번호 채번
            String ordRetnNo = treo1003Service.generateOrdRetnNo(vo.getInsertId());
            vo.setOrdRetnNo(ordRetnNo);

            treo1003Service.insertOrderRetn(vo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("ordRetnNo", ordRetnNo));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody OrderRetnMstVO vo) {
        try {
            treo1003Service.update(vo);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Map<String, String> req) {
        try {
            String ordRetnNo = req.get("ordRetnNo");
            String userCetCd = req.get("userCetCd");
            treo1003Service.delete(ordRetnNo, userCetCd);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }


    
}
