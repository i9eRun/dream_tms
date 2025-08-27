package com.dreamnalgae.tms.controller.system;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.system.CodeDTO;
import com.dreamnalgae.tms.service.system.CodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class CodeController {
    private final CodeService codeService;

    @GetMapping("/{groupCd}")
    public List<CodeDTO> getCodeList(@PathVariable String groupCd) {
        return codeService.getCodesByGroup(groupCd);
    }
    
}
