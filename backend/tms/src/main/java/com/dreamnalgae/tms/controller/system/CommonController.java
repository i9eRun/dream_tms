package com.dreamnalgae.tms.controller.system;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.system.ExcelRequestDTO;
import com.dreamnalgae.tms.service.system.CommonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class CommonController {
    private final CommonService commonService;
    
    @PostMapping(value = "/exceldownload", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody ExcelRequestDTO requestDto, jakarta.servlet.http.HttpServletRequest httpRequest) throws IOException {
        byte[] excelBytes = commonService.generateExcel(requestDto);

        // === 로그 남기기 ===
        // 로그인 사용자 정보는 세션/시큐리티/커스텀 저장소 등 프로젝트 규약에 맞춰 꺼내세요.
        //String userId    = dream.util.SecurityUtil.getLoginUserIdOrNull();   // 예시 유틸
        //String userCetCd = dream.util.SecurityUtil.getLoginUserCetCdOrNull();// 예시 유틸

        Integer rowCount = (requestDto.getData() != null) ? requestDto.getData().size() : 0;

        // menuNm이 프론트에서 오지 않는 경우: 파일명에서 추정 (YYYYMMDD_메뉴명.xlsx 형식일 때)
        String menuNm = (requestDto.getMenuNm() != null)
                ? requestDto.getMenuNm()
                : parseMenuNmFromFileName(requestDto.getFileName());

        commonService.logExcelDownload(
                requestDto.getUserId(),
                requestDto.getUserCetCd(),
                requestDto.getFileName(),
                requestDto.getMenuId(),   // null 허용
                menuNm,                   // null 가능
                rowCount,
                httpRequest,
                requestDto.getParamsJson()
        );

        // ===== 한글 파일명 인코딩 처리 (RFC 5987) =====
        String encodedFileName = URLEncoder.encode(requestDto.getFileName(), StandardCharsets.UTF_8)
                                           .replaceAll("\\+", "%20");
        String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return ResponseEntity.ok()
            .headers(headers)
            .body(excelBytes);
    }


    // 파일명 예: 20250731_메뉴이름.xlsx → "메뉴이름" 추출
    private String parseMenuNmFromFileName(String fileName) {
        if (fileName == null) return null;
        try {
            String base = fileName;
            if (base.toLowerCase().endsWith(".xlsx")) {
                base = base.substring(0, base.length() - 5);
            }
            int idx = base.indexOf('_');
            if (idx > -1 && idx + 1 < base.length()) {
                return base.substring(idx + 1);
            }
        } catch (Exception ignore) {}
        return null;
    }
}
