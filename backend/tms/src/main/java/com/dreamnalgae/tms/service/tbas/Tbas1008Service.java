package com.dreamnalgae.tms.service.tbas;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tbas.TmsDeptVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1008Service {
    private final NamedParameterJdbcTemplate jdbc;

    // 부서코드 리스트 불러오기
    public List<Map<String, Object>> selectMain(String deptInfoSch, String useYnSch) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
          .append("    DEPT_CD, N_DEPT_CD, DEPT_NM, JIYUK_CD, DEPT_CD_OLD, DEPT_NM_OLD, ")
          .append("    DEPT_GWA_NM_OLD, DEPT_BAN_NM_OLD, DEPT_CHO_NM_OLD, DEPT_ACC_CD, TEAM_HEAD_NM_OLD, ")
          .append("    USE_YN, TEAM_NM, OLD_TEAM_NM, TEAM_GB, MULRYU_GB, ARANGE_SEQ1, ARANGE_SEQ2, ")
          .append("    NEW_TEAM_ABBR_NM, NEW_TEAM_NM, NEW_CHO_NM, NEW_TEAM_HEAD_NM, JO_HEAD_NM, ")
          .append("    TOT_HEAD_NM, MGR_HEAD_NM, INSA_DEPT_CD_OLD, NEW_TEAM_HEAD_ID, JO_HEAD_ID, ")
          .append("    TOT_HEAD_ID, MGR_HEAD_ID, JAES_DV_AMT, USER_CNT ")
          .append("FROM TMS_DEPT ")
          .append("WHERE 1=1 ")
          .append("  AND (N_DEPT_CD LIKE :keyword OR DEPT_NM LIKE :keyword) ")
          .append("  AND (:useYn IS NULL OR :useYn = '' OR USE_YN = :useYn) ")
          .append("ORDER BY USE_YN DESC, N_DEPT_CD");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("keyword", "%" + (deptInfoSch != null ? deptInfoSch : "") + "%");
        params.addValue("useYn", useYnSch);

        return jdbc.query(sb.toString(), params, new ColumnMapRowMapper());
    }
    
    // INSERT
    public boolean insert(TmsDeptVO vo) {
        String sql = """
            INSERT INTO TMS_DEPT (
                N_DEPT_CD, USER_CET_CD, DEPT_NM, JIYUK_CD,
                DEPT_CD_OLD, DEPT_NM_OLD, DEPT_GWA_NM_OLD, DEPT_BAN_NM_OLD, DEPT_CHO_NM_OLD, DEPT_ACC_CD,
                TEAM_HEAD_NM_OLD, USE_YN, TEAM_NM, OLD_TEAM_NM, TEAM_GB, MULRYU_GB,
                ARANGE_SEQ1, ARANGE_SEQ2, NEW_TEAM_ABBR_NM, NEW_TEAM_NM, NEW_CHO_NM,
                NEW_TEAM_HEAD_NM, JO_HEAD_NM, TOT_HEAD_NM, MGR_HEAD_NM, INSA_DEPT_CD_OLD,
                INSERT_DT, INSERT_ID, USER_CNT, JAES_DV_AMT, DEPT_CD,
                NEW_TEAM_HEAD_ID, JO_HEAD_ID, TOT_HEAD_ID, MGR_HEAD_ID
            ) VALUES (
                :nDeptCd, :userCetCd, :deptNm, :jiyukCd,
                :deptCdOld, :deptNmOld, :deptGwaNmOld, :deptBanNmOld, :deptChoNmOld, :deptAccCd,
                :teamHeadNmOld, :useYn, :teamNm, :oldTeamNm, :teamGb, :mulryuGb,
                :arangeSeq1, :arangeSeq2, :newTeamAbbrNm, :newTeamNm, :newChoNm,
                :newTeamHeadNm, :joHeadNm, :totHeadNm, :mgrHeadNm, :insaDeptCdOld,
                SYSDATE, :insertId, :userCnt, :jaesDvAmt, :deptCd,
                :newTeamHeadId, :joHeadId, :totHeadId, :mgrHeadId
            )
        """;
        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        return jdbc.update(sql, params) > 0;
    }

    // UPDATE
    public int update(TmsDeptVO vo) {
        String sql = """
            UPDATE TMS_DEPT SET
                DEPT_NM = :deptNm,
                JIYUK_CD = :jiyukCd,
                DEPT_CD_OLD = :deptCdOld,
                DEPT_NM_OLD = :deptNmOld,
                DEPT_GWA_NM_OLD = :deptGwaNmOld,
                DEPT_BAN_NM_OLD = :deptBanNmOld,
                DEPT_CHO_NM_OLD = :deptChoNmOld,
                DEPT_ACC_CD = :deptAccCd,
                TEAM_HEAD_NM_OLD = :teamHeadNmOld,
                USE_YN = :useYn,
                TEAM_NM = :teamNm,
                OLD_TEAM_NM = :oldTeamNm,
                TEAM_GB = :teamGb,
                MULRYU_GB = :mulryuGb,
                ARANGE_SEQ1 = :arangeSeq1,
                ARANGE_SEQ2 = :arangeSeq2,
                NEW_TEAM_ABBR_NM = :newTeamAbbrNm,
                NEW_TEAM_NM = :newTeamNm,
                NEW_CHO_NM = :newChoNm,
                NEW_TEAM_HEAD_NM = :newTeamHeadNm,
                JO_HEAD_NM = :joHeadNm,
                TOT_HEAD_NM = :totHeadNm,
                MGR_HEAD_NM = :mgrHeadNm,
                INSA_DEPT_CD_OLD = :insaDeptCdOld,
                UPDATE_DT = SYSDATE,
                UPDATE_ID = :updateId,
                USER_CNT = :userCnt,
                JAES_DV_AMT = :jaesDvAmt,
                DEPT_CD = :deptCd,
                NEW_TEAM_HEAD_ID = :newTeamHeadId,
                JO_HEAD_ID = :joHeadId,
                TOT_HEAD_ID = :totHeadId,
                MGR_HEAD_ID = :mgrHeadId
            WHERE USER_CET_CD = :userCetCd AND N_DEPT_CD = :nDeptCd
        """;
        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);
        return jdbc.update(sql, params);
    }

    // DELETE
    public int delete(String userCetCd, String nDeptCd) {
        String sql = "DELETE FROM TMS_DEPT WHERE USER_CET_CD = :userCetCd AND N_DEPT_CD = :nDeptCd";
        return jdbc.update(sql,
                Map.of("userCetCd", userCetCd, "nDeptCd", nDeptCd));
    }
    
}
