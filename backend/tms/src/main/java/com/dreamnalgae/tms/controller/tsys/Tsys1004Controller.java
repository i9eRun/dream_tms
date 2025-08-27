package com.dreamnalgae.tms.controller.tsys;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsys.TmsCusMenuVO;
import com.dreamnalgae.tms.model.tsys.TmsCusUserVO;
import com.dreamnalgae.tms.model.tsys.TmsCusVO;
import com.dreamnalgae.tms.service.tsys.Tsys1004Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsys")
public class Tsys1004Controller {
    private final Tsys1004Service tsys1004Service;

    @GetMapping("/course/list")
    public List<TmsCusVO> getCourseList(
         @RequestParam(name = "userCetCd", required = true) String userCetCd
    ) {
        return tsys1004Service.selectCourseList(userCetCd);
    }

    @GetMapping("/course/userlist")
    public List<TmsCusUserVO> getUserList(
         @RequestParam(name = "userCetCd", required = true) String userCetCd,
         @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword
    ) {
        return tsys1004Service.selectUserList(userCetCd, keyword);
    }

    @GetMapping("/course/usercuslist")
    public List<String> getUserCusList(
         @RequestParam(name = "userCetCd", required = true) String userCetCd,
         @RequestParam(name = "userId", required = true) String userId
    ) {
        return tsys1004Service.selectUserCusList(userCetCd, userId);
    }

    @PostMapping("/cusmenu/insert")
    public ResponseEntity<String> insertCusMenus(@RequestBody List<TmsCusMenuVO> list) {
        tsys1004Service.insertCusMenus(list);
        return ResponseEntity.ok("저장 성공");
    }
    
}
