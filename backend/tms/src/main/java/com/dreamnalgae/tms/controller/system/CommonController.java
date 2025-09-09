package com.dreamnalgae.tms.controller.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamnalgae.tms.model.system.ExcelRequestDTO;
import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeMstVO;
import com.dreamnalgae.tms.service.system.CommonService;
import com.dreamnalgae.tms.service.tchu.Tchu1001Service;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class CommonController {
    private final CommonService commonService;
    private final Tchu1001Service tchu1001Service;
    
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

    

    // ===== 1) 리스트를 고정 크기로 자르되, 비어 있어도 최소 1페이지 보장 =====
    private static <T> List<List<T>> chunkKeepOnePage(List<T> src, int size) {
        List<List<T>> out = new ArrayList<>();
        if (src == null || src.isEmpty()) {
            out.add(Collections.emptyList());   // <= 데이터 0건이어도 1페이지 보장
            return out;
        }
        for (int i = 0; i < src.size(); i += size) {
            out.add(src.subList(i, Math.min(i + size, src.size())));
        }
        return out;
    }


    // pdf 헤더
    private void addHeader(Document document, Font titleFont, Font font, String chooseDate) throws Exception {
        Paragraph title = new Paragraph("출 고 증", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph info = new Paragraph("대행사: 각대행사이름 - 오후 출고증   일자: " + chooseDate, font);
        info.setSpacingBefore(10f);
        info.setSpacingAfter(8f);
        document.add(info);
    }


    // ===== 3) 본문 테이블 생성 (헤더 + 데이터 + 공백행 패딩) =====
    private PdfPTable buildBodyTable(Font font,
                                    List<TmsDungeMstVO> pageData,
                                    int rowsPerPage,
                                    int rowNoOffset /*(페이지별 번호 시작 오프셋; 0=페이지마다 1부터)*/) throws Exception {

        PdfPTable table = new PdfPTable(new float[]{1, 3, 2, 4, 2, 2});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6f);

        // (a) 헤더
        String[] headers = {"번호", "출판사명", "지역명", "서점명", "부수", "덩이"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPadding(5f);
            table.addCell(cell);
        }

        // (b) 실제 데이터 행
        int idx = 0;
        for (TmsDungeMstVO vo : pageData) {
            idx++;
            addDataRow(table, font,
                    rowNoOffset + idx,                                       // 번호
                    safe(vo.getChulpanNm()), safe(vo.getJiyukNm()),
                    safe(vo.getSujumNm()),
                    vo.getQty() == null ? "" : String.valueOf(vo.getQty()),
                    vo.getDunge() == null ? "" : String.valueOf(vo.getDunge()));
        }

        // (c) 공백행 패딩 (데이터 부족분 채우기)
        for (int i = idx + 1; i <= rowsPerPage; i++) {
            addBlankRow(table, font, rowNoOffset + i);
        }

        return table;
    }

    private static String safe(String s) { return s == null ? "" : s; }

    // 실제 데이터 행 셀 추가(가독성을 위해 최소 높이와 패딩 줌)
    private void addDataRow(PdfPTable table, Font font,
                            int no, String chulpanNm, String jiyukNm, String sujumNm,
                            String qty, String dunge) {
        PdfPCell[] cells = new PdfPCell[] {
                new PdfPCell(new Phrase(String.valueOf(no), font)),
                new PdfPCell(new Phrase(chulpanNm, font)),
                new PdfPCell(new Phrase(jiyukNm, font)),
                new PdfPCell(new Phrase(sujumNm, font)),
                new PdfPCell(new Phrase(qty, font)),
                new PdfPCell(new Phrase(dunge, font))
        };
        for (PdfPCell c : cells) {
            c.setMinimumHeight(18f);
            c.setPadding(4f);
            table.addCell(c);
        }
    }

    // 공백행(번호만 있고 나머지 빈칸) 추가
    private void addBlankRow(PdfPTable table, Font font, int no) {
        PdfPCell[] cells = new PdfPCell[] {
                new PdfPCell(new Phrase(String.valueOf(no), font)),
                new PdfPCell(new Phrase("", font)),
                new PdfPCell(new Phrase("", font)),
                new PdfPCell(new Phrase("", font)),
                new PdfPCell(new Phrase("", font)),
                new PdfPCell(new Phrase("", font))
        };
        for (PdfPCell c : cells) {
            c.setMinimumHeight(18f);   // 행 높이 고정감
            c.setPadding(4f);
            table.addCell(c);
        }
    }





    // ===== 하단 박스(합계+안내문+회사정보) =====
    private void addFooterBox(Document document,
                              BaseFont baseFont,
                              int pageQtySum,
                              int pageDungeSum,
                              boolean showGrandTotal,
                              int totalQtySum,
                              int totalDungeSum) throws Exception {
                                
        Font sumFont = new Font(baseFont, 12, Font.BOLD);
        Font noteFont = new Font(baseFont, 10);

        PdfPTable outer = new PdfPTable(1);
        outer.setWidthPercentage(100);
        outer.setSpacingBefore(10f);

        PdfPCell box = new PdfPCell();
        box.setBorder(Rectangle.BOX);
        box.setPadding(10f);

        PdfPTable inner = new PdfPTable(2);
        inner.setWidthPercentage(100);

        // (1) 페이지 합계
        Paragraph sumP = new Paragraph("부수 합계 : " + pageQtySum + "     덩이 합계 : " + pageDungeSum, sumFont);
        sumP.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell sumCell = new PdfPCell(sumP);
        sumCell.setColspan(2);
        sumCell.setBorder(Rectangle.NO_BORDER);
        sumCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        sumCell.setPaddingBottom(5f);
        inner.addCell(sumCell);

        // (2) 전체 합계 (다페이지 & 마지막 페이지만)
        if (showGrandTotal) {
            Paragraph totalP = new Paragraph("총 부수 합계 : " + totalQtySum + "    총 덩이 합계 : " + totalDungeSum, sumFont);
            totalP.setAlignment(Element.ALIGN_RIGHT);
            PdfPCell totalCell = new PdfPCell(totalP);
            totalCell.setColspan(2);
            totalCell.setBorder(Rectangle.NO_BORDER);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setPaddingBottom(8f);
            inner.addCell(totalCell);
        }


        // (3) 안내문
        Paragraph leftNote = new Paragraph("", noteFont);
        leftNote.setAlignment(Element.ALIGN_LEFT);
        leftNote.add("※ 모든 확인의 책임은 최근(시내 2개월, 지방 3개월)에 한함.\n");
        leftNote.add("※ 출고증은 모든 업무 확인의 근거자료가 되오니 정확히 기재하여 주십시오.\n");
        leftNote.add("※ 계좌번호 : 기업은행 377-018441-04-017   예금주 : 드림날개\n");
        leftNote.add("※ 책을 가장 소중히 생각하는 도서물류의 최강 !");
        PdfPCell leftCell = new PdfPCell(leftNote);
        leftCell.setColspan(2);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setPaddingBottom(8f);
        inner.addCell(leftCell);

        // (4) 회사정보 (우측 정렬)
        Paragraph rightInfo = new Paragraph("", noteFont);
        rightInfo.setAlignment(Element.ALIGN_RIGHT);
        rightInfo.add("경기도 파주시 파주읍 부곡리 7-12\n");
        rightInfo.add("전화 / 드림날개 : 031)945-6001   황금날개 : 031)972-4933\n");
        rightInfo.add("팩스 / 드림날개 : 031)945-6009   황금날개 : 031)972-4935");
        PdfPCell rightCell = new PdfPCell(rightInfo);
        rightCell.setColspan(2);
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        inner.addCell(rightCell);

        box.addElement(inner);
        outer.addCell(box);
        document.add(outer);

    }


    // 출고증 출력 get방식 사용 안함.
    @GetMapping("/print/pdf/chulgo")
    public void exportChulgoPdf(HttpServletResponse response,
                                @RequestParam String chooseDate,
                                @RequestParam String userCetCd) throws Exception {

        // 데이터 조회
        List<TmsDungeMstVO> all = tchu1001Service.selectList(chooseDate, userCetCd, null, null, null);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=chulgo.pdf");

        try (OutputStream os = response.getOutputStream()) {
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter.getInstance(document, os);
            document.open();

            // 폰트 등록
            BaseFont baseFont = BaseFont.createFont(
                    "src/main/resources/fonts/NanumGothic.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font bodyFont = new Font(baseFont, 10);

            final int rowsPerPage = 25;

            // 페이지 단위로 분할 (0건이어도 최소 1페이지 보장)
            List<List<TmsDungeMstVO>> pages = chunkKeepOnePage(all, rowsPerPage);

            int totalQtySum   = all.stream().mapToInt(v -> v.getQty()   == null ? 0 : v.getQty().intValue()).sum();
            int totalDungeSum = all.stream().mapToInt(v -> v.getDunge() == null ? 0 : v.getDunge().intValue()).sum();
            boolean multiPage = pages.size() > 1;

            for (int p = 0; p < pages.size(); p++) {
                List<TmsDungeMstVO> page = pages.get(p);

                // (1) 헤더
                addHeader(document, titleFont, bodyFont, chooseDate);

                // (2) 본문 (데이터 + 빈 행 패딩)
                PdfPTable table = buildBodyTable(bodyFont, page, rowsPerPage, 0);
                document.add(table);

                // (3) 합계 (이 페이지 데이터 기준)
                int pageQtySum = page.stream().mapToInt(v -> v.getQty() == null ? 0 : v.getQty().intValue()).sum();
                int pageDungeSum = page.stream().mapToInt(v -> v.getDunge() == null ? 0 : v.getDunge().intValue()).sum();

                boolean isLast = (p == pages.size() - 1);
                boolean showGrandTotal = multiPage && isLast;  // 한 페이지면 false

                addFooterBox(document, baseFont, pageQtySum, pageDungeSum,
                            showGrandTotal, totalQtySum, totalDungeSum);

                if (!isLast) document.newPage();
            }

            document.close();
        }
    }

    







}
