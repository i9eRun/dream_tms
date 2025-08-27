package com.dreamnalgae.tms.model.tsys;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TmsMenuVO {
    private String userCetCd;      // 사용자 구분 코드
    private Integer menuSeq;       // 메뉴 일련번호
    private String menuId;         // 메뉴 ID
    private String menuNm;         // 메뉴 이름
    private Integer menuLevel;     // 메뉴 레벨
    private String menuIcon;       // 아이콘
    private String parentMenuId;   // 상위 메뉴 ID
    private String pgmYn;          // 프로그램 여부
    private String useYn;          // 사용 여부
    private String execUrl;        // 실행 URL
    private String execType;       // 실행 타입
    private String menuParam;      // 메뉴 파라미터
    private Integer menuOrd;       // 메뉴 순서
    private String userType;       // 사용자 타입
    private Date insertDt;         // 등록일시
    private String insertId;       // 등록자
    private Date updateDt;         // 수정일시
    private String updateId;       // 수정자
    private String runType;        // 실행 방식
    private String lockType;       // 잠금 타입

    private List<TmsMenuVO> children;

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public boolean isExpanded() {
        return true;
    }
    
}
