package com.dreamnalgae.tms.service.tsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tsys.TmsGroupMVO;
import com.dreamnalgae.tms.model.tsys.TmsGroupUserMVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsys1003Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsGroupMVO> selectGroup(String userCetCd, String keyword) {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ")
        .append("  GROUP_CD, GROUP_NM, BIGO, USE_YN ")
        .append("FROM TMS_GROUP_M ")
        .append("WHERE USER_CET_CD = :userCetCd ");

      Map<String, Object> params = new HashMap<>();
      params.put("userCetCd", userCetCd);

      if (keyword != null && !keyword.trim().isEmpty()) {
          sb.append(" AND GROUP_NM LIKE :keyword ");
          params.put("keyword", "%" + keyword + "%");
      }

      sb.append("ORDER BY GROUP_CD ");

      return jdbc.query(sb.toString(), params, new BeanPropertyRowMapper<>(TmsGroupMVO.class));
    }

    public List<String> findMenuIdsByGroup(String userCetCd, String groupCd) {
      String sql = """
          SELECT MENU_ID
          FROM TMS_GROUP_MENU_M
          WHERE USER_CET_CD = :userCetCd
            AND GROUP_CD = :groupCd
      """;

      Map<String, Object> params = Map.of(
          "userCetCd", userCetCd,
          "groupCd", groupCd
      );

      return jdbc.query(sql, params, (rs, rowNum) -> rs.getString("MENU_ID"));
    }

    @Transactional
    public void saveGroupMenus(String userCetCd, String groupCd, String userId, List<String> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
          deleteMenusByGroup(userCetCd, groupCd);
          return;
        }

        // 1. 메뉴 전체 트리 캐싱
        Map<String, String> parentMap = findAllMenuParentMap();
        
        // 2. 부모 포함 메뉴 ID 전체 집합 생성
        Set<String> allMenuIds = new HashSet<>();
        for (String menuId : menuIds) {
            allMenuIds.add(menuId);
            String parent1 = parentMap.get(menuId);
            if (parent1 != null && !parent1.isBlank()) {
                allMenuIds.add(parent1);
                String parent2 = parentMap.get(parent1);
                if (parent2 != null && !parent2.isBlank()) {
                    allMenuIds.add(parent2);
                }
            }
        }

        //System.out.println(allMenuIds);

        // 3. 기존 그룹 메뉴 삭제
        deleteMenusByGroup(userCetCd, groupCd);

        // 4. 일괄 insert
        insertGroupMenus(userCetCd, groupCd, userId, new ArrayList<>(allMenuIds));
    }

    // 전체 메뉴 Map<menuId, parentMenuId> 로 불러오기 (캐시용)
    public Map<String, String> findAllMenuParentMap() {
        String sql = "SELECT MENU_ID, PARENT_MENU_ID FROM TMS_MENU_M";
        return jdbc.query(sql, rs -> {
            Map<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("MENU_ID"), rs.getString("PARENT_MENU_ID"));
            }
            return map;
        });
    }

    public void deleteMenusByGroup(String userCetCd, String groupCd) {
        String sql = """
            DELETE FROM TMS_GROUP_MENU_M
            WHERE USER_CET_CD = :userCetCd
              AND GROUP_CD = :groupCd
        """;

        Map<String, Object> params = Map.of(
            "userCetCd", userCetCd,
            "groupCd", groupCd
        );

        jdbc.update(sql, params);
    }

    public void insertGroupMenus(String userCetCd, String groupCd, String userId, List<String> menuIds) {
        String sql = """
            INSERT INTO TMS_GROUP_MENU_M (
                USER_CET_CD, GROUP_CD, MENU_ID,
                INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :groupCd, :menuId,
                SYSDATE, :userId
            )
        """;

        List<Map<String, String>> batchParams = menuIds.stream()
          .filter(menuId -> menuId != null && !menuId.isBlank())  // ← 최상위 메뉴가 포함되는 경우 부모메뉴가 없어 null 들어와서 null 체크 추가함.
          .map(menuId -> Map.of(
              "userCetCd", userCetCd,
              "groupCd", groupCd,
              "menuId", menuId,
              "userId", userId
          ))
          .toList();

        jdbc.batchUpdate(sql, batchParams.toArray(new Map[0]));
    }




    // 그룹 사용자 리스트 불러오기
    public List<Map<String, Object>> selectGroupUserList(String groupCd, String userCetCd) {
        String sql = """
            SELECT gu.user_id, u.user_nm, td.DEPT_NM
            FROM TMS_GROUP_USER_M gu
            LEFT JOIN TMS_COM_USER u ON gu.user_id = u.user_id
            LEFT JOIN TMS_DEPT td ON u.N_DEPT_CD = td.N_DEPT_CD
            WHERE gu.group_cd = :groupCd
                AND gu.user_cet_cd = :userCetCd
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("groupCd", groupCd);
        params.put("userCetCd", userCetCd);

        return jdbc.queryForList(sql, params);
    }



    // 그룹 사용자 추가
    @Transactional
    public void insertGroupUser(TmsGroupUserMVO vo) {
        String sql = """
            INSERT INTO TMS_GROUP_USER_M (
                USER_CET_CD, GROUP_CD, USER_ID, INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :groupCd, :userId, SYSDATE, :insertId
            )
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", vo.getUserCetCd());
        params.put("groupCd", vo.getGroupCd());
        params.put("userId", vo.getUserId());
        params.put("insertId", vo.getInsertId());

        jdbc.update(sql, params);
    }


    // 그룹 사용자 삭제
    @Transactional
    public void deleteGroupUser(TmsGroupUserMVO vo) {
        String sql = """
            DELETE FROM TMS_GROUP_USER_M
            WHERE USER_CET_CD = :userCetCd
            AND GROUP_CD = :groupCd
            AND USER_ID = :userId
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", vo.getUserCetCd());
        params.put("groupCd", vo.getGroupCd());
        params.put("userId", vo.getUserId());

        jdbc.update(sql, params);
    }

    

    // 그룹 코드 추가
    @Transactional
    public boolean insertGroup(TmsGroupMVO vo) {
        // 1. 중복 검사
        String dupCheckSql = """
            SELECT COUNT(*) 
            FROM TMS_GROUP_M 
            WHERE GROUP_CD = :groupCd 
            AND USER_CET_CD = :userCetCd
        """;

        Map<String, Object> checkParams = Map.of(
            "groupCd", vo.getGroupCd(),
            "userCetCd", vo.getUserCetCd()
        );

        Integer count = jdbc.queryForObject(dupCheckSql, checkParams, Integer.class);
        if (count != null && count > 0) {
            throw new DuplicateKeyException("이미 존재하는 그룹입니다.");
        }

        // 2. insert
        String insertSql = """
            INSERT INTO TMS_GROUP_M (
                USER_CET_CD, GROUP_CD, GROUP_NM, USE_YN, BIGO, INSERT_DT, INSERT_ID
            ) VALUES (
                :userCetCd, :groupCd, :groupNm, :useYn, :bigo, SYSDATE, :insertId
            )
        """;

        Map<String, Object> insertParams = new HashMap<>();
        insertParams.put("userCetCd", vo.getUserCetCd());
        insertParams.put("groupCd", vo.getGroupCd());
        insertParams.put("groupNm", vo.getGroupNm());
        insertParams.put("useYn", vo.getUseYn());
        insertParams.put("bigo", vo.getBigo());
        insertParams.put("insertId", vo.getInsertId());

        return jdbc.update(insertSql, insertParams) > 0;
    }

    @Transactional
    public boolean updateGroup(TmsGroupMVO vo) {
        String sql = """
            UPDATE TMS_GROUP_M
            SET GROUP_NM = :groupNm,
                USE_YN = :useYn,
                BIGO = :bigo,
                UPDATE_DT = SYSDATE,
                UPDATE_ID = :updateId
            WHERE USER_CET_CD = :userCetCd
            AND GROUP_CD = :groupCd
        """;

        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }


    // 그룹 삭제
    public boolean deleteGroup(TmsGroupMVO vo) {
        String sql = """
            DELETE FROM TMS_GROUP_M
            WHERE USER_CET_CD = :userCetCd
            AND GROUP_CD = :groupCd
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("userCetCd", vo.getUserCetCd());
        params.put("groupCd", vo.getGroupCd());

        return jdbc.update(sql, params) > 0;
    }

    
}
