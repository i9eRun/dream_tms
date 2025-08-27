package com.dreamnalgae.tms.service.tbas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tbas.tbas1011.TmsSubVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1011Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsSubVO> selectList(String userCetCd, String cusCd) {
        String sql = """
            SELECT 
                T.*,
                (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = T.CUST_CD AND ROWNUM = 1) AS CUST_NM,
                (SELECT JIYUK_NM FROM TMS_CUST WHERE CUST_CD = T.CUST_CD AND ROWNUM = 1) AS JIYUK_NM

            FROM TMS_SUB T
            WHERE USER_CET_CD = :userCetCd AND CUS_CD = :cusCd
            ORDER BY T.SUB_SUNBUN
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", userCetCd);
        params.put("cusCd", cusCd);

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(TmsSubVO.class));
    }


    
    @Transactional
    public void saveAll(Map<String, List<TmsSubVO>> data) {
        List<TmsSubVO> insertList = data.get("insertList");
        List<TmsSubVO> updateList = data.get("updateList");
        List<TmsSubVO> deleteList = data.get("deleteList");

        // 삭제
        if (deleteList !=null && !deleteList.isEmpty()) {
            String deleteSql = """
                    DELETE FROM TMS_SUB
                    WHERE USER_CET_CD = :userCetCd
                        AND CUS_CD = :cusCd
                        AND CUST_CD = :custCd
                        AND SUB_SUNBUN = :oldSubSunbun
                    """;

            List<Map<String, Object>> delParams = deleteList.stream()
                .map(vo -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userCetCd", vo.getUserCetCd());
                    map.put("cusCd", vo.getCusCd());
                    map.put("custCd", vo.getCustCd());
                    map.put("oldSubSunbun", vo.getOldSubSunbun());
                    return map;
                }).collect(Collectors.toList());

            jdbc.batchUpdate(deleteSql, delParams.toArray(new Map[0]));
        }


        // 수정처리
        if (updateList !=null && !updateList.isEmpty()) {
            String updateSql = """
                    UPDATE TMS_SUB SET
                        SUB_SUNBUN = :subSunbun,
                        SUB_WORK_GB = :subWorkGb,
                        SUB_ORDER_GB = :subOrderGb,
                        UPDATE_ID = :updateId,
                        UPDATE_DT = SYSDATE
                    WHERE USER_CET_CD = :userCetCd
                        AND CUS_CD = :cusCd
                        AND CUST_CD = :custCd
                        AND SUB_SUNBUN = :oldSubSunbun
                    """;

                List<Map<String, Object>> updateParams = updateList.stream()
                    .map(vo -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("subSunbun", vo.getSubSunbun());
                        map.put("subWorkGb", vo.getSubWorkGb());
                        map.put("subOrderGb", vo.getSubOrderGb());
                        map.put("updateId", vo.getUpdateId());
                        map.put("userCetCd", vo.getUserCetCd());
                        map.put("cusCd", vo.getCusCd());
                        map.put("custCd", vo.getCustCd());
                        map.put("oldSubSunbun", vo.getOldSubSunbun());
                        return map;
                    })
                    .collect(Collectors.toList());

            jdbc.batchUpdate(updateSql, updateParams.toArray(new Map[0]));
        }


        // 3. 등록 처리
        if (insertList != null && !insertList.isEmpty()) {
            String insertSql = """
                INSERT INTO TMS_SUB (
                    USER_CET_CD, CUS_CD, CUST_CD, SUB_SUNBUN,
                    CUST_GB, SUB_WORK_GB, SUB_ORDER_GB,
                    INSERT_ID, INSERT_DT
                ) VALUES (
                    :userCetCd, :cusCd, :custCd, :subSunbun,
                    :custGb, :subWorkGb, :subOrderGb,
                    :insertId, SYSDATE
                )
            """;

            List<Map<String, Object>> insertParams = insertList.stream()
                .map(vo -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userCetCd", vo.getUserCetCd());
                    map.put("cusCd", vo.getCusCd());
                    map.put("custCd", vo.getCustCd());
                    map.put("subSunbun", vo.getSubSunbun());
                    map.put("custGb", vo.getCustGb());
                    map.put("subWorkGb", vo.getSubWorkGb());
                    map.put("subOrderGb", vo.getSubOrderGb());
                    map.put("insertId", vo.getInsertId());
                    return map;
                })
                .collect(Collectors.toList());

            jdbc.batchUpdate(insertSql, insertParams.toArray(new Map[0]));
        }







    }

    


    
}
