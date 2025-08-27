package com.dreamnalgae.tms.service.tsub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.tsub.tsub1005.TmsIlSubVO;
import com.dreamnalgae.tms.model.tsub.tsub1005.TransferVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsub1005Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsIlSubVO> selectList(String userCetCd, String cusDt, String cusGb, String cusAmpmGb, String cusCd) {
        StringBuilder sql = new StringBuilder("""
            SELECT  A.CUS_CD,
                    SF_NM_CD(A.CUS_CD, 'TMS_CUS') AS CUS_NM,
                    SF_NM_CD(B.USER_ID, 'TMS_COM_USER') AS USER_NM,
                    SUM(ORD_QTY) AS QTY,
                    SUM(ORD_BOX) AS BOX  
            FROM TMS_IL_CUS A
                JOIN TMS_BAECHA B ON A.CUS_CD = B.CUS_CD AND A.CUS_DT = B.CUS_DT
                JOIN TMS_ORDER_MST C ON A.CUS_DT = C.TMS_DT AND A.CUS_CD = C.DELIV_PATH_CD
                JOIN TMS_CUS D ON A.CUS_CD = D.CUS_CD
            WHERE A.CUS_DT = :cusDt
            AND A.USER_CET_CD = :userCetCd
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("cusDt", cusDt);

        if (StringUtils.hasText(cusGb)) {
            sql.append(" AND D.CUS_GB = :cusGb");
            params.put("cusGb", cusGb);
        }

        if (StringUtils.hasText(cusAmpmGb)) {
            sql.append(" AND D.CUS_AMPM_GB = :cusAmpmGb");
            params.put("cusAmpmGb", cusAmpmGb);
        }

        if (StringUtils.hasText(cusCd)) {
            sql.append(" AND A.CUS_CD = :cusCd");
            params.put("cusCd", cusCd);
        }

        sql.append(" GROUP BY A.CUS_CD, B.USER_ID");

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsIlSubVO.class));
    }


    public List<Map<String, Object>> selectItemList(String userCetCd, String cusDt, String cusCd) {
        StringBuilder sql = new StringBuilder("""
            SELECT C.SUJUM_CD,
                SF_NM_CD(C.SUJUM_CD, 'TMS_CUST')AS SUJUM_NM,
                SUM(ORD_QTY) AS QTY,
                SUM(ORD_BOX) AS BOX,
                SF_NM_CODE (B.SUB_WORK, '1019') AS WORK_NM  
            FROM TMS_IL_CUS A,TMS_IL_SUB B, TMS_ORDER_MST C 
            WHERE A.CUS_CD = B.CUS_CD 
                AND A.CUS_DT = C.TMS_DT 
                AND B.CUST_CD = C.SUJUM_CD 
                AND A.CUS_DT = B.CUS_DT
                AND A.CUS_CD = C.DELIV_PATH_CD
                AND A.CUS_DT = :cusDt
                AND A.CUS_CD = :cusCd
                AND A.USER_CET_CD = :userCetCd
                AND B.SUB_WORK = '1'
            GROUP BY C.SUJUM_CD, B.SUB_WORK
                UNION 
            SELECT C.CUST_CD,
                SF_NM_CD(C.CUST_CD, 'TMS_CUST')AS SUJUM_NM,
                SUM(SUGER_QTY) AS QTY,
                SUM(SUGER_BOX) AS BOX,
                SF_NM_CODE (B.SUB_WORK, '1019') AS WORK_NM 
            FROM TMS_IL_CUS A,TMS_IL_SUB B, TMS_SUGER C 
            WHERE A.CUS_CD = B.CUS_CD 
                AND B.CUST_CD = C.CUST_CD 
                AND A.CUS_DT = B.CUS_DT
                AND A.CUS_CD = C.CUS_CD
                AND C.CUS_DT = :cusDt
                AND C.CUS_CD = :cusCd
                AND A.USER_CET_CD = :userCetCd
                AND C.SUGER_GB = '0'
                AND B.SUB_WORK = '1'
            GROUP BY C.CUST_CD, B.SUB_WORK
        
        """);

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("cusDt", cusDt);
        params.put("cusCd", cusCd);

        return jdbc.query(sql.toString(), params, new ColumnMapRowMapper());
    }


    @Transactional
    public void transferItems(List<TransferVO> transferList) {
        for (TransferVO item : transferList) {
            callTransferProcedure(item);
        }
    }

    private void callTransferProcedure(TransferVO vo) {
        String callSql = "CALL TMS_WMS.SP_IKWAN_UP(:oldCusCd, :newCusCd, :custCd, :oldDt, :newDt)";

        Map<String, Object> params = new HashMap<>();
        params.put("oldCusCd", vo.getOldCusCd());
        params.put("newCusCd", vo.getNewCusCd());
        params.put("custCd", vo.getSujumCd());
        params.put("oldDt", vo.getOldDt());
        params.put("newDt", vo.getNewDt());

        jdbc.update(callSql, params);
    }



}
