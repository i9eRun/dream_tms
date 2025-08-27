package com.dreamnalgae.tms.model.tbas.tbas1011;

import java.util.Date;

import lombok.Data;

@Data
public class TmsSubVO {
    private String userCetCd;     // 사용자센터코드 (PK)
    private String cusCd;         // 거래처 코드
    private Integer subSunbun;    // 순번
    private Integer oldSubSunbun;    // 순번
    private String custCd;        // 거래처 코드
    private String custNm;        // 거래처명
    private String jiyukNm;        // 거래처명
    private String custGb;        // 거래처 구분
    private String subWorkGb;     // 작업 구분
    private String subOrderGb;    // 주문 구분
    private String subChulGb;     // 출고 구분
    private Date insertDt;        // 등록일자
    private String insertId;      // 등록자 ID
    private Date updateDt;        // 수정일자
    private String updateId;      // 수정자 ID
}
