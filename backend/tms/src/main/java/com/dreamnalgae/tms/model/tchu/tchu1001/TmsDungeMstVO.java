package com.dreamnalgae.tms.model.tchu.tchu1001;

import java.util.Date;

import lombok.Data;

@Data
public class TmsDungeMstVO {
    private String userCetCd;     // USER_CET_CD
    private Long rowSeq;          // ROW_SEQ
    private String chulpanCd;     // CHULPAN_CD
    private String cetCd;         // CET_CD
    private String chulpanNm;     
    private String ordNo;         // ORD_NO
    private String sujumCd;       // SUJUM_CD
    private String sujumNm;       // SUJUM_NM
    private String chulgoGb;      // CHULGO_GB
    private String jiyukNm;       // JIYUK_NM
    private Long courseCd;        // COURSE_CD
    private String telNo;         // TEL_NO
    private Long dunge;           // DUNGE
    private String chulNm;        // CHUL_NM
    private Long qty;             // QTY
    private String bigo;          // BIGO
    private String dungeDt;       // DUNGE_DT (YYYYMMDD 문자열)
    private String outYn;         // OUT_YN
    private String dayGb;         // DAY_GB
    private String transGb;       // TRANS_GB
    private Date insertDt;        // INSERT_DT
    private String dreamUpdate;   // DREAM_UPDATE
    private String daeNo;         // DAE_NO
    private String insertId;      // INSERT_ID
    private Date updateDt;        // UPDATE_DT
    private String updateId;      // UPDATE_ID
}
