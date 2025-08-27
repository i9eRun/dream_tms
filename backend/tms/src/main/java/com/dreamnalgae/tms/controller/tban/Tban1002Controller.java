package com.dreamnalgae.tms.controller.tban;

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

import com.dreamnalgae.tms.model.tban.tban1002.TmsBanpIbgoMstVO;
import com.dreamnalgae.tms.service.tban.Tban1002Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tban/1002")
public class Tban1002Controller {
    private final Tban1002Service tban1002Service;

    @GetMapping("/list")
    public List<TmsBanpIbgoMstVO> getOrders(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String chulpanCd,
        @RequestParam(required = false) String sujumCd,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        return tban1002Service.selectList(userCetCd, chulpanCd, sujumCd, startDate, endDate);
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody TmsBanpIbgoMstVO vo) {
        try {
            String banpIbgoNo = tban1002Service.generateBanpIbgoNo(vo.getInsertId());
            vo.setBanpIbgoNo(banpIbgoNo);

            tban1002Service.insertBanpIbgo(vo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("banpIbgoNo", banpIbgoNo));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TmsBanpIbgoMstVO vo) {
        try {
            tban1002Service.updateBanpIbgo(vo);
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
            String banpIbgoNo = req.get("banpIbgoNo");
            String userCetCd = req.get("userCetCd");
            tban1002Service.deleteBanpIbgo(banpIbgoNo, userCetCd);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    



    
}
