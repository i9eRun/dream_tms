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
import com.dreamnalgae.tms.model.treo.treo1004.TmsJaechulgoVO;
import com.dreamnalgae.tms.service.treo.Treo1004Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treo/1004")
public class Treo1004Controller {
    private final Treo1004Service treo1004Service;

    @GetMapping("/list")
    public List<TmsJaechulgoVO> getList(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String chulpanCd,
        @RequestParam(required = false) String sujumCd,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        return treo1004Service.selectList(userCetCd, chulpanCd, sujumCd, startDate, endDate);
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody TmsJaechulgoVO vo) {
        try {
            // 재출고번호 채번
            String jaechulgoNo = treo1004Service.generateNo(vo.getInsertId());
            vo.setJaechulgoNo(jaechulgoNo);

            treo1004Service.insert(vo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("jaechulgoNo", jaechulgoNo));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }



    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TmsJaechulgoVO vo) {
        try {
            treo1004Service.update(vo);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }



    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Map<String, String> request) {
        try {
            String jaechulgoNo = request.get("jaechulgoNo");
            String userCetCd = request.get("userCetCd");

            treo1004Service.delete(userCetCd, jaechulgoNo);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }


    
}
