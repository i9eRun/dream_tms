package com.dreamnalgae.tms.controller.tmslabel.popup;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.service.tmslabel.popup.PopupCustService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tmslabel/popup/cust")
public class PopupCustController {
    private final PopupCustService popupCustService;

    @GetMapping("/list")
    public List<Map<String, Object>> getCustList(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "custDivGb", required = false, defaultValue = "") String custDivGb,
            @RequestParam(name = "useYn", required = false, defaultValue = "") String useYn
    ) {
        return popupCustService.selectCustList(keyword, custDivGb, useYn);
    }

    
}
