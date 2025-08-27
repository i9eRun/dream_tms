package com.dreamnalgae.tms.model.tbas;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TmsDeptVO {
    @JsonProperty("N_DEPT_CD")
    private String nDeptCd;
    
    @JsonProperty("USER_CET_CD")
    private String userCetCd;

    @JsonProperty("DEPT_NM")
    private String deptNm;

    @JsonProperty("JIYUK_CD")
    private String jiyukCd;

    @JsonProperty("DEPT_CD_OLD")
    private String deptCdOld;

    @JsonProperty("DEPT_NM_OLD")
    private String deptNmOld;

    @JsonProperty("DEPT_GWA_NM_OLD")
    private String deptGwaNmOld;

    @JsonProperty("DEPT_BAN_NM_OLD")
    private String deptBanNmOld;

    @JsonProperty("DEPT_CHO_NM_OLD")
    private String deptChoNmOld;

    @JsonProperty("DEPT_ACC_CD")
    private String deptAccCd;

    @JsonProperty("TEAM_HEAD_NM_OLD")
    private String teamHeadNmOld;

    @JsonProperty("USE_YN")
    private String useYn;

    @JsonProperty("TEAM_NM")
    private String teamNm;

    @JsonProperty("OLD_TEAM_NM")
    private String oldTeamNm;

    @JsonProperty("TEAM_GB")
    private String teamGb;

    @JsonProperty("MULRYU_GB")
    private String mulryuGb;

    @JsonProperty("ARANGE_SEQ1")
    private Integer arangeSeq1;

    @JsonProperty("ARANGE_SEQ2")
    private Integer arangeSeq2;

    @JsonProperty("NEW_TEAM_ABBR_NM")
    private String newTeamAbbrNm;

    @JsonProperty("NEW_TEAM_NM")
    private String newTeamNm;

    @JsonProperty("NEW_CHO_NM")
    private String newChoNm;

    @JsonProperty("NEW_TEAM_HEAD_NM")
    private String newTeamHeadNm;

    @JsonProperty("JO_HEAD_NM")
    private String joHeadNm;

    @JsonProperty("TOT_HEAD_NM")
    private String totHeadNm;

    @JsonProperty("MGR_HEAD_NM")
    private String mgrHeadNm;

    @JsonProperty("INSA_DEPT_CD_OLD")
    private String insaDeptCdOld;

    @JsonProperty("INSERT_DT")
    private Date insertDt;

    @JsonProperty("INSERT_ID")
    private String insertId;

    @JsonProperty("UPDATE_DT")
    private Date updateDt;

    @JsonProperty("UPDATE_ID")
    private String updateId;

    @JsonProperty("NEW_TEAM_HEAD_ID")
    private String newTeamHeadId;

    @JsonProperty("JO_HEAD_ID")
    private String joHeadId;

    @JsonProperty("TOT_HEAD_ID")
    private String totHeadId;

    @JsonProperty("MGR_HEAD_ID")
    private String mgrHeadId;

    @JsonProperty("JAES_DV_AMT")
    private Double jaesDvAmt;

    @JsonProperty("USER_CNT")
    private Integer userCnt;

    @JsonProperty("DEPT_CD")
    private String deptCd;
}
