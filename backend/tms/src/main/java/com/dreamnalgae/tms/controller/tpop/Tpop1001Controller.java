package com.dreamnalgae.tms.controller.tpop;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tpop.UserSearchVO;
import com.dreamnalgae.tms.service.tpop.Tpop1001Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tpop")
public class Tpop1001Controller {

    private final Tpop1001Service tpop1001Service;

    @GetMapping("/user/search")
    public List<UserSearchVO> searchUsers(
        @RequestParam(name = "search_keyword", required = false) String searchKeyword,
        @RequestParam(name = "user_cet_cd", required = false) String userCetCd,
        @RequestParam(name = "cbo_use_yn_sch", required = false) String useYn
    ) {
        userCetCd = "00001";
        return tpop1001Service.searchUsers(searchKeyword,userCetCd,useYn);
    }
    
}
