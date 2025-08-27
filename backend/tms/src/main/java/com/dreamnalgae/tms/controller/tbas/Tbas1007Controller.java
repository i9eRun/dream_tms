package com.dreamnalgae.tms.controller.tbas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tbas.tbas1007.HolidayVO;
import com.dreamnalgae.tms.service.tbas.Tbas1007Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tbas/1007")
public class Tbas1007Controller {
    private final Tbas1007Service tbas1007Service;

    @GetMapping("/list")
    public ResponseEntity<List<HolidayVO>> getHolidayList(HolidayVO vo) {
        List<HolidayVO> list = tbas1007Service.selectMain(vo);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveHoliday(@RequestBody HolidayVO vo) {
        tbas1007Service.insertHoliday(vo);
        return ResponseEntity.ok("저장되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteHoliday(@RequestBody HolidayVO vo) {
        tbas1007Service.deleteHoliday(vo);
        return ResponseEntity.ok("삭제되었습니다.");
    }




    
}
