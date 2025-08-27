package com.dreamnalgae.tms.model.tbas.tbas1007;

import java.util.Date;

import lombok.Data;

@Data
public class HolidayVO {
    private String userCetCd;   // 사용자대행코드
    private String dday;        // 일자 (YYYYMMDD)
    private String jibangGb;    // 지방구분 (0=휴일, 1=영업)
    private String sinaeGb;     // 시내구분 (0=휴일, 1=영업)
    private String ddayYo;      // 요일
    private String bigo;        // 구분(비고)    

    private Date insertDt;      // 입력일자
    private String insertId;    // 입력자ID
    private Date updateDt;      // 수정일자
    private String updateId;    // 수정자ID

    // 조회용
    private String startDate;
    private String endDate;

}
