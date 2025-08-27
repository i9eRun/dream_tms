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

import com.dreamnalgae.tms.model.treo.treo1005.TmsBanpRetnMstVO;
import com.dreamnalgae.tms.service.treo.Treo1005Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treo/1005")
public class Treo1005Controller {
    private final Treo1005Service treo1005Service;

    @GetMapping("/list")
    public ResponseEntity<List<TmsBanpRetnMstVO>> getReorderList(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String chulpanCd,
        @RequestParam(required = false) String sujumCd,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        List<TmsBanpRetnMstVO> list = treo1005Service.selectList(userCetCd, chulpanCd, sujumCd, startDate, endDate);
        return ResponseEntity.ok(list);
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody TmsBanpRetnMstVO vo) {
        try {
            String banpRetnNo = treo1005Service.generateBanpRetnNo(vo.getInsertId());
            vo.setBanpRetnNo(banpRetnNo);

            treo1005Service.insertBanpRetn(vo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("banpRetnNo", banpRetnNo));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TmsBanpRetnMstVO vo) {
        try {
            treo1005Service.updateBanpRetn(vo);
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
            String banpRetnNo = req.get("banpRetnNo");
            String userCetCd = req.get("userCetCd");
            treo1005Service.deleteBanpRetn(banpRetnNo, userCetCd);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    
}
