package com.dreamnalgae.tms.model.treo.treo1003;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderRetnMstVO {
    private String userCetCd;         // 사용자 구분 코드
    private String ordRetnNo;         // 반품 번호
    private Date ordIbgoRegDt;   // 입고 등록일
    private String custIbgoNo;        // 고객 입고 번호
    private String ordIbgoScanNo;     // 입고 스캔 번호
    private String ordRetnDt;      // 반품일
    private String ordGuljaeGb;       // 굴재 구분
    private String chulpanCd;         // 출판사 코드
    private String sujumCd;           // 서점 코드
    private String chulpanNm;         // 출판사명
    private String sujumNm;           // 서점명
    private Long ordQty;              // 주문 수량
    private Long ordBox;              // 박스 수
    private String regiUserId;        // 등록 사용자
    private String delivCd;           // 배송 코드
    private String delivPathCd;       // 배송 경로 코드
    private String transGb;           // 운송 구분
    private BigDecimal unsongExpAmt;  // 운송비 예상 금액
    private String bigo;              // 비고
    private Date insertDt;       // 등록일
    private String insertId;          // 등록자
    private Date updateDt;       // 수정일
    private String updateId;          // 수정자
}
