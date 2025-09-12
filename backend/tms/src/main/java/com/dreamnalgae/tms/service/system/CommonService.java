package com.dreamnalgae.tms.service.system;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamnalgae.tms.model.system.ExcelRequestDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final NamedParameterJdbcTemplate jdbc;

    public byte[] generateExcel(ExcelRequestDTO dto) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // 가운데 정렬 + 보더 스타일
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // 헤더 스타일 (회색 배경)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(cellStyle);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 1. 헤더 생성
            Row headerRow = sheet.createRow(0);
            List<ExcelRequestDTO.HeaderInfo> headers = dto.getHeaders();

            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i).getHeader());
                cell.setCellStyle(headerStyle);
            }

            // 2. 데이터 생성
            List<Map<String, Object>> dataList = dto.getData();

            for (int r = 0; r < dataList.size(); r++) {
                Row row = sheet.createRow(r + 1);
                Map<String, Object> rowData = dataList.get(r);

                for (int c = 0; c < headers.size(); c++) {
                    String key = headers.get(c).getDataIndex();
                    Object value = rowData.get(key);
                    Cell cell = row.createCell(c);
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(cellStyle);
                }
            }

            // 3. 셀 너비 자동 조정
            for (int c = 0; c < headers.size(); c++) {
                int maxWidth = headers.get(c).getHeader().getBytes().length;
                for (int r = 0; r < dataList.size(); r++) {
                    Object value = dataList.get(r).get(headers.get(c).getDataIndex());
                    if (value != null) {
                        int len = value.toString().getBytes().length;
                        if (len > maxWidth) {
                            maxWidth = len;
                        }
                    }
                }
                int width = (maxWidth + 2) * 256;
                sheet.setColumnWidth(c, Math.min(width, 255 * 256));
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        }
    }


    public void logExcelDownload(
        String userId,
        String userCetCd,
        String fileName,
        String menuId,
        String menuNm,
        Integer rowCount,
        HttpServletRequest request,
        String paramsJson
    ) {
        String sql = """
            INSERT INTO TMS_EXCEL_DL_LOG
              (USER_ID, USER_CET_CD, FILE_NAME, MENU_ID, MENU_NM,
               ROW_COUNT, CLIENT_IP, USER_AGENT, REQUEST_URI, PARAMS_JSON)
            VALUES
              (:userId, :userCetCd, :fileName, :menuId, :menuNm,
               :rowCount, :clientIp, :userAgent, :requestUri, :paramsJson)
                """;

        Map<String, Object> p = new HashMap<>();
        p.put("userId", userId);
        p.put("userCetCd", userCetCd);
        p.put("fileName", fileName);
        p.put("menuId", menuId);
        p.put("menuNm", menuNm);
        p.put("rowCount", rowCount);
        p.put("clientIp", resolveClientIp(request));
        p.put("userAgent", safeTruncate(request.getHeader("User-Agent"), 4000));
        p.put("requestUri", safeTruncate(request.getRequestURI(), 400));
        p.put("paramsJson", paramsJson);

        jdbc.update(sql, p);
    }

        private static String resolveClientIp(HttpServletRequest request) {
        // 프록시/로드밸런서 환경 고려
        String[] headerKeys = {
                "X-Forwarded-For",
                "X-Real-IP",
                "CF-Connecting-IP",
                "X-Client-IP",
                "X-Forwarded",
                "Forwarded-For",
                "Forwarded"
        };
        for (String h : headerKeys) {
            String v = request.getHeader(h);
            if (v != null && !v.isEmpty() && !"unknown".equalsIgnoreCase(v)) {
                // X-Forwarded-For: client, proxy1, proxy2...
                int comma = v.indexOf(',');
                return (comma > -1) ? v.substring(0, comma).trim() : v.trim();
            }
        }
        return request.getRemoteAddr();
    }

    private static String safeTruncate(String s, int max) {
        if (s == null) return null;
        return (s.length() <= max) ? s : s.substring(0, max);
    }


    // // 엑셀 기본 import 로직
    // public List<Map<String, Object>> readExcel(MultipartFile file) throws IOException {
    //     List<Map<String, Object>> result = new ArrayList<>();

    //     try (InputStream is = file.getInputStream();
    //          Workbook workbook = WorkbookFactory.create(is)) {
            
    //         Sheet sheet = workbook.getSheetAt(0);
    //         Iterator<Row> rows = sheet.iterator();

    //         // 첫번째 행은 헤더라고 가정
    //         if (rows.hasNext()) rows.next();

    //         while (rows.hasNext()) {
    //             Row row = rows.next();
    //             Map<String, Object> map = new HashMap<>();

    //             map.put("chulpanCnvCd", getCellValue((row.getCell(0)))); // 출판사코드
    //             map.put("chulpanCnvNm", getCellValue((row.getCell(1)))); // 출판사명
    //             map.put("sujumCnvCd", getCellValue((row.getCell(2))));   // 거래처 코드
    //             map.put("sujumCnvNm", getCellValue((row.getCell(3))));   // 거래처명
    //             map.put("qty", getCellValue((row.getCell(4))));          // 수량 
    //             map.put("box", getCellValue((row.getCell(5))));          // 덩이

    //             result.add(map);
    //         }
    //     }

    //     return result;
    // }



    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }


    
    public List<Map<String, Object>> readExcel(MultipartFile file) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();

        try (InputStream is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) return result;

            // === 1) 헤더 행 탐색 ===
            Row headerRow = null;
            while (rows.hasNext()) {
                Row candidate = rows.next();
                String firstCell = getCellValue(candidate.getCell(0));
                if (firstCell != null &&
                    (firstCell.contains("출판사") ||
                    firstCell.contains("출판사코드") ||
                    firstCell.contains("거래처") ||
                    firstCell.equalsIgnoreCase("NO"))) {
                    headerRow = candidate;
                    break;
                }
            }
            if (headerRow == null) return result;

            // === 2) 헤더 매핑 ===
            Map<Integer, String> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = getCellValue(cell);
                if (header == null) continue;

                switch (header.trim()) {
                    // 기본 양식
                    case "출판사코드", "출판사코" -> headerMap.put(cell.getColumnIndex(), "chulpanCnvCd");
                    case "출판사명", "출판사"   -> headerMap.put(cell.getColumnIndex(), "chulpanCnvNm");
                    case "거래처코드"          -> headerMap.put(cell.getColumnIndex(), "sujumCnvCd");
                    case "거래처명", "서점명"  -> headerMap.put(cell.getColumnIndex(), "sujumCnvNm");
                    case "수량"               -> headerMap.put(cell.getColumnIndex(), "qty");
                    case "덩이", "덩이 수량"    -> headerMap.put(cell.getColumnIndex(), "box");

                    // 확장 필드
                    case "NO"                 -> headerMap.put(cell.getColumnIndex(), "rowNo");
                    case "지점"               -> headerMap.put(cell.getColumnIndex(), "jiyukNm");
                    case "박스 수량"           -> headerMap.put(cell.getColumnIndex(), "pack");
                    case "비고"               -> headerMap.put(cell.getColumnIndex(), "remark");
                }
            }

            // === 3) 데이터 행 처리 ===
            String lastChulpanNm = null; // 출판사명 carry-over

            while (rows.hasNext()) {
                Row row = rows.next();
                Map<String, Object> map = new HashMap<>();

                boolean skipRow = false;
                boolean allEmpty = true;

                for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
                    int colIdx = entry.getKey();
                    String fieldName = entry.getValue();

                    String value = getCellValue(row.getCell(colIdx));

                    if (value != null && !value.trim().isEmpty()) {
                        allEmpty = false;
                    }

                    // ✅ 합계/총계/소계 (공백 포함까지 처리)
                    if (value != null) {
                        String normalized = value.replaceAll("\\s+", "");
                        if (normalized.contains("합계") ||
                            normalized.contains("총계") ||
                            normalized.contains("소계")) {
                            skipRow = true;
                            break;
                        }
                    }

                    map.put(fieldName, value);
                }

                if (!skipRow && !allEmpty && !map.isEmpty()) {
                    // ✅ 출판사명 carry-over
                    String chulpanNm = (String) map.get("chulpanCnvNm");
                    if (chulpanNm != null && !chulpanNm.trim().isEmpty()) {
                        lastChulpanNm = chulpanNm;
                    } else if (lastChulpanNm != null) {
                        map.put("chulpanCnvNm", lastChulpanNm);
                    }

                    // ✅ 출판사코드/거래처코드 보정
                    if (isEmpty(map.get("chulpanCnvCd"))) {
                        map.put("chulpanCnvCd", map.get("chulpanCnvNm"));
                    }
                    if (isEmpty(map.get("sujumCnvCd"))) {
                        map.put("sujumCnvCd", map.get("sujumCnvNm"));
                    }

                    result.add(map);
                }
            }
        }

        return result;
    }

    private boolean isEmpty(Object obj) {
        return obj == null || obj.toString().trim().isEmpty();
    }









}
