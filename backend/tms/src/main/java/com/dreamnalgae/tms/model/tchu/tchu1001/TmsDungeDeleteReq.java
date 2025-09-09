package com.dreamnalgae.tms.model.tchu.tchu1001;

import lombok.Data;

@Data
public class TmsDungeDeleteReq {
    private String userCetCd;
    private Long rowSeq;   // NUMBER
    private String ordNo;  // VARCHAR2(15)
}
