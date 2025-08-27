package com.dreamnalgae.tms.model.tbas.tbas1011;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class TmsSubSaveRequest {
    private List<TmsSubVO> modified;
    private List<Map<String, Object>> removed; // userCetCd + cusCd 조합
}
