package com.dreamnalgae.tms.service.tpop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tpop.UserSearchVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tpop1001Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<UserSearchVO> searchUsers(String searchKeyword, String userCetCd, String useYn) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append("""
            SELECT A.USER_ID, A.USER_NM, A.DEPT_CD, B.DEPT_NM, A.N_DEPT_CD, A.JIKWEE_CD,
                   (SELECT CODE_NM FROM TMS_CODE_M WHERE GROUP_CD = '240' AND CODE_CD = A.JIKWEE_CD) AS JIKWEE_NM,
                   A.JIKCHK_CD,
                   (SELECT CODE_NM FROM TMS_CODE_M WHERE GROUP_CD = '241' AND CODE_CD = A.JIKCHK_CD) AS JIKCHK_NM,
                   A.JAEJIK_GB,
                   (SELECT CODE_NM FROM TMS_CODE_M WHERE GROUP_CD = '242' AND CODE_CD = A.JAEJIK_GB) AS JAEJIK_NM,
                   TO_CHAR(A.ENTER_DT, 'YYYYMMDD') AS ENTER_DT,
                   TO_CHAR(A.RETIRE_DT, 'YYYYMMDD') AS RETIRE_DT,
                   A.EXT_TEL_NO, A.PASS_WD, A.USER_LEVEL_GB, A.USER_AUTH_RW, A.CHULPAN_CD,
                   (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = A.CHULPAN_CD) AS CHULPAN_NM,
                   A.INS_USER_ID, A.INF_USER_ID, A.TEL_NO, A.FAX_NO, A.EMAIL_ADDR,
                   A.USER_CHK_GB, A.BANP_INS_ID, A.USER_AUTH_ADM,
                   DECODE(A.USE_YN, '1', '사용', '미사용') AS AS_USE_YN,
                   A.BIGO, A.USER_ID_OLD
              FROM TMS_COM_USER A
              LEFT JOIN TMS_DEPT B ON A.DEPT_CD = B.DEPT_CD
             WHERE 1=1
        """);

        // 사용여부 필터
        if (useYn != null && !useYn.isEmpty()) {
            sql.append(" AND A.USE_YN = :useYn ");
            params.put("useYn", useYn);
        }

        // 사용자 소속 회사코드 (userCetCd)
        sql.append(" AND A.USER_CET_CD = :userCetCd ");
        params.put("userCetCd", userCetCd);

        // 검색 조건 처리
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            boolean isNumber = searchKeyword.matches("^[0-9].*");

            if (isNumber) {
                sql.append(" AND A.USER_ID LIKE :keyword1 || '%' ");
                params.put("keyword1", searchKeyword);
            } else {
                sql.append("""
                    AND (
                        A.USER_NM LIKE '%' || :keyword2 || '%' OR
                        A.USER_ID LIKE :keyword3 || '%'
                    )
                """);
                params.put("keyword2", searchKeyword);
                params.put("keyword3", searchKeyword);
            }

            sql.append("""
                ORDER BY DECODE(INSTR(A.USER_NM, :keyword4), 0, 999999, INSTR(A.USER_NM, :keyword4)), A.USER_NM
            """);
            params.put("keyword4", searchKeyword);
        } else {
            sql.append(" ORDER BY A.USER_NM ");
        }

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(UserSearchVO.class));
    }

    
}
