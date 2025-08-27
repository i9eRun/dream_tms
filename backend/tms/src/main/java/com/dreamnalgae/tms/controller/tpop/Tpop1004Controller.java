package com.dreamnalgae.tms.controller.tpop;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tpop.CarSearchVO;
import com.dreamnalgae.tms.service.tpop.Tpop1004Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tpop")
public class Tpop1004Controller {
    private final Tpop1004Service tpop1004Service;

    @GetMapping("/car/search")
    public List<CarSearchVO> searchCarList(CarSearchVO vo) {
        return tpop1004Service.selectCarList(vo);
    }
    
}
