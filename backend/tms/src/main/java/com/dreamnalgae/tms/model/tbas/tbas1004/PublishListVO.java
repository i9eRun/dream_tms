package com.dreamnalgae.tms.model.tbas.tbas1004;

import lombok.Data;

@Data
public class PublishListVO {

    private String userCetCd;              
    private String custCd;              // 거래처 코드
    private String custAbbrNm;          // 거래처 약칭명
    private String jiyukNm;             // 지역명
    private String chulgoGb;            // 출고형태
    private String teamNm;              // 소속팀
    private String paymentManagerNm;    // 출고담당
    private String chargeEmpId;         // 전산담당
    private String telNo;               // 전화번호
    private String faxNo;               // 팩스번호
    private String custDivGb;           // 거래처분류
    private String banpTeamNm;          // 반품 팀명
    private String baebonCd;            // 배본 코드
}
