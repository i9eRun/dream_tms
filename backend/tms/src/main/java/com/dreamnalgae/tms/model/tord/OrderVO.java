package com.dreamnalgae.tms.model.tord;

import java.util.Date;

import lombok.Data;

@Data
public class OrderVO {
    private String userCetCd;
    private String cetCd;
    private String ordNo;
    private String ordPoNo;
    private String ordDt;
    private Integer ordAmt;
    private int boxQty;
    private String chulpanCd;
    private String sujumCd;
    private String ordDivGb;
    private String chulgoGb;
    private String guljaeGb;
    private String ordInputId;
    private String ordChkGb;
    private String chulgoDt;
    private String slipGb;
    private Date unsongDt;
    private String unsongExpAmt;
    private String unsongBigo;
    private String bigo;
    private Date labelPrtDt;
    private String labelPrtId;
    private String unsongRecvDt;
    private String unsongRecvId;
    private String singanGb;
    private String takbaeTelNo;
    private String takbaeCustNm;
    private String takbaePostNo;
    private String takbaeAddr;
    private String takbaeBigo;
    private String oldLabelPrtId;
    private String oldBoxQty;
    private String oldLabelPrtDt;
    private String ordItmQty;
    private String ordWeight;
    private String transGb;
    private String ordRegiTime;
    private int bokQty;
    private String boxSumQty;
    private String ordSheetDt;
    private String jegoJungsanYn;
    private String takbaeInvoiceNo;
    private String saleYn;
    private String storeCd;
    private String specPrtCnt;
    private String delivCd;
    private String delivPathCd;
    private String chulgoTeamCd;
    private String chulgoTeamNm;
    private String chulgoDamdangNm;
    private String takbaeCpTelNo;
    private String chulpanGb;
    private String chulpanOrdDt;
    private String ordCnt;
    private String omsRowNo;
    private String ordNoChulpan;
    private String jisiNo;
    private String jisiUserId;
    private String itemGb;
    private String orgOrdNo;
    private String insertId;
    private Date insertDt;
    private String updateId;
    private Date updateDt;
    private int ordQty;
    private int ordBox;

    // 출판사, 서점, 주문자 정보
    private String chulpanNm;
    private String chulpanJiyukNm;
    private String sujumNm;
    private String sujumJiyukNm;
    private String ordInputNm;

    private int rnum;
}
