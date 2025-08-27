package com.dreamnalgae.tms.controller.tpop;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tpop.CusSearchVO;
import com.dreamnalgae.tms.service.tpop.Tpop1005Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tpop/1005")
public class Tpop1005Controller {
    private final Tpop1005Service tpop1005Service;

    @GetMapping("/cuslist")
    public List<CusSearchVO> getCusList(CusSearchVO vo) {
        return tpop1005Service.selectCusList(vo);
    }
    
}
