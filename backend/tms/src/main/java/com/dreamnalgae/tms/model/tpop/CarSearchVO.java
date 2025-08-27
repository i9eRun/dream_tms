package com.dreamnalgae.tms.model.tpop;

import java.sql.Date;

import lombok.Data;

@Data
public class CarSearchVO {
    private String keyword;
    private String userCetCd;
    private String carCd;
    private String carRegnum;
    private String carKind;
    private String carUses;
    private String carNm;
    private String carChulYear;
    private String userId;
    private String userNm;
    private String carChadaenum;
    private String carJuhYear;
    private String carInwon;
    private String carComp;
    private String carPurDt;
    private String carPurway;
    private Integer carPurmoney;
    private String carChkDt;
    private String carYoungDt;
    private String carBaeDt;
    private String carInsuname;
    private Integer carChainsu;
    private Integer carTotinsu;
    private String carTon;
    private String carLoadage;
    private String carGubun;
    private String carAmpm;
    private String coursegubun;
    private Date insertDt;
    private String insertId;
    private Date updateDt;
    private String updateId;
}
