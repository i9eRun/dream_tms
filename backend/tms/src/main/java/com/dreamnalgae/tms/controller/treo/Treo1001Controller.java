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

import com.dreamnalgae.tms.model.treo.treo1001.TmsReorderMstVO;
import com.dreamnalgae.tms.service.treo.Treo1001Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treo/1001")
public class Treo1001Controller {
    private final Treo1001Service treo1001Service;

    @GetMapping("/list")
    public ResponseEntity<List<TmsReorderMstVO>> getReorderList(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String chulpanCd,
        @RequestParam(required = false) String sujumCd,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        List<TmsReorderMstVO> list = treo1001Service.selectReorderList(userCetCd, chulpanCd, sujumCd, startDate, endDate);
        return ResponseEntity.ok(list);
    }



    @PostMapping("/insert")
    public ResponseEntity<?> insertReorder(@RequestBody TmsReorderMstVO vo) {
        try {
            // 사고번호 채번
            String reordNo = treo1001Service.generateReordNo(vo.getInsertId());
            vo.setReordNo(reordNo);

            treo1001Service.insertReorder(vo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("reordNo", reordNo));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateReorder(@RequestBody TmsReorderMstVO vo) {
        try {
            treo1001Service.updateReorder(vo);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReorder(@RequestBody Map<String, String> request) {
        try {
            String reordNo = request.get("reordNo");
            String userCetCd = request.get("userCetCd");

            treo1001Service.deleteReorder(reordNo, userCetCd);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }





    
}
