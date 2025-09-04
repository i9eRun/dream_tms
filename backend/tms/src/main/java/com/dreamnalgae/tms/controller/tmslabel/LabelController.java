package com.dreamnalgae.tms.controller.tmslabel;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.service.tmslabel.LabelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tmslabel")
public class LabelController {
    private final LabelService labelService;

    @GetMapping("/agency-list")
    public List<Map<String, Object>> getAgency(@RequestParam String userCetCd) {
        return labelService.getAgency(userCetCd);
    }
    
}
