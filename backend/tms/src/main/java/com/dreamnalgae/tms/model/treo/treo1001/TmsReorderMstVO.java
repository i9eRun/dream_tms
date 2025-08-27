package com.dreamnalgae.tms.model.treo.treo1001;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TmsReorderMstVO {
    private String userCetCd;
    private String reordNo;
    private String preOrdNo;
    private String reordGb;
    private String reordDt;
    private String chkGb;
    private String chulpanCd;
    private String chulpanNm;
    private String sujumCd;
    private String sujumNm;
    private Integer reordCnt;
    private String reordReqId;
    private String reordAppId;
    private BigDecimal reordQty;
    private BigDecimal reordBox;
    private BigDecimal orderBox;
    private BigDecimal boxCount;
    private String reordDamdangId;
    private String reordJDamdangId;
    private String bigo;
    private String reordFaultGb;
    private String reordReasonGb;
    private String cusCd;
    private String cusDt;
    private String reordJaEcheck;
    private String reordOcheck;
    private String reordJuncheck;
    private String reordEndcheck;
    private String reordSeq;
    private String reordSagogu;
    private BigDecimal totalBox;
    private BigDecimal reordOrder;
    private String reordNalgae;
    private String reordNalgaeChk;
    private String parentCustCd;
    private Date insertDt;
    private String insertId;
    private Date updateDt;
    private String updateId;
}
