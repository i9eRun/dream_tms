package com.dreamnalgae.tms.model.tsys;

import java.util.Date;

import lombok.Data;

@Data
public class TmsGroupMVO {
    private String userCetCd;     // 회사코드
    private String groupCd;       // 그룹코드
    private String groupNm;       // 그룹명
    private String bigo;          // 비고
    private String useYn;         // 사용여부
    private Date insertDt;        // 등록일
    private String insertId;      // 등록자
    private Date updateDt;        // 수정일
    private String updateId;      // 수정자
}
