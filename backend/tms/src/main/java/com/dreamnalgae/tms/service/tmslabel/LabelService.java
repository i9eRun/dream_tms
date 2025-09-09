package com.dreamnalgae.tms.service.tmslabel;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamnalgae.tms.model.tchu.tchu1001.TmsDungeMstVO;
import com.dreamnalgae.tms.model.tmslabel.LabelItem;
import com.dreamnalgae.tms.model.tmslabel.LabelPrintRequest;
import com.dreamnalgae.tms.model.tmslabel.TmsDungeTransferItem;
import com.dreamnalgae.tms.model.tmslabel.TmsDungeTransferReq;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class LabelService {
    private final NamedParameterJdbcTemplate jdbc;

    private static volatile BaseFont CACHED_BASE_FONT;

    private BaseFont loadBaseFontCached() throws IOException, DocumentException {
        if (CACHED_BASE_FONT == null) {
            synchronized (LabelService.class) {
                if (CACHED_BASE_FONT == null) {
                    CACHED_BASE_FONT = loadBaseFont("/fonts/NanumGothic.ttf");
                }
            }
        }
        return CACHED_BASE_FONT;
    }

    private static float mmToPt(float mm) { return (mm / 25.4f) * 72f; }

    private static String safe(String s) { return s == null ? "" : s; }

    // 테이블/컬럼명은 질문 맥락에 맞춰 예시: TMS_DUNGE_MST (OUT_YN 업데이트)
    private static final String SQL_MARK_PRINTED = """
        UPDATE TMS_DUNGE_MST
        SET OUT_YN   = '1',
            UPDATE_DT = SYSDATE,
            UPDATE_ID = :updateId
        WHERE USER_CET_CD = :userCetCd
        AND ROW_SEQ IN (:rowSeqs)
    """;

    public static record BatchResult(int updated, int failed,
                        List<TmsDungeTransferItem> successKeys) {}

    // 2) classpath에서 byte[]로 읽어서 등록
    private BaseFont loadBaseFont(String classpathPath) throws IOException, DocumentException {
        try (InputStream is = getClass().getResourceAsStream(classpathPath)) {
            if (is == null) {
                throw new FileNotFoundException(classpathPath + " not on classpath");
            }
            byte[] fontBytes = is.readAllBytes();

            // name은 null 금지. 확장자 포함한 가짜 파일명으로 전달해야 함(.ttf/.otf)
            String name = classpathPath;
            int slash = name.lastIndexOf('/');
            if (slash >= 0) name = name.substring(slash + 1); // 예: NanumGothic.ttf

            String lower = name.toLowerCase();
            if (!lower.endsWith(".ttf") && !lower.endsWith(".otf")) {
                name = name + ".ttf"; // 확장자 보정
            }

            return BaseFont.createFont(
                name,                 // ✅ null 금지, .ttf/.otf 필요
                BaseFont.IDENTITY_H,  // ✅ 유니코드
                BaseFont.EMBEDDED,    // ✅ 임베딩
                true,                 // cached
                fontBytes,            // ✅ byte[] 폰트
                null
            );
        }
    }


    public List<Map<String, Object>> getAgency(String userCetCd) {
        String sql = """
            SELECT
                CUST_CD AS "custCd",
                CUST_NM AS "custNm"
            FROM TMS_CUST
            WHERE USER_CET_CD = :userCetCd
              AND CUST_DIV_GB = '10'
            ORDER BY CUST_NM
        """;

        Map<String,Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        return jdbc.queryForList(sql, params);
    }
    

    public List<Map<String, Object>> selectChulpanList(String userCetCd, String chooseDate) {
        String sql = """
            SELECT DISTINCT 
                d.CHULPAN_CD AS "custCd",
                c.CUST_NM    AS "custNm"
            FROM TMS_DUNGE_MST d
            JOIN TMS_CUST c
            ON d.CHULPAN_CD = c.CUST_CD
            WHERE d.DUNGE_DT IS NOT NULL
            AND d.DUNGE_DT = :chooseDate
            AND d.USER_CET_CD = :userCetCd
            ORDER BY d.CHULPAN_CD
        """;

        Map<String,Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("chooseDate", chooseDate);

        return jdbc.queryForList(sql, params);
    }


    
    // 그리드 리스트 목록 가져오기
    public List<TmsDungeMstVO> selectList(String dungeDt, String userCetCd, String chulgoGb, String chulpanCd, String cetCd) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                A.USER_CET_CD,
                A.ROW_SEQ,
                A.CET_CD,
                C.CUST_NM AS CHULPAN_NM,
                A.ORD_NO,
                A.SUJUM_CD,
                C2.CHULGO_GB,
                A.SUJUM_NM,
                A.JIYUK_NM,
                A.COURSE_CD,
                A.TEL_NO,
                A.DUNGE,
                A.CHUL_NM,
                A.QTY,
                A.BIGO,
                A.DUNGE_DT,
                A.OUT_YN,
                A.DAY_GB,
                A.TRANS_GB,
                A.INSERT_DT,
                A.DREAM_UPDATE,
                A.CHULPAN_CD,
                A.DAE_NO,
                A.INSERT_ID,
                A.UPDATE_DT,
                A.UPDATE_ID
            FROM TMS_DUNGE_MST A
            LEFT JOIN TMS_CUST C ON A.CHULPAN_CD = C.CUST_CD
            LEFT JOIN TMS_CUST C2 ON A.SUJUM_CD = C2.CUST_CD
            WHERE A.DUNGE_DT = :dungeDt
            AND A.USER_CET_CD = :userCetCd
            """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dungeDt", dungeDt)
                .addValue("userCetCd", userCetCd);

        if (chulgoGb != null && !chulgoGb.isEmpty()) {
            sql.append("""
                AND C2.CHULGO_GB = :chulgoGb
                """);
            params.addValue("chulgoGb", chulgoGb);
        }
 
        if (chulpanCd != null && !chulpanCd.isEmpty()) {
            sql.append("""
                AND A.CHULPAN_CD = :chulpanCd
                """);
            params.addValue("chulpanCd", chulpanCd);
        }

        if (cetCd != null && !cetCd.isEmpty()) {
            sql.append("""
                AND A.CET_CD = :cetCd
                """);
            params.addValue("cetCd", cetCd);
        }

        sql.append("""
            ORDER BY A.ROW_SEQ
            """);

        System.out.println(sql.toString());

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsDungeMstVO.class));
    }



    // 삭제
    @Transactional
    public void deleteOne(String userCetCd, String rowSeq, String ordNo) {
        String sql = """
            DELETE FROM TMS_DUNGE_MST
            WHERE USER_CET_CD = :userCetCd
               AND ROW_SEQ     = :rowSeq
               AND ORD_NO      = :ordNo
               AND (TRANS_GB IS NULL OR TRIM(TRANS_GB) <> '1')
        """;

        Map<String, Object> params = Map.of(
            "userCetCd", userCetCd,
            "rowSeq", rowSeq,
            "ordNo", ordNo
        );

        jdbc.update(sql, params);
    }


    //전송상태 배치 업데이트
    @Transactional
    public BatchResult transferBulk(TmsDungeTransferReq req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            return new BatchResult(0, 0, List.of());
        }

        final String sql = """
            UPDATE TMS_DUNGE_MST
            SET TRANS_GB  = '1',
                UPDATE_DT = SYSDATE,
                UPDATE_ID = :updateId
            WHERE USER_CET_CD = :userCetCd
            AND ROW_SEQ     = :rowSeq
            AND ORD_NO      = :ordNo
            AND (TRANS_GB IS NULL OR TRIM(TRANS_GB) <> '1')
            """;

        List<MapSqlParameterSource> params = req.getItems().stream().map(it ->
            new MapSqlParameterSource()
                .addValue("userCetCd", it.getUserCetCd())
                .addValue("rowSeq", it.getRowSeq())
                .addValue("ordNo", it.getOrdNo())
                .addValue("updateId", it.getUpdateId())
        ).toList();

        int[] counts = jdbc.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));

        int updated = 0, failed = 0;
        List<TmsDungeTransferItem> successKeys = new ArrayList<>();

        for (int i = 0; i < counts.length; i++) {
            int c = counts[i];
            boolean success = (c > 0) || (c == Statement.SUCCESS_NO_INFO); // -2도 성공으로 간주
            if (success) {
                updated++;
                successKeys.add(req.getItems().get(i));
            } else {
                // c == 0(매칭 없음) 또는 c == Statement.EXECUTE_FAILED(-3) 등은 실패 처리
                failed++;
            }
        }
        
        return new BatchResult(updated, failed, successKeys);
    }



    /*
    public byte[] buildLabelPdfAndMarkPrinted(LabelPrintRequest req) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 라벨 사이즈 50×30mm
        float widthPt  = mmToPt(50f);
        float heightPt = mmToPt(30f);
        Rectangle pageSize = new Rectangle(widthPt, heightPt);

        Document doc = new Document(pageSize, 6, 6, 6, 6); // 얇은 여백
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        // 한글 폰트 (경로/파일명은 프로젝트에 맞춰 두세요)
        BaseFont base = BaseFont.createFont(
            "src/main/resources/fonts/NanumGothic.ttf",
            BaseFont.IDENTITY_H,
            BaseFont.EMBEDDED
        );
        Font f10 = new Font(base, 10);
        Font f9  = new Font(base, 9);
        PdfContentByte cb = writer.getDirectContent();

        for (LabelItem item : req.getItems()) {
            for (int c = 0; c < Math.max(1, req.getCopiesPerItem()); c++) {
                // === Code128 바코드 (OpenPDF) ===
                Barcode128 code128 = new Barcode128();
                code128.setCode(item.getOrdNo());
                code128.setFont(null);
                Image barcodeImg = code128.createImageWithBarcode(cb, null, null);
                barcodeImg.scaleToFit(mmToPt(44), mmToPt(10));

                // === QR 코드 (ZXing) ===
                Image qrImg = createQrImage(item.getOrdNo(), 300, ErrorCorrectionLevel.M, 0);
                qrImg.scaleToFit(mmToPt(12), mmToPt(12));

                // === 레이아웃 ===
                PdfPTable table = new PdfPTable(new float[]{ 1f, 0.35f });
                table.setWidthPercentage(100);

                // 좌측: 바코드 + 거래처 코드/명
                PdfPTable left = new PdfPTable(1);
                left.setWidthPercentage(100);

                PdfPCell bcCell = new PdfPCell(barcodeImg, true);
                bcCell.setBorder(Rectangle.NO_BORDER);
                bcCell.setPaddingBottom(2);
                left.addCell(bcCell);

                Phrase p1 = new Phrase(item.getSujumCd() + "  " + safe(item.getSujumNm()), f10);
                PdfPCell textCell = new PdfPCell(p1);
                textCell.setBorder(Rectangle.NO_BORDER);
                textCell.setPaddingTop(0);
                left.addCell(textCell);

                PdfPCell leftCell = new PdfPCell(left);
                leftCell.setBorder(Rectangle.NO_BORDER);
                leftCell.setPaddingRight(4);

                // 우측: QR + ordNo
                PdfPTable right = new PdfPTable(1);
                right.setWidthPercentage(100);

                PdfPCell qrCell = new PdfPCell(qrImg, true);
                qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                qrCell.setBorder(Rectangle.NO_BORDER);
                right.addCell(qrCell);

                PdfPCell ordNoCell = new PdfPCell(new Phrase(item.getOrdNo(), f9));
                ordNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                ordNoCell.setBorder(Rectangle.NO_BORDER);
                ordNoCell.setPaddingTop(1f);
                right.addCell(ordNoCell);

                PdfPCell rightCell = new PdfPCell(right);
                rightCell.setBorder(Rectangle.NO_BORDER);

                table.addCell(leftCell);
                table.addCell(rightCell);

                doc.add(table);
                doc.newPage();
            }
        }

        doc.close();

        // 출력되었다면 OUT_YN=1 업데이트
        if (req.isUpdateOutYn() && req.getItems() != null && !req.getItems().isEmpty()) {
            List<Long> ids = req.getItems().stream().map(LabelItem::getRowSeq).toList();
            markPrinted(ids, req.getUpdateId(), req.getUserCetCd());
        }

        return baos.toByteArray();
    }
*/


        /** ZXing으로 QR 생성 → OpenPDF Image 변환 */
    private Image createQrImage(String text, int sizePx,
                                ErrorCorrectionLevel ecLevel, int margin) throws Exception {
        Map<EncodeHintType, Object> hints = Map.of(
            EncodeHintType.ERROR_CORRECTION, ecLevel,
            EncodeHintType.MARGIN, margin // 여백(quiet zone) 0~4 권장
        );

        BitMatrix matrix = new QRCodeWriter().encode(
            text, BarcodeFormat.QR_CODE, sizePx, sizePx, hints
        );

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return Image.getInstance(baos.toByteArray());
    }


    private void markPrinted(List<Long> rowSeqs, String updateId, String userCetCd) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("updateId", updateId)
                .addValue("userCetCd", userCetCd)
                .addValue("rowSeqs", rowSeqs);  // 컬렉션 그대로 넣기

        jdbc.update(SQL_MARK_PRINTED, params);
    }



    // 두번째행에 지역명 빠진 버전
    // public byte[] buildLabelPdfAndMarkPrinted(LabelPrintRequest req) throws Exception {
    //     if (req.getItems() == null || req.getItems().isEmpty()) {
    //         throw new IllegalArgumentException("items is empty");
    //     }

    //     ByteArrayOutputStream baos = new ByteArrayOutputStream();

    //     // 라벨 사이즈 50×30mm
    //     float widthPt  = mmToPt(60f);
    //     float heightPt = mmToPt(40f);
    //     Rectangle pageSize = new Rectangle(widthPt, heightPt);

    //     Document doc = new Document(pageSize, 6, 6, 6, 6); // 얇은 여백
    //     PdfWriter writer = PdfWriter.getInstance(doc, baos);
    //     doc.open();

    //     // 한글 폰트 (classpath:/fonts/NanumGothic.ttf) - byte[] 로딩 캐시 사용
    //     BaseFont base = loadBaseFontCached();
    //     Font f12b = new Font(base, 12, Font.BOLD);
    //     Font f11b = new Font(base, 11, Font.BOLD);
    //     Font f10  = new Font(base, 10);

    //     PdfContentByte cb = writer.getDirectContent();

    //     // 전체 라벨 매수(페이지 수) 계산
    //     int totalLabels = 0;
    //     for (LabelItem li : req.getItems()) {
    //         int copies = (li.getDunge() == null || li.getDunge() < 1) ? 1 : li.getDunge();
    //         totalLabels += copies;
    //     }

    //     int printed = 0;

    //     for (LabelItem item : req.getItems()) {
    //         int copies = (item.getDunge() == null || item.getDunge() < 1) ? 1 : item.getDunge();

    //         for (int c = 0; c < copies; c++) {
    //             // ── 바코드 (짧게) ─────────────────────────────────────
    //             Barcode128 code128 = new Barcode128();
    //             code128.setCode(item.getOrdNo());
    //             code128.setCodeType(Barcode128.CODE128);
    //             code128.setFont(null);                 // 숫자 숨김
    //             code128.setBarHeight(mmToPt(8));       // 높이 낮춤 = 더 짧게
    //             code128.setX(0.75f);                   // 막대 폭(가로 길이도 함께 단축)

    //             Image barcodeImg = code128.createImageWithBarcode(cb, null, null);
    //             // 좌측 하단 영역에 깔끔히 들어가도록 최대 크기 제한
    //             barcodeImg.scaleToFit(mmToPt(28), mmToPt(10));

    //             // ── 메인 2열 테이블 (오른쪽 넓게) ─────────────────────
    //             PdfPTable table = new PdfPTable(new float[]{ 0.60f, 0.40f }); // 좌:60% / 우:40%
    //             table.setWidthPercentage(100);
    //             table.setKeepTogether(true);  // 라벨 분할 금지
    //             table.setSplitRows(false);
    //             table.setSplitLate(false);
    //             table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

    //             // 1행: 서점명 | 코스번호
    //             PdfPCell sujumCell = new PdfPCell(new Phrase(safe(item.getSujumNm()), f12b));
    //             sujumCell.setBorder(Rectangle.NO_BORDER);
    //             sujumCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    //             sujumCell.setPaddingBottom(2f);

    //             String courseText = (item.getCourseCd() == null) ? "" : String.valueOf(item.getCourseCd());
    //             PdfPCell courseCell = new PdfPCell(new Phrase(courseText, f12b));
    //             courseCell.setBorder(Rectangle.NO_BORDER);
    //             courseCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    //             courseCell.setPaddingBottom(2f);

    //             table.addCell(sujumCell);
    //             table.addCell(courseCell);

    //             // 2행: 덩이 | 수량
    //             String dungeText = (item.getDunge() == null) ? "" : String.valueOf(item.getDunge());
    //             PdfPCell dungeCell = new PdfPCell(new Phrase(dungeText, f11b));
    //             dungeCell.setBorder(Rectangle.NO_BORDER);
    //             dungeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    //             dungeCell.setPaddingBottom(2f);

    //             String qtyText = (item.getQty() == null) ? "" : String.valueOf(item.getQty());
    //             PdfPCell qtyCell = new PdfPCell(new Phrase(qtyText, f11b));
    //             qtyCell.setBorder(Rectangle.NO_BORDER);
    //             qtyCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    //             qtyCell.setPaddingBottom(2f);

    //             table.addCell(dungeCell);
    //             table.addCell(qtyCell);

    //             // 3행: 바코드 | 거래처명
    //             PdfPCell barcodeCell = new PdfPCell(barcodeImg, true);
    //             barcodeCell.setBorder(Rectangle.NO_BORDER);
    //             barcodeCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    //             barcodeCell.setPaddingTop(2f);

    //             PdfPCell chulCell = new PdfPCell(new Phrase(safe(item.getChulpanNm()), f10)); // DTO 필드명 chulNm 기준
    //             chulCell.setBorder(Rectangle.NO_BORDER);
    //             chulCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    //             chulCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    //             chulCell.setPaddingTop(2f);
    //             chulCell.setNoWrap(false); // 길면 줄바꿈 허용

    //             table.addCell(barcodeCell);
    //             table.addCell(chulCell);

    //             // 라벨 1장 추가
    //             doc.add(table);

    //             printed++;
    //             if (printed < totalLabels) {
    //                 doc.newPage(); // 마지막 장 뒤에는 페이지 넘기지 않음
    //             }
    //         }
    //     }

    //     doc.close();

    //     // OUT_YN=1 업데이트
    //     if (req.isUpdateOutYn()) {
    //         List<Long> ids = new ArrayList<>();
    //         for (LabelItem li : req.getItems()) {
    //             if (li.getRowSeq() != null) ids.add(li.getRowSeq());
    //         }
    //         if (!ids.isEmpty()) {
    //             markPrinted(ids, req.getUpdateId(), req.getUserCetCd());
    //         }
    //     }

    //     return baos.toByteArray();
    // }





    public byte[] buildLabelPdfAndMarkPrinted(LabelPrintRequest req) throws Exception {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("items is empty");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 라벨 사이즈 50×30mm
        final float widthPt  = mmToPt(50f);   // ≈ 141.73pt
        final float heightPt = mmToPt(30f);   // ≈ 85.04pt
        final Rectangle pageSize = new Rectangle(widthPt, heightPt);

        // 얇은 여백
        final float mL = 6f, mR = 6f, mT = 6f, mB = 6f;

        Document doc = new Document(pageSize, mL, mR, mT, mB);
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        // 폰트 (classpath:/fonts/NanumGothic.ttf)
        BaseFont base = loadBaseFontCached();
        Font f12b = new Font(base, 12, Font.BOLD);
        Font f11b = new Font(base, 11, Font.BOLD);
        Font f10  = new Font(base, 10);

        PdfContentByte cb = writer.getDirectContent();

        // 가용 영역
        final float contentW   = pageSize.getWidth()  - mL - mR;            // ≈ 129.7pt
        final float contentTop = pageSize.getHeight() - mT;                 // y 기준
        final float contentH   = pageSize.getHeight() - mT - mB;            // ≈ 73.0pt

        // 👉 행높이를 유효 높이에서 비율로 계산 (총합 = contentH)
        final float rowH1 = contentH * 0.32f;                               // 1행: 서점명 | 코스번호 (~23pt)
        final float rowH2 = contentH * 0.20f;                               // 2행: 덩이 | 수량 | 지역 (~14.6pt)
        final float rowH3 = contentH - rowH1 - rowH2;                       // 3행: 바코드 | 출판사명 (~35pt)

        // 전체 출력 매수
        int total = 0;
        for (LabelItem li : req.getItems()) {
            int copies = (li.getDunge() == null || li.getDunge() < 1) ? 1 : li.getDunge();
            total += copies;
        }
        int printed = 0;

        for (LabelItem item : req.getItems()) {
            int copies = (item.getDunge() == null || item.getDunge() < 1) ? 1 : item.getDunge();

            for (int k = 0; k < copies; k++) {
                // ── 바코드(짧게) ─────────────────────────────────────
                Barcode128 code128 = new Barcode128();
                code128.setCode(item.getOrdNo());
                code128.setCodeType(Barcode128.CODE128);
                code128.setFont(null);                 // 아랫글자 숨김
                code128.setBarHeight(mmToPt(7));       // 더 낮게 (≈ 19.9pt)
                code128.setX(0.70f);                   // 막대 얇게 → 가로길이도 단축
                Image barcodeImg = code128.createImageWithBarcode(cb, null, null);

                // 폭과 높이 조정 (조금 더 여유 있게 줄임)
                barcodeImg.scaleToFit(mmToPt(22), rowH3 - 6f); // 원래 26 → 22mm로 줄임

                // [행1] 2열: 서점명 | 코스번호
                PdfPTable row1 = new PdfPTable(new float[]{ 0.55f, 0.45f });
                row1.setTotalWidth(contentW); row1.setLockedWidth(true);
                row1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                PdfPCell sujumCell = new PdfPCell(new Phrase(safe(item.getSujumNm()), f12b));
                sujumCell.setBorder(Rectangle.NO_BORDER);
                sujumCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                sujumCell.setPadding(0.5f);

                String courseText = (item.getCourseCd() == null) ? "" : String.valueOf(item.getCourseCd());
                PdfPCell courseCell = new PdfPCell(new Phrase(courseText, f12b));
                courseCell.setBorder(Rectangle.NO_BORDER);
                courseCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                courseCell.setPadding(0.5f);

                row1.addCell(sujumCell);
                row1.addCell(courseCell);

                // [행2] 3열: 덩이 | 수량 | 지역명
                PdfPTable row2 = new PdfPTable(new float[]{ 0.25f, 0.25f, 0.50f });
                row2.setTotalWidth(contentW); row2.setLockedWidth(true);
                row2.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                PdfPCell dungeCell = new PdfPCell(new Phrase(
                        item.getDunge() == null ? "" : String.valueOf(item.getDunge()), f11b));
                dungeCell.setBorder(Rectangle.NO_BORDER);
                dungeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                dungeCell.setPadding(0.5f);

                PdfPCell qtyCell = new PdfPCell(new Phrase(
                        item.getQty() == null ? "" : String.valueOf(item.getQty()), f11b));
                qtyCell.setBorder(Rectangle.NO_BORDER);
                qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                qtyCell.setPadding(0.5f);

                PdfPCell regionCell = new PdfPCell(new Phrase(safe(item.getJiyukNm()), f11b));
                regionCell.setBorder(Rectangle.NO_BORDER);
                regionCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                regionCell.setPadding(0.5f);

                row2.addCell(dungeCell);
                row2.addCell(qtyCell);
                row2.addCell(regionCell);

                // [행3] 2열: 바코드 | 출판사명
                PdfPTable row3 = new PdfPTable(new float[]{ 0.55f, 0.45f });
                row3.setTotalWidth(contentW); row3.setLockedWidth(true);
                row3.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                PdfPCell barcodeCell = new PdfPCell(barcodeImg, true);
                barcodeCell.setBorder(Rectangle.NO_BORDER);
                barcodeCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                barcodeCell.setPadding(0f);
                barcodeCell.setPaddingRight(5f); // 바코드와 출판사명 사이 간격 추가

                PdfPCell chulCell = new PdfPCell(new Phrase(safe(item.getChulpanNm()), f10));
                chulCell.setBorder(Rectangle.NO_BORDER);
                chulCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                chulCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                chulCell.setNoWrap(false);
                chulCell.setPadding(0.5f);
                chulCell.setPaddingLeft(3f); // 바코드쪽에서 여백 더 확보

                row3.addCell(barcodeCell);
                row3.addCell(chulCell);

                // 바깥 1열에 담아 고정 높이로 구성(합계 = contentH)
                PdfPTable outer = new PdfPTable(1);
                outer.setTotalWidth(contentW); outer.setLockedWidth(true);
                outer.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                PdfPCell c1 = new PdfPCell(row1); c1.setBorder(Rectangle.NO_BORDER); c1.setFixedHeight(rowH1);
                PdfPCell c2 = new PdfPCell(row2); c2.setBorder(Rectangle.NO_BORDER); c2.setFixedHeight(rowH2);
                PdfPCell c3 = new PdfPCell(row3); c3.setBorder(Rectangle.NO_BORDER); c3.setFixedHeight(rowH3);

                outer.addCell(c1);
                outer.addCell(c2);
                outer.addCell(c3);

                // 절대 좌표 렌더링 → 페이지 분리 없음
                outer.writeSelectedRows(0, -1, mL, contentTop, cb);

                printed++;
                if (printed < total) doc.newPage();
            }

        }

        doc.close();

        if (req.isUpdateOutYn()) {
            List<Long> ids = new ArrayList<>();
            for (LabelItem li : req.getItems()) if (li.getRowSeq() != null) ids.add(li.getRowSeq());
            if (!ids.isEmpty()) markPrinted(ids, req.getUpdateId(), req.getUserCetCd());
        }

        return baos.toByteArray();
    }

    
    public List<TmsDungeMstVO> selectListByOrdNos(String userCetCd, List<String> ordNos) {
        String sql = """
                    SELECT * FROM TMS_DUNGE_MST
                    WHERE USER_CET_CD = :userCetCd
                    AND ORD_NO IN (:ordNos)
                    ORDER BY ORD_NO
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("ordNos", ordNos);

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(TmsDungeMstVO.class));
    }


    public String selectCustNmByCetCd(String custCd, String userCetCd) {
        String sql = "SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = :custCd AND USER_CET_CD = :userCetCd";
        Map<String, Object> params = Map.of("custCd", custCd, "userCetCd", userCetCd);
        return jdbc.queryForObject(sql, params, String.class);
    }





}
