package com.dreamnalgae.tms.controller.system;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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



    @PostMapping(value="/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> importExcel(@RequestPart("file") MultipartFile file) throws Exception {
        List<Map<String,Object>> rows = new ArrayList<>();

        try (InputStream in = file.getInputStream(); XSSFWorkbook wb = new XSSFWorkbook(in)) {
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext()) return Map.of("success", true, "data", rows);

            // 첫 번째 행 = 헤더
            Row headerRow = it.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(c -> headers.add(c.getStringCellValue().trim()));

            // 데이터 행
            while (it.hasNext()) {
                Row r = it.next();
                Map<String,Object> map = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell c = r.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    map.put(headers.get(i), getCellValue(c));
                }
                rows.add(map);
            }
        }

        return Map.of("success", true, "data", rows);
    }

    private Object getCellValue(Cell c) {
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING  -> c.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(c) ? c.getDateCellValue() : c.getNumericCellValue();
            case BOOLEAN -> c.getBooleanCellValue();
            case FORMULA -> c.getCachedFormulaResultType() == CellType.NUMERIC
                                ? (DateUtil.isCellDateFormatted(c) ? c.getDateCellValue() : c.getNumericCellValue())
                                : c.getStringCellValue();
            default -> null;
        };
    }


      
    @PostMapping("/excel/import")
    public List<Map<String, Object>> readExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return commonService.readExcel(file);
    }










}
