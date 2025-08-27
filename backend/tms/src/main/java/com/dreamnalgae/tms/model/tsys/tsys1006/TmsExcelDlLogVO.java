package com.dreamnalgae.tms.model.tsys.tsys1006;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TmsExcelDlLogVO {
    private Long logId;           // LOG_ID
    private String userId;        // USER_ID
    private String userCetCd;     // USER_CET_CD
    private String fileName;      // FILE_NAME
    private String menuId;        // MENU_ID
    private String menuNm;        // MENU_NM
    private Long rowCount;        // ROW_COUNT
    private String clientIp;      // CLIENT_IP
    private String userAgent;     // USER_AGENT
    private String requestUri;    // REQUEST_URI
    private String paramsJson;    // PARAMS_JSON
    private Timestamp createdAt;  // CREATED_AT
}
