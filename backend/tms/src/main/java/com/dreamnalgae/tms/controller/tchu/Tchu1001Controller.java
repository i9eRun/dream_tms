package com.dreamnalgae.tms.controller.tchu;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeDeleteReq;
import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeMstVO;
import com.dreamnalgae.tms.service.tchu.Tchu1001Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tchu/1001")
public class Tchu1001Controller {
    private final Tchu1001Service tchu1001Service;

    @GetMapping("/list")
    public List<TmsDungeMstVO> getList(
        @RequestParam(name = "chooseDate", required = true) String dungeDt,
        @RequestParam(name = "userCetCd", required = true) String userCetCd,
        @RequestParam(name = "chulgoGb", required = false) String chulgoGb,
        @RequestParam(name = "chulpanCd", required = false) String chulpanCd,
        @RequestParam(name = "cetCd", required = false) String cetCd
    ) {
        return tchu1001Service.selectList(dungeDt, userCetCd, chulgoGb, chulpanCd, cetCd);
    }




    
    
}
