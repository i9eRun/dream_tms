package com.dreamnalgae.tms.model.treo.treo1004;

import java.util.Date;

import lombok.Data;

@Data
public class TmsJaechulgoVO {
    private String userCetCd;             // USER_CET_CD
    private String jaechulgoNo;           // JAECHULGO_NO
    private String preOrdNo;              // PRE_ORD_NO
    private String jaechulgoGb;           // JAECHULGO_GB
    private String jaechulgoDt;           // JAECHULGO_DT
    private String chkGb;                 // CHK_GB
    private String chulpanCd;             // CHULPAN_CD
    private String sujumCd;               // SUJUM_CD
    private String chulpanNm;
    private String sujumNm;
    private Integer jaechulgoCnt;         // JAECHULGO_CNT
    private String jaechulgoReqId;        // JAECHULGO_REQ_ID
    private String jaechulgoAppId;        // JAECHULGO_APP_ID
    private Long jaechulgoQty;            // JAECHULGO_QTY
    private Long jaechulgoBox;            // JAECHULGO_BOX
    private Long orderBox;                // ORDER_BOX
    private Long boxCount;                // BOX_COUNT
    private String jaechulgoDamdangId;    // JAECHULGO_DAMDANG_ID
    private String jaechulgoJDamdangId;   // JAECHULGO_J_DAMDANG_ID
    private String bigo;                  // BIGO
    private String jaechulgoFaultGb;      // JAECHULGO_FAULT_GB
    private String jaechulgoReasonGb;     // JAECHULGO_REASON_GB
    private String cusCd;                 // CUS_CD
    private String cusDt;                 // CUS_DT
    private String jaechulgoJaecheck;     // JAECHULGO_JAECHECK
    private String jaechulgoOcheck;       // JAECHULGO_OCHECK
    private String jaechulgoJuncheck;     // JAECHULGO_JUNCHECK
    private String jaechulgoEndcheck;     // JAECHULGO_ENDCHECK
    private String jaechulgoSeq;          // JAECHULGO_SEQ
    private String jaechulgoSagogu;       // JAECHULGO_SAGOGU
    private Long totalBox;                // TOTAL_BOX
    private Long jaechulgoOrder;          // JAECHULGO_ORDER
    private String jaechulgoNalgae;       // JAECHULGO_NALGAE
    private String jaechulgoNalgaeChk;    // JAECHULGO_NALGAE_CHK
    private String parentCustCd;          // PARENT_CUST_CD
    private Date insertDt;                // INSERT_DT
    private String insertId;              // INSERT_ID
    private Date updateDt;                // UPDATE_DT
    private String updateId;              // UPDATE_ID
}
