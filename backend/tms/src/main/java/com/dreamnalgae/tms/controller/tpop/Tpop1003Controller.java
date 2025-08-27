package com.dreamnalgae.tms.controller.tpop;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.service.tpop.Tpop1003Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tpop")
public class Tpop1003Controller {
    private final Tpop1003Service tpop1003Service;

    @GetMapping("/custlist")
    public List<Map<String, Object>> getCustList(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "custDivGb", required = false, defaultValue = "") String custDivGb,
            @RequestParam(name = "sosokNm", required = false, defaultValue = "") String sosokNm,
            @RequestParam(name = "useYn", required = false, defaultValue = "") String useYn
    ) {
        return tpop1003Service.selectCustList(keyword, custDivGb, sosokNm, useYn);
    }

    @GetMapping("/code/sosok")
    public List<Map<String, Object>> getSosokList(
        @RequestParam(name = "userCetCd", required = false, defaultValue = "") String userCetCd
    ) {
        return tpop1003Service.selectSosokList();

    }
    
}
