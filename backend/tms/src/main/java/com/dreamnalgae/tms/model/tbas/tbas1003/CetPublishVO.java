package com.dreamnalgae.tms.model.tbas.tbas1003;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CetPublishVO {
    private String userCetCd;           // 사용자대행사코드
    private String chk;                 // '0' 고정 (프론트 전용)

    private Long rowSeq;                // 순번
    private String chulpanCd;           // 출판사코드
    private String cetCd;             // 서점코드

    private BigDecimal basicDangaRt;    // 기본비율
    private Long singanQty;             // 신간기본부수

    private String bokCnvCd;            // 변환코드
    private String baebonCd;            // 배본처
    private String bokCnvCd2;           // 반품변환코드

    private String partCd;              // 부문코드
    private String cnvSujumCd;          // 변환서점코드

    private String centerPrtYn;        // 대행사거래명세서출력여부 ('1': 출력, '0': 미출력)
    private String centerPrtGb;        // 대행사출력구분 ('1': 운영팀, '2': 서점, '3': 드림날개)
    private String chulpanPrtGb;          // 출판사출력구분 ('1': 운영팀, '2': 서점, '3': 드림날개)

    private String banpNotYn;           // 반품불가여부 ('1': 불가, '0': 가능)
    private String oldCnvCd;            // 이전변환코드

    private Date insertDt;              // 입력일자
    private String insertId;            // 입력자ID
    private Date updateDt;              // 수정일자
    private String updateId;            // 수정자ID

    // 프론트 그리드용 추가 정보
    private String custAbbrNm;          // 서점 약칭
    private String jiyukNm;             // 지역명
}
