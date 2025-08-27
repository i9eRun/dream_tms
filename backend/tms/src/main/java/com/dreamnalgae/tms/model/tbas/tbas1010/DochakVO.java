package com.dreamnalgae.tms.model.tbas.tbas1010;

import java.util.Date;

import lombok.Data;

@Data
public class DochakVO {
    private String userCetCd;          // USER_CET_CD
    private String sujumCd;            // SUJUM_CD
    private String custNm;
    private String telNo;
    private String jiyukNm;
    private String custAbbrNm;
    private String dochakGb;           // DOCHAK_GB
    private Integer dochakOverQty;     // DOCHAK_OVER_QTY
    private String dochakBasicGb;      // DOCHAK_BASIC_GB
    private Integer dochakBasicDanga;  // DOCHAK_BASIC_DANGA
    private String dochakBigo;         // DOCHAK_BIGO
    private Date insertDt;             // INSERT_DT
    private String insertId;           // INSERT_ID
    private Date updateDt;             // UPDATE_DT
    private String updateId;           // UPDATE_ID
}
