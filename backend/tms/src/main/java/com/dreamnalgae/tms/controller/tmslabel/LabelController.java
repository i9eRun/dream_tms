package com.dreamnalgae.tms.controller.tmslabel;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.entity.tmslabel.TmsDungeMst;
import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeMstVO;
import com.dreamnalgae.tms.model.tmslabel.LabelPrintRequest;
import com.dreamnalgae.tms.model.tmslabel.TmsDungeMstSaveRequest;
import com.dreamnalgae.tms.model.tmslabel.TmsDungeTransferReq;
import com.dreamnalgae.tms.service.tmslabel.LabelService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tmslabel")
public class LabelController {
    private final LabelService labelService;

    @GetMapping("/agency-list")
    public List<Map<String, Object>> getAgency(@RequestParam String userCetCd) {
        return labelService.getAgency(userCetCd);
    }

    @GetMapping("/chulpan-list")
    public List<Map<String, Object>> getChulpanList(@RequestParam String userCetCd, @RequestParam String chooseDate) {
        return labelService.selectChulpanList(userCetCd, chooseDate);
    }

    @GetMapping("/list")
    public List<TmsDungeMstVO> getList(
        @RequestParam(name = "chooseDate", required = true) String dungeDt,
        @RequestParam(name = "userCetCd", required = true) String userCetCd,
        @RequestParam(name = "chulgoGb", required = false) String chulgoGb,
        @RequestParam(name = "chulpanCd", required = false) String chulpanCd,
        @RequestParam(name = "cetCd", required = false) String cetCd
    ) {
        return labelService.selectList(dungeDt, userCetCd, chulgoGb, chulpanCd, cetCd);
    }
    

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReorder(@RequestBody Map<String, String> request) {
        try {
            String userCetCd = request.get("userCetCd");
            String rowSeq = request.get("rowSeq");
            String ordNo = request.get("ordNo");

            if (!StringUtils.hasText(userCetCd) ||
                    !StringUtils.hasText(rowSeq) ||
                    !StringUtils.hasText(ordNo)) {
                    return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "필수값(userCetCd, rowSeq, ordNo)이 누락되었습니다."
                    ));
            }

