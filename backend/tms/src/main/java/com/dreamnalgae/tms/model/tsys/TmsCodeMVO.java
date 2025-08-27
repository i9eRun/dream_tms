package com.dreamnalgae.tms.model.tsys;

import java.util.Date;

import lombok.Data;

@Data
public class TmsCodeMVO {
    private String groupKeyword; // 검색어
    private String userCetCd;        // USER_CET_CD
    private String groupCd;          // GROUP_CD
    private String codeCd;           // CODE_CD
    private String codeNm;           // CODE_NM
    private String codeAbbrNm;       // CODE_ABBR_NM
    private Integer codeLength;      // CODE_LENGTH
    private String codeBigo;         // CODE_BIGO
    private String codeColId;        // CODE_COL_ID
    private Integer arangeSeq;       // ARANGE_SEQ
    private String useYn;            // USE_YN
    private String refColChar1;      // REF_COL_CHAR1
    private String refColChar2;      // REF_COL_CHAR2
    private String refColChar3;      // REF_COL_CHAR3
    private String refColChar4;      // REF_COL_CHAR4
    private String refColChar5;      // REF_COL_CHAR5
    private Double refColNum1;       // REF_COL_NUM1
    private Double refColNum2;       // REF_COL_NUM2
    private Double refColNum3;       // REF_COL_NUM3
    private Double refColNum4;       // REF_COL_NUM4
    private Double refColNum5;       // REF_COL_NUM5
    private Date insertDt;           // INSERT_DT
    private String insertId;         // INSERT_ID
    private Date updateDt;           // UPDATE_DT
    private String updateId;         // UPDATE_ID
    
    // 그룹에서 가져오는 REF 컬럼 제목용
    private String char1Title;
    private String char2Title;
    private String char3Title;
    private String char4Title;
    private String char5Title;
}
