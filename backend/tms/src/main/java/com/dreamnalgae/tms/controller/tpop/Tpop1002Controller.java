package com.dreamnalgae.tms.controller.tpop;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.service.tpop.Tpop1002Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tpop")
public class Tpop1002Controller {
    private final Tpop1002Service tpop1002Service;

    @GetMapping("/deptlist")
    public List<Map<String, Object>> getDeptList(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "useYn", required = false, defaultValue = "") String useYn
    ) {
        return tpop1002Service.selectTable(keyword, useYn);
    }
    
}
