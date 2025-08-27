package com.dreamnalgae.tms.model.tsub.tsub1004;

import lombok.Data;

@Data
public class TmsBaechaVO {
    private String userCetCd;
    private String cusDt;
    private String cusCd;
    private String cusNm;
    private String userId;
    private String userNm;
    private String telNo;
    private String carCd;
    private String carRegnum;
    private String insertId;
    private String insertDt;
    private String updateId;
    private String updateDt;

    //조회조건
    private String cusGb;
    private String cusAmpmGb;
}
