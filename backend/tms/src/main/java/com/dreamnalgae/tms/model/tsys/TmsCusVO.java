package com.dreamnalgae.tms.model.tsys;

import java.util.Date;

import lombok.Data;

@Data
public class TmsCusVO {
    private String userCetCd;     // 사용자 구분 코드
    private String cusCd;         // 거래처 코드
    private String carCd;         // 차량 코드
    private String carNm;         // 차량명
    private String carRegnum;     // 차량번호
    private String cusNm;         // 거래처 명
    private String cusAmpm;       // 오전/오후 여부
    private String cusGb;         // 구분
    private String cusUse;        // 사용 여부
    private String userId;        // 등록 사용자 ID
    private String userNm;        // 등록 사용자 이름
    private String cusAmpmGb;     // 오전/오후 구분
    private String cusBonsa;      // 본사 여부
    private Date insertDt;        // 등록일시
    private String insertId;      // 등록자
    private Date updateDt;        // 수정일시
    private String updateId;      // 수정자
    private String cusTmsGb;      // TMS 구분
    private String parentCusCd;   // 상위 거래처 코드
}
