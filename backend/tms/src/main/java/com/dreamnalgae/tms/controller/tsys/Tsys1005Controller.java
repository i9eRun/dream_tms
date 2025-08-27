package com.dreamnalgae.tms.controller.tsys;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsys.TmsCodeMVO;
import com.dreamnalgae.tms.service.tsys.Tsys1005Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsys/code")
public class Tsys1005Controller {
    private final Tsys1005Service tsys1005Service;

    @GetMapping("/grouplist")
    public List<TmsCodeMVO> getGroupList(@RequestParam(required = false) String groupKeyword) {
        TmsCodeMVO param = new TmsCodeMVO();
        param.setGroupKeyword(groupKeyword);
        return tsys1005Service.selectCodeGroups(param);
    }
    
    @GetMapping("/list/{groupCd}")
    public List<TmsCodeMVO> getCodeList(@PathVariable String groupCd) {
        return tsys1005Service.selectCodeList(groupCd);
    }

    @PostMapping("/groupinsert")
    public ResponseEntity<String> insertGroup(@RequestBody TmsCodeMVO dto) {
        int result = tsys1005Service.insertGroupCode(dto);
        return (result > 0)
            ? ResponseEntity.ok("등록 성공")
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
    }

    @PostMapping("/groupupdate")
    public ResponseEntity<String> updateGroup(@RequestBody TmsCodeMVO dto) {
        int result = tsys1005Service.updateGroupCode(dto);
        return (result > 0)
            ? ResponseEntity.ok("수정 성공")
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
    }

    @DeleteMapping("/groupdelete")
    public ResponseEntity<String> deleteGroupCode(@RequestBody TmsCodeMVO dto) {
        int result = tsys1005Service.deleteGroupCode(dto.getUserCetCd(), dto.getCodeCd());
        return result > 0
            ? ResponseEntity.ok("삭제 성공")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 데이터가 없습니다.");
    }

    @PostMapping("/codeinsert")
    public ResponseEntity<String> insertCode(@RequestBody TmsCodeMVO dto) {
        int result = tsys1005Service.insertCode(dto);
        return result > 0
        ? ResponseEntity.ok("등록 성공")
        : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
    }

    @PostMapping("/codeupdate")
    public ResponseEntity<String> updateCode(@RequestBody TmsCodeMVO dto) {
        int result = tsys1005Service.updateCode(dto);
        return result > 0
        ? ResponseEntity.ok("수정 성공")
        : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
    }

    @PostMapping("/codedelete")
    public ResponseEntity<?> deleteCode(@RequestBody TmsCodeMVO vo) {
        int result = tsys1005Service.deleteCode(vo);
        return result > 0
            ? ResponseEntity.ok("삭제 성공")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 실패");
    }


}
