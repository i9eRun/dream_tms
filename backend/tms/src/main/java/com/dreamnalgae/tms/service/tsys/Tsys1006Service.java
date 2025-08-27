package com.dreamnalgae.tms.service.tsys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.tsys.tsys1006.TmsExcelDlLogVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsys1006Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsExcelDlLogVO> selectList(String userCetCd, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                  LOG_ID,
                  CREATED_AT,
                  USER_ID,
                  USER_CET_CD,
                  FILE_NAME,
                  MENU_ID,
                  MENU_NM,
                  ROW_COUNT,
                  CLIENT_IP,
                  USER_AGENT,
                  REQUEST_URI,
                  PARAMS_JSON
            FROM TMS_EXCEL_DL_LOG
            WHERE USER_CET_CD = :userCetCd
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);

        // if (StringUtils.hasText(cusGb)) {
        //     sql.append(" AND C.CUS_GB = :cusGb");
        //     params.put("cusGb", cusGb);
        // }

        // if (StringUtils.hasText(cusAmpmGb)) {
        //     sql.append(" AND C.CUS_AMPM_GB = :cusAmpmGb");
        //     params.put("cusAmpmGb", cusAmpmGb);
        // }

        //sql.append(" ORDER BY C.CUS_CD");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsExcelDlLogVO.class));
    }
    
    
}
