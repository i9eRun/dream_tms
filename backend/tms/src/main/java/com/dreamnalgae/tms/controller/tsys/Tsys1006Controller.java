package com.dreamnalgae.tms.controller.tsys;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsys.tsys1006.TmsExcelDlLogVO;
import com.dreamnalgae.tms.service.tsys.Tsys1006Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsys/1006")
public class Tsys1006Controller {
    private final Tsys1006Service tsys1006Service;

    @GetMapping("/list")
    public List<TmsExcelDlLogVO> getList(
        @RequestParam(name = "userCetCd", required = false) String userCetCd,
        @RequestParam(name = "startDate", required = false) String startDate,
        @RequestParam(name = "endDate", required = false) String endDate
    ) {
        return tsys1006Service.selectList(userCetCd, startDate, endDate);
    }


}
