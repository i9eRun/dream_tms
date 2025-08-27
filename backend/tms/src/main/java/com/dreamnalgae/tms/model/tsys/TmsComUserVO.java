package com.dreamnalgae.tms.model.tsys;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TmsComUserVO {
    private String userId;
    private String userCetCd;
    private String userNm;
    @JsonProperty("nDeptCd")
    private String nDeptCd;
    private String deptNm;
    private String jikweeCd;
    private String jikchkCd;
    private String jaejikGb;
    private Date enterDt;
    private Date retireDt;
    private String jikweeNm;
    private String extTelNo;
    private String passWd;
    private String userLevelGb;
    private String userAuthRw;
    private String chulpanCd;
    private String chulpanNm;
    private String insUserId;
    private String infUserId;
    private String telNo;
    private String faxNo;
    private String emailAddr;
    private String userChkGb;
    private String banpInsId;
    private String userAuthAdm;
    private String useYn;
    private String bigo;
    private String empId;
    private String userIdOld;
    private Date insertDt;
    private String insertId;
    private Date updateDt;
    private String updateId;
    private String jiyukCd;
    private Date lastLoginTime;
    private String telNo1;
    private String telNo2;
    private String telNo3;
    private Date jikchkDt;
    private String geunmooJo;
    private String lastLoginAddr;
    private String deptCd;
    private String pdaNo;
    private Date pdaBulDt;
    private Date pdaBanDt;
    private Date winUniformBulDt;
    private Date winUniformBanDt;
    private Date sumUniformBulDt;
    private Date sumUniformBanDt;

       
}
