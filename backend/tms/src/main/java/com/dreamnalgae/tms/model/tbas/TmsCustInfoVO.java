package com.dreamnalgae.tms.model.tbas;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TmsCustInfoVO {
    private String userCetCd;
    private String tradeEndYn;
    private String custCd;
    private String custGb;
    private String custDivGb;
    private String custAbbrNm;
    private String custNm;
    private String bizRegNo;
    private String daepyoNm;
    private String uptaeNm;
    private String jongmokNm;
    private String telNo;
    private String telNo2;
    private String faxNo;
    private String emailAddr;
    private String postNo;
    private String addr1;
    private String addr2;
    private String tradeFrDt;
    private String tradeToDt;
    private BigDecimal sinyongAmt;
    private BigDecimal misuAmt;
    private String magamDd;
    private String contrStoreYn;
    private String jiyukNm;
    private String delivPathCd;
    private String delivPosCd;
    private String expressDelivPathCd;
    private String chargeEmpId;
    private String bigo;
    private String dreamBigo;
    private String saleTypeGb;
    private String guljaeGb;
    private String chulgoGb;
    private String sosokNm;
    private String banpomYn;
    private String custDivCd1;
    private String custDivCd2;
    private String billYn;
    private String dmSendYn;
    private String bizNm;
    private String teamCd;
    private String teamNm;
    private String damdangNm;
    private String delivDivCd;
    private String bunjiPrtYn;
    private String sendEmailAddr;
    private String workUserId;
    private String workDt;
    private String useYn;
    private String banpDamdangNm;
    private String banpTeamCd;
    private String banpTeamNm;
    private String ordMethodGb;
    private String ordMethodGbD;
    private BigDecimal bokIniBokwanAmt;
    private String chunguAmtPrtYn;
    private String chulpanSubCd;
    private String banpEmailAddr;
    private String bonsaSaleNm;
    private String boxAutoYn;
    private String homepageAddr;
    private String homenameCustNm;
    private String homepageUserId;
    private String homepageYn;
    private String baebonCd;
    private BigDecimal squareSize;
    private BigDecimal garoSize;
    private BigDecimal seroSize;
    private BigDecimal depthSize;
    private String recvPostNo;
    private String recvAddr1;
    private String recvDamdangNm;
    private String recvNickNm;
    private String webOrdGb;
    private String chulgoSortGb;
    private String chunguDamdangNm;
    private String chunguNickNm;
    private Integer jakupIndCnt;
    private String boxBokQtyYn;
    private String bokWmsCnvYn;
    private String takbaeCd;
    private String parentCustCd;
    private String mgtLevel;
    private String daepyoEmailAddr;
    private String daepyoNmYn;
    private String daepyoCpTelNo;
    private String accCd;
    private String accEmailAddr;
    private String banpAutoJaesGb;
    private String banpTrustYn;
    private String pissSchUse;
    private String bokPanswaeYn;
    private String bokPart;
    private String sunlblUseYn;
    private String prtGb;
    private String pltUseKpp;
    private String pltUseAj;
    private String pltUseNg;
    private String pltUseEx;
    private String jaebulTrustYn;
    private String julpanTrustYn;
    private String jaebulTrustDt;
    private String salesManagerNm;     // 영업팀장이름
    private String paymentManagerNm;   // 결제담당이름
    private String orderManagerNm;     // 주문담당이름

    private String salesManagerTel;    // 영업팀장 전화번호
    private String paymentManagerTel;  // 결제담당 전화번호
    private String orderManagerTel;    // 주문담당 전화번호

    // 조인된 테이블 컬럼
    private String chulpanTeamHeadNm;
    private String chulpanHeadTelNo;
    private String chulpanDamdangNm;
    private String chulpanDamdangTelNo;
    private String chulpanOrderDamdangNm;
    private String chulpanOrderTelNo;

    private String chargeEmpNm;
    private String chargeEmpTelNo;
    private String chargeEmpFaxNo;

    private String delivCarNo;
    private String expressDelivCarNo;

    private String scanYn;
    private String totScanYn;
    private String panswaeDt;

    private String salesDamdangId;
    private String salesDamdangNm;
    private String pissEndDt;
    private String mainSale;
    
    private String custX;
    private String custY;
    private String custX3;
    private String custY3;
    private String changAddr;
    
    private String changAddr2;
    private String changPostNo;
    private String ptlGb;
    private Integer dochaQty;
    private Integer dochaBox;
    private Integer dochaAmt;

}
