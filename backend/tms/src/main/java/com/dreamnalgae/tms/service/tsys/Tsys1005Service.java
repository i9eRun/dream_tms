package com.dreamnalgae.tms.service.tsys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tsys.TmsCodeMVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsys1005Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsCodeMVO> selectCodeGroups(TmsCodeMVO param) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
          .append("  GROUP_CD, CODE_CD, CODE_NM, CODE_ABBR_NM, CODE_LENGTH, CODE_BIGO, ")
          .append("  CODE_COL_ID, ARANGE_SEQ, USE_YN, ")
          .append("  REF_COL_CHAR1, REF_COL_CHAR2, REF_COL_CHAR3, REF_COL_CHAR4, REF_COL_CHAR5, ")
          .append("  REF_COL_NUM1, REF_COL_NUM2, REF_COL_NUM3, REF_COL_NUM4, REF_COL_NUM5 ")
          .append("FROM TMS_CODE_M ")
          .append("WHERE GROUP_CD = '000' ");

        Map<String, Object> params = new HashMap<>();

        if (param.getGroupKeyword() != null && !param.getGroupKeyword().isEmpty()) {
            sb.append("AND (CODE_NM LIKE :keyword OR CODE_CD LIKE :keyword) ");
            params.put("keyword", "%" + param.getGroupKeyword() + "%");
        }

        sb.append("ORDER BY ARANGE_SEQ, CODE_CD ");

        return jdbc.query(sb.toString(), params, new BeanPropertyRowMapper<>(TmsCodeMVO.class));
    }

    public List<TmsCodeMVO> selectCodeList(String groupCd) {
        String sql = """
            SELECT
                  T1.GROUP_CD
                , T1.CODE_CD
                , T1.CODE_NM
                , T1.CODE_ABBR_NM
                , T1.CODE_LENGTH
                , T1.CODE_BIGO
                , T1.CODE_COL_ID
                , T1.ARANGE_SEQ
                , T1.USE_YN
                , T1.REF_COL_CHAR1
                , T1.REF_COL_CHAR2
                , T1.REF_COL_CHAR3
                , T1.REF_COL_CHAR4
                , T1.REF_COL_CHAR5
                , T1.REF_COL_NUM1
                , T1.REF_COL_NUM2
                , T1.REF_COL_NUM3
                , T1.REF_COL_NUM4
                , T1.REF_COL_NUM5
                , T2.REF_COL_CHAR1 AS CHAR1_TITLE
                , T2.REF_COL_CHAR2 AS CHAR2_TITLE
                , T2.REF_COL_CHAR3 AS CHAR3_TITLE
                , T2.REF_COL_CHAR4 AS CHAR4_TITLE
                , T2.REF_COL_CHAR5 AS CHAR5_TITLE
            FROM TMS_CODE_M T1
            INNER JOIN (
                SELECT
                    GROUP_CD,
                    CODE_CD,
                    NVL(REF_COL_CHAR1, '문자형1') AS REF_COL_CHAR1,
                    NVL(REF_COL_CHAR2, '문자형2') AS REF_COL_CHAR2,
                    NVL(REF_COL_CHAR3, '문자형3') AS REF_COL_CHAR3,
                    NVL(REF_COL_CHAR4, '문자형4') AS REF_COL_CHAR4,
                    NVL(REF_COL_CHAR5, '문자형5') AS REF_COL_CHAR5
                FROM TMS_CODE_M
                WHERE GROUP_CD = '000'
            ) T2 ON T1.GROUP_CD = T2.CODE_CD
            WHERE T1.GROUP_CD = :groupCd
            ORDER BY T1.ARANGE_SEQ, T1.GROUP_CD, T1.CODE_CD
        """;

        Map<String, Object> params = Map.of("groupCd", groupCd);

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(TmsCodeMVO.class));
    }
        
    public int insertGroupCode(TmsCodeMVO dto) {
        String sql = """
            INSERT INTO TMS_CODE_M (
                USER_CET_CD, GROUP_CD, CODE_CD, CODE_NM, CODE_ABBR_NM, CODE_LENGTH,
                CODE_BIGO, CODE_COL_ID, ARANGE_SEQ, USE_YN,
                REF_COL_CHAR1, REF_COL_CHAR2, REF_COL_CHAR3, REF_COL_CHAR4, REF_COL_CHAR5,
                REF_COL_NUM1, REF_COL_NUM2, REF_COL_NUM3, REF_COL_NUM4, REF_COL_NUM5,
                INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, '000', :codeCd, :codeNm, :codeAbbrNm, :codeLength,
                :codeBigo, :codeColId, :arangeSeq, :useYn,
                :refColChar1, :refColChar2, :refColChar3, :refColChar4, :refColChar5,
                :refColNum1, :refColNum2, :refColNum3, :refColNum4, :refColNum5,
                SYSDATE, :insertId
            )
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(dto));
    }

    public int updateGroupCode(TmsCodeMVO dto) {
        String sql = """
            UPDATE TMS_CODE_M SET
                USER_CET_CD     = :userCetCd,
                CODE_NM         = :codeNm,
                CODE_ABBR_NM    = :codeAbbrNm,
                CODE_LENGTH     = :codeLength,
                CODE_BIGO       = :codeBigo,
                CODE_COL_ID     = :codeColId,
                ARANGE_SEQ      = :arangeSeq,
                USE_YN          = :useYn,
                REF_COL_CHAR1   = :refColChar1,
                REF_COL_CHAR2   = :refColChar2,
                REF_COL_CHAR3   = :refColChar3,
                REF_COL_CHAR4   = :refColChar4,
                REF_COL_CHAR5   = :refColChar5,
                REF_COL_NUM1    = :refColNum1,
                REF_COL_NUM2    = :refColNum2,
                REF_COL_NUM3    = :refColNum3,
                REF_COL_NUM4    = :refColNum4,
                REF_COL_NUM5    = :refColNum5,
                UPDATE_DT       = SYSDATE,
                UPDATE_ID       = :updateId
            WHERE
                GROUP_CD = '000'
                AND CODE_CD = :codeCd
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(dto));
    }


    public int deleteGroupCode(String userCetCd, String codeCd) {
        String sql = """
            DELETE FROM TMS_CODE_M
            WHERE USER_CET_CD = :userCetCd
            AND GROUP_CD = '000'
            AND CODE_CD = :codeCd
        """;

        Map<String, Object> params = Map.of(
            "userCetCd", userCetCd,
            "codeCd", codeCd
        );

        return jdbc.update(sql, params);
    }


    public int insertCode(TmsCodeMVO dto) {
        String sql = """
            INSERT INTO TMS_CODE_M (
                USER_CET_CD, GROUP_CD, CODE_CD, CODE_NM, CODE_ABBR_NM, CODE_LENGTH,
                CODE_BIGO, CODE_COL_ID, ARANGE_SEQ, USE_YN,
                REF_COL_CHAR1, REF_COL_CHAR2, REF_COL_CHAR3, REF_COL_CHAR4, REF_COL_CHAR5,
                REF_COL_NUM1, REF_COL_NUM2, REF_COL_NUM3, REF_COL_NUM4, REF_COL_NUM5,
                INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :groupCd, :codeCd, :codeNm, :codeAbbrNm, :codeLength,
                :codeBigo, :codeColId, :arangeSeq, :useYn,
                :refColChar1, :refColChar2, :refColChar3, :refColChar4, :refColChar5,
                :refColNum1, :refColNum2, :refColNum3, :refColNum4, :refColNum5,
                SYSDATE, :insertId
            )
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(dto));
    }

    public int updateCode(TmsCodeMVO dto) {
        String sql = """
            UPDATE TMS_CODE_M SET
                USER_CET_CD     = :userCetCd,
                CODE_NM         = :codeNm,
                CODE_ABBR_NM    = :codeAbbrNm,
                CODE_LENGTH     = :codeLength,
                CODE_BIGO       = :codeBigo,
                CODE_COL_ID     = :codeColId,
                ARANGE_SEQ      = :arangeSeq,
                USE_YN          = :useYn,
                REF_COL_CHAR1   = :refColChar1,
                REF_COL_CHAR2   = :refColChar2,
                REF_COL_CHAR3   = :refColChar3,
                REF_COL_CHAR4   = :refColChar4,
                REF_COL_CHAR5   = :refColChar5,
                REF_COL_NUM1    = :refColNum1,
                REF_COL_NUM2    = :refColNum2,
                REF_COL_NUM3    = :refColNum3,
                REF_COL_NUM4    = :refColNum4,
                REF_COL_NUM5    = :refColNum5,
                UPDATE_DT       = SYSDATE,
                UPDATE_ID       = :updateId
            WHERE
                GROUP_CD = :groupCd
                AND CODE_CD = :codeCd
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(dto));
    }

    

    public int deleteCode(TmsCodeMVO vo) {
        String sql = "DELETE FROM TMS_CODE_M WHERE USER_CET_CD = :userCetCd AND GROUP_CD = :groupCd AND CODE_CD = :codeCd";
        
        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", vo.getUserCetCd());
        params.put("groupCd", vo.getGroupCd());
        params.put("codeCd", vo.getCodeCd());

        return jdbc.update(sql, params);
    }

}
