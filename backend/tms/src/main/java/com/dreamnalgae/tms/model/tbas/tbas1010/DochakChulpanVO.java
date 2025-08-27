package com.dreamnalgae.tms.model.tbas.tbas1010;

import java.util.Date;

import lombok.Data;

@Data
public class DochakChulpanVO {
    private String userCetCd;          // USER_CET_CD
    private String sujumCd;            // SUJUM_CD
    private String chulpanCd;          // CHULPAN_CD
    private String chulpanNm;
    private String chulpanGb;          // CHULPAN_GB
    private String chulpanBasicGb;     // CHULPAN_BASIC_GB
    private Integer chulpanBasicDanga; // CHULPAN_BASIC_DANGA (NUMBER)
    private String chulpanBigo;        // CHULPAN_BIGO
    private Date insertDt;             // INSERT_DT
    private String insertId;           // INSERT_ID
    private Date updateDt;             // UPDATE_DT
    private String updateId;           // UPDATE_ID
    private Integer chulpanBasicQty;   // CHULPAN_BASIC_QTY (NUMBER)    
}
