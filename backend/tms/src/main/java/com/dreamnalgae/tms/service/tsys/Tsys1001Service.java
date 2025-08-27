package com.dreamnalgae.tms.service.tsys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tsys.TmsComUserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsys1001Service {
    private final NamedParameterJdbcTemplate namedJdbc;

    public List<TmsComUserVO> selectMain(String searchKeyword, String userChkGb, String useYn, String geunmooJo) {
        String sql = """
            SELECT A.USER_ID,
                   A.USER_NM,
                   A.DEPT_CD,
                   A.N_DEPT_CD,
                   (SELECT DEPT_NM FROM TMS_DEPT WHERE N_DEPT_CD = A.N_DEPT_CD) AS DEPT_NM,
                   A.JIKWEE_CD,
                   A.JIKCHK_CD,
                   A.JAEJIK_GB,
                   A.ENTER_DT,
                   A.RETIRE_DT,
                   A.EXT_TEL_NO,
                   A.PASS_WD,
                   A.USER_LEVEL_GB,
                   A.USER_AUTH_RW,
                   A.CHULPAN_CD,
                   (SELECT CUST_NM FROM TMS_CUST WHERE CUST_CD = A.CHULPAN_CD) AS CHULPAN_NM,
                   A.INS_USER_ID,
                   A.INF_USER_ID,
                   A.TEL_NO,
                   A.FAX_NO,
                   A.EMAIL_ADDR,
                   A.USER_CHK_GB,
                   A.BANP_INS_ID,
                   A.USER_AUTH_ADM,
                   A.USE_YN,
                   A.BIGO,
                   A.USER_ID_OLD,
                   A.TEL_NO1,
                   A.TEL_NO2,
                   A.TEL_NO3,
                   A.PDA_NO,
                   A.PDA_BUL_DT,
                   A.PDA_BAN_DT,
                   A.WIN_UNIFORM_BUL_DT,
                   A.WIN_UNIFORM_BAN_DT,
                   A.SUM_UNIFORM_BUL_DT,
                   A.SUM_UNIFORM_BAN_DT,
                   A.JIKCHK_DT,
                   A.GEUNMOO_JO
              FROM TMS_COM_USER A
              WHERE (A.USER_ID LIKE :userInfo || '%' OR A.USER_NM LIKE '%' || :userInfo || '%')
                AND (:userChkGb IS NULL OR :userChkGb = '' OR A.USER_CHK_GB = :userChkGb)
                AND (:useYn IS NULL OR :useYn = '' OR A.USE_YN = :useYn)
                AND (:geunmooJo IS NULL OR :geunmooJo = '' OR A.GEUNMOO_JO = :geunmooJo)
                
             ORDER BY A.N_DEPT_CD, A.GEUNMOO_JO, A.JIKCHK_CD DESC, A.JIKWEE_CD, A.USER_NM ASC
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("userInfo", searchKeyword);
        params.put("userChkGb", userChkGb);
        params.put("useYn", useYn);
        params.put("geunmooJo", geunmooJo);
        //params.put("userId", userId);


        return namedJdbc.query(sql, params, new BeanPropertyRowMapper<>(TmsComUserVO.class));
    }
    
    // 유저 등록
    public boolean insertUser(TmsComUserVO vo) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(vo);

        String checkSql = "SELECT COUNT(*) FROM TMS_COM_USER WHERE USER_ID = :userId";
        int count = namedJdbc.queryForObject(checkSql, params, Integer.class);

        if (count > 0) {
            throw new DuplicateKeyException("이미 존재하는 사용자 ID입니다.");
        }


        String sql = """
            INSERT INTO TMS_COM_USER (
                USER_ID, USER_CET_CD, USER_NM, N_DEPT_CD, JIKWEE_CD, JIKCHK_CD,
                JAEJIK_GB, ENTER_DT, RETIRE_DT, JIKWEE_NM, EXT_TEL_NO, PASS_WD,
                USER_LEVEL_GB, USER_AUTH_RW, CHULPAN_CD, INS_USER_ID, INF_USER_ID,
                TEL_NO, FAX_NO, EMAIL_ADDR, USER_CHK_GB, BANP_INS_ID, USER_AUTH_ADM,
                USE_YN, BIGO, EMP_ID, USER_ID_OLD, INSERT_DT, INSERT_ID, UPDATE_DT, UPDATE_ID,
                JIYUK_CD, LAST_LOGIN_TIME, TEL_NO1, TEL_NO2, TEL_NO3,
                JIKCHK_DT, GEUNMOO_JO, LAST_LOGIN_ADDR, DEPT_CD,
                PDA_NO, PDA_BUL_DT, PDA_BAN_DT,
                WIN_UNIFORM_BUL_DT, WIN_UNIFORM_BAN_DT,
                SUM_UNIFORM_BUL_DT, SUM_UNIFORM_BAN_DT
            ) VALUES (
                :userId, :userCetCd, :userNm, :nDeptCd, :jikweeCd, :jikchkCd,
                :jaejikGb, :enterDt, :retireDt, :jikweeNm, :extTelNo, :passWd,
                :userLevelGb, :userAuthRw, :chulpanCd, :insUserId, :infUserId,
                :telNo, :faxNo, :emailAddr, :userChkGb, :banpInsId, :userAuthAdm,
                :useYn, :bigo, :empId, :userIdOld, SYSDATE, :insertId, :updateDt, :updateId,
                :jiyukCd, :lastLoginTime, :telNo1, :telNo2, :telNo3,
                :jikchkDt, :geunmooJo, :lastLoginAddr, :deptCd,
                :pdaNo, :pdaBulDt, :pdaBanDt,
                :winUniformBulDt, :winUniformBanDt,
                :sumUniformBulDt, :sumUniformBanDt
            )
                """;

        
        return namedJdbc.update(sql, params) > 0;
    }

    // 유저 업데이트
    public boolean updateUser(TmsComUserVO vo) {
        String sql = """
            UPDATE TMS_COM_USER SET
                USER_NM = :userNm,
                N_DEPT_CD = :nDeptCd,
                JIKWEE_CD = :jikweeCd,
                JIKCHK_CD = :jikchkCd,
                JAEJIK_GB = :jaejikGb,
                ENTER_DT = :enterDt,
                RETIRE_DT = :retireDt,
                JIKWEE_NM = :jikweeNm,
                EXT_TEL_NO = :extTelNo,
                PASS_WD = :passWd,
                USER_LEVEL_GB = :userLevelGb,
                USER_AUTH_RW = :userAuthRw,
                CHULPAN_CD = :chulpanCd,
                INS_USER_ID = :insUserId,
                INF_USER_ID = :infUserId,
                TEL_NO = :telNo,
                FAX_NO = :faxNo,
                EMAIL_ADDR = :emailAddr,
                USER_CHK_GB = :userChkGb,
                BANP_INS_ID = :banpInsId,
                USER_AUTH_ADM = :userAuthAdm,
                USE_YN = :useYn,
                BIGO = :bigo,
                EMP_ID = :empId,
                USER_ID_OLD = :userIdOld,
                UPDATE_DT = SYSDATE,
                UPDATE_ID = :updateId,
                JIYUK_CD = :jiyukCd,
                LAST_LOGIN_TIME = :lastLoginTime,
                TEL_NO1 = :telNo1,
                TEL_NO2 = :telNo2,
                TEL_NO3 = :telNo3,
                JIKCHK_DT = :jikchkDt,
                GEUNMOO_JO = :geunmooJo,
                LAST_LOGIN_ADDR = :lastLoginAddr,
                DEPT_CD = :deptCd,
                PDA_NO = :pdaNo,
                PDA_BUL_DT = :pdaBulDt,
                PDA_BAN_DT = :pdaBanDt,
                WIN_UNIFORM_BUL_DT = :winUniformBulDt,
                WIN_UNIFORM_BAN_DT = :winUniformBanDt,
                SUM_UNIFORM_BUL_DT = :sumUniformBulDt,
                SUM_UNIFORM_BAN_DT = :sumUniformBanDt
            WHERE USER_ID = :userId
        """;

        return namedJdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }

    // 유저 삭제
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM TMS_COM_USER WHERE USER_ID = :userId AND USER_CET_CD = :userCetCd";

        Map<String, Object> params = Map.of("userId", userId,"userCetCd","00001");
        return namedJdbc.update(sql, params) > 0;
    }


}