            labelService.deleteOne(userCetCd, rowSeq, ordNo);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }


    // 미전송 데이터를 전송 상태로 일괄 업데이트
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TmsDungeTransferReq req) {
        // 필수 검증
        if (req.getItems() == null || req.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "items가 비어 있습니다."
            ));
        }
        // 각 항목의 키 검증 (null/blank)
        for (var it : req.getItems()) {
            if (!StringUtils.hasText(it.getUserCetCd())
                    || it.getRowSeq() == null
                    || !StringUtils.hasText(it.getOrdNo())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "필수값(userCetCd,rowSeq,ordNo)이 누락된 항목이 있습니다."
                ));
            }
        }

        var r = labelService.transferBulk(req);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "updated", r.updated(),
            "failed",  r.failed(),
            "successKeys", r.successKeys()
        ));
    }


    @PostMapping("/label/print")
    public ResponseEntity<byte[]> printLabels(@RequestBody LabelPrintRequest req) throws Exception {
        byte[] pdf = labelService.buildLabelPdfAndMarkPrinted(req);

        HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
        // 브라우저에서 바로 보기(inline)
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"labels.pdf\"");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }



    // 리스트를 고정크기(25)로 자르되, 비어 있어도 최소 1페이지 보장
    private static <T> List<List<T>> chunkKeepOnePage(List<T> src, int size) {
        List<List<T>> out = new ArrayList<>();
        if (src == null || src.isEmpty()) {
            out.addAll(Collections.emptyList()); // 데이터가 0건이어도 1페이지 보장
            return out;
        }
        for (int i =0; i < src.size(); i += size) {
            out.add(src.subList(i, Math.min(i+size, src.size())));
        }
        return out;
    }

    // PDF 헤더 생성
    private void addHeader(Document document, Font titleFont, Font font, 
                        String chooseDate, String custNm, String custDivGb) throws Exception {
        Paragraph title = new Paragraph("출 고 증", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10f);
        document.add(title);

        // 대행사명과 일자 표시
        Paragraph info = new Paragraph("대행사: " + custNm + "         일자: " + chooseDate, font);
        if ("1".equals(custDivGb)) {
            info = new Paragraph("일자: " + chooseDate, font);
        }
        
        info.setSpacingBefore(5f);
        info.setSpacingAfter(15f);
        document.add(info);
    }


    // PDF 본문 테이블 생성 (헤더 + 데이터 + 공백행 패딩)
    private PdfPTable buildBodyTable(Font font,
                                    List<TmsDungeMstVO> pageData,
                                    int rowsPerPage,
                                    int rowNoOffset) throws Exception {

        PdfPTable table = new PdfPTable(new float[]{1, 3, 2, 4, 2, 2});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6f);

        // 테이블 헤더
        String[] headers = {"번호", "출판사명", "지역명", "서점명", "부수", "덩이"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPadding(5f);
            table.addCell(cell);
        }

        // 테이블 데이터 행
        int idx = 0;
        for (TmsDungeMstVO vo : pageData) {
            idx++;
                 addDataRow(table, font,
                    rowNoOffset + idx,
                    safe(vo.getChulNm()), safe(vo.getJiyukNm()),
                    safe(vo.getSujumNm()),
                    vo.getQty() == null ? "" : String.valueOf(vo.getQty()),
                    vo.getDunge() == null ? "" : String.valueOf(vo.getDunge()));
        }

        // 공백행 패딩
        for (int i = idx + 1; i <= rowsPerPage; i++) {
            addBlankRow(table, font, rowNoOffset + i);
        }

        return table;
    }

    private static String safe(String s) { return s == null ? "" : s;}

    
    private void addDataRow(PdfPTable table,
                            Font font,
                            int no,
                            String chulNm,
                            String jiyukNm,
                            String sujumNm,
                            String qty,
                            String dunge) {

        PdfPCell[] cells = new PdfPCell[] {
            new PdfPCell(new Phrase(String.valueOf(no), font)),
            new PdfPCell(new Phrase(chulNm, font)),
            new PdfPCell(new Phrase(jiyukNm, font)),
            new PdfPCell(new Phrase(sujumNm, font)),
            new PdfPCell(new Phrase(qty, font)),
            new PdfPCell(new Phrase(dunge, font))
        };

        for (PdfPCell c : cells) {
            c.setMinimumHeight(20f);
            c.setPadding(4f);
            table.addCell(c);
        }
    }


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
            c.setMinimumHeight(20f); // 행 높이 고정감
            c.setPadding(4f);
            table.addCell(c);
        }
    }


    // PDF 하단 푸터 생성
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
        outer.setSpacingBefore(5f);

        PdfPCell box = new PdfPCell();
        box.setBorder(Rectangle.BOX);
        box.setPadding(10f);

        PdfPTable inner = new PdfPTable(2);
        inner.setWidthPercentage(100);

        // 각 페이지 합계
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



    @PostMapping("/print/chulgo")
    public ResponseEntity<byte[]> exportChulgoPdf(
            @RequestParam("userCetCd") String userCetCd,
            @RequestParam("custDivGb") String custDivGb,
            @RequestParam("custName") String custName,
            @RequestParam("ordNos") String ordNos // 프론트에서 JSON.stringify로 보냄
    ) throws Exception {

        String chooseDate = "";
        String custNm = "";

        // JSON 문자열을 List<String>으로 변환
        ObjectMapper mapper = new ObjectMapper();
        List<String> ordNoList = mapper.readValue(ordNos, new TypeReference<List<String>>() {});

        List<TmsDungeMstVO> all = labelService.selectListByOrdNos(userCetCd, ordNoList);

        if (!all.isEmpty()) {
            // 첫번째 데이터에서 날짜 추출
            String ordNo =all.get(0).getOrdNo();
             if (ordNo != null && ordNo.contains("-")) {
                String rawDate = ordNo.split("-")[0]; // "20250825"
                try {
                    LocalDate date = LocalDate.parse(rawDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    chooseDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // "2025-08-25"
                } catch (Exception e) {
                    chooseDate = rawDate; // 파싱 실패 시 원본 그대로
                }
            }

            // 첫번재 데이터 기준으로 대행사명 조회
            String cetCd = all.get(0).getCetCd();
            if (StringUtils.hasText(cetCd)) {
                custNm = labelService.selectCustNmByCetCd(cetCd, userCetCd);
            }

        }

        // PDF 응답 준비
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 30, 30, 40, 40); // 좌우 30, 상단 40, 하단 40
        PdfWriter.getInstance(document, baos);
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

        // 페이지 단위 분할
        List<List<TmsDungeMstVO>> pages = chunkKeepOnePage(all, rowsPerPage);

        int totalQtySum = all.stream().mapToInt(v -> v.getQty() == null ? 0 : v.getQty().intValue()).sum();
        int totalDungeSum = all.stream().mapToInt(v -> v.getDunge() == null ? 0 : v.getDunge().intValue()).sum();
        boolean multiPage = pages.size() > 1;

        for (int p = 0; p < pages.size(); p++) {
            List<TmsDungeMstVO> page = pages.get(p);

            // PDF 헤더 생성
            addHeader(document, titleFont, bodyFont, chooseDate, custNm, custDivGb); // chooseDate 대신 공란 or 첫 데이터 기준

            // PDF 본문 테이블 생성
            PdfPTable table = buildBodyTable(bodyFont, page, rowsPerPage, 0);
            document.add(table);

            // 합계 처리
            int pageQtySum = page.stream().mapToInt(v -> v.getQty() == null ? 0 : v.getQty().intValue()).sum();
            int pageDungeSum = page.stream().mapToInt(v -> v.getDunge() == null ? 0 : v.getDunge().intValue()).sum();

            boolean isLast = (p == pages.size() - 1);
            boolean showGrandTotal = multiPage && isLast;

            addFooterBox(document, baseFont, pageQtySum, pageDungeSum, showGrandTotal, totalQtySum, totalDungeSum);

            if (!isLast) document.newPage();
        }

        document.close();

        // 파일명 설정
        String fileName = "chulgo.pdf";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        //headers.setContentDispositionFormData("inline", encodedFileName); // http환경에 브라우저에 따라 다운로드로 변경될 수 있음.
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\""); //브라우저에서 바로보기

        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }



    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody TmsDungeMstSaveRequest request) {
        labelService.saveAll(
            request.getInsertList(),
            request.getUpdateList(),
            request.getDeleteList(),
            request.getInsertId(),
            request.getUserCetCd(),
            request.getLoginCustDivGb(),  // 추가
            request.getLoginCustName(),  // 추가
            request.getLoginCustCd()     // 추가
        );

        System.out.println(request);

        return ResponseEntity.ok("success");
    }







}
