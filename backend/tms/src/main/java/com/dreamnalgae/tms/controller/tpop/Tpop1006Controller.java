package com.dreamnalgae.tms.controller.tpop;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tpop.tpop1006.ChulpanSujumVO;
import com.dreamnalgae.tms.model.tpop.tpop1006.SujumVO;
import com.dreamnalgae.tms.service.tpop.Tpop1006Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tpop/1006")
public class Tpop1006Controller {
    private final Tpop1006Service tpop1006Service;

    /**
     * 출판사별 서점 목록 조회 API
     *
     * @param keyword     서점명, 약칭, 코드 등 검색 키워드
     * @param chulpanCd   출판사 코드
     * @param useYn       사용 여부 ('1' 또는 '0' 또는 null)
     * @param strWhere    추가 조건 (선택사항)
     * @return 서점 목록
     */
    @GetMapping("/sujum-list")
    public List<SujumVO> getSujumList(
            @RequestParam String userCetCd,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String chulpanCd,
            @RequestParam(required = false, defaultValue = "") String useYn       
    ) {
        return tpop1006Service.selectSujumList(userCetCd, chulpanCd, keyword, useYn);
    }
    

    @GetMapping("/list")
    public ResponseEntity<List<ChulpanSujumVO>> getList(
        @RequestParam String userCetCd,
        @RequestParam(required = false) String chulpanCd,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String useYn
    ) {
        List<ChulpanSujumVO> list = tpop1006Service.selectChulpanSujumList(userCetCd, chulpanCd, keyword, useYn);
        return ResponseEntity.ok(list);
    }


}
