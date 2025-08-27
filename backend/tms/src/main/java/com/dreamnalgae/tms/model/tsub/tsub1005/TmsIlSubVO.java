package com.dreamnalgae.tms.model.tsub.tsub1005;

import java.util.Date;

import lombok.Data;

@Data
public class TmsIlSubVO {
    private String userCetCd;             // USER_CET_CD
    private String cusDt;                 // CUS_DT
    private String cusCd;                 // CUS_CD
    private Integer subSunbun;           // SUB_SUNBUN
    private String custCd;                // CUST_CD
    private String subGb;                 // SUB_GB
    private String subWork;               // SUB_WORK
    private String subWorkGb;            // SUB_WORK_GB
    private String subCancel;            // SUB_CANCEL
    private Date subDeptTime;   // SUB_DEPT_TIME
    private Date subArriveTime; // SUB_ARRIVE_TIME
    private String subWorkTime;          // SUB_WORK_TIME
    private Date subMoveTime;   // SUB_MOVE_TIME
    private String custGb;                // CUST_GB
    private String subIkwan;              // SUB_IKWAN
    private String subIkwanCd;            // SUB_IKWAN_CD
    private String subIkwanNm;            // SUB_IKWAN_NM
    private String subYcGb;               // SUB_YC_GB
    private String subMemo;               // SUB_MEMO
    private String subCancelMemo;         // SUB_CANCEL_MEMO
    private String subInitalingSign;      // SUB_INITALING_SIGN
    private String subCustSign;           // SUB_CUST_SIGN
    private String subSignGb;             // SUB_SIGN_GB
    private String initalingSignGb;       // INITALING_SIGN_GB
    private Integer gainsuQty;           // GAINSU_QTY
    private Integer gainsuBox;           // GAINSU_BOX
    private Date insertDt;      // INSERT_DT
    private String insertId;              // INSERT_ID
    private Date updateDt;      // UPDATE_DT
    private String updateId;              // UPDATE_ID
    private String cusNm;              // UPDATE_ID
    private String userId;              // UPDATE_ID
    private String userNm;              // UPDATE_ID
    private String qty;              // UPDATE_ID
    private String box;              // UPDATE_ID
}
