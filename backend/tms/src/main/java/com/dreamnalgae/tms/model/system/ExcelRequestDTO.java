package com.dreamnalgae.tms.model.system;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ExcelRequestDTO {
    private String fileName;
    private List<HeaderInfo> headers;
    private List<Map<String, Object>> data;

    // 추가
    private String userId;
    private String userCetCd;
    private String menuId;
    private String menuNm;
    private String paramsJson;

    @Data
    public static class HeaderInfo {
        private String header;     // 헤더 표시 이름
        private String dataIndex;  // 실제 데이터 key
    }   
}
