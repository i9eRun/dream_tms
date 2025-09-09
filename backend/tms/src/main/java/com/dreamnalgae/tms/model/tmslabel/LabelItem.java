package com.dreamnalgae.tms.model.tmslabel;

import lombok.Data;

@Data
public class LabelItem {
    private Long rowSeq;
    private String ordNo;
    private String sujumCd;
    private String sujumNm;
    private Integer courseCd;
    private Integer dunge;
    private Integer qty;
    private String jiyukNm;
    private String chulpanNm;
}
