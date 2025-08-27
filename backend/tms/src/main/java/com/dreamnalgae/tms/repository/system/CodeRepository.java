package com.dreamnalgae.tms.repository.system;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.dreamnalgae.tms.model.system.CodeDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CodeRepository {

    private final NamedParameterJdbcTemplate namedJdbc;

    public List<CodeDTO> findByGroupCd(String groupCd) {
        String sql = """
                SELECT code_cd, code_nm
                FROM tms_code_m
                WHERE group_cd = :groupCd
                  AND use_yn = '1'
                ORDER BY code_cd
            """;

        Map<String, Object> params = Map.of("groupCd", groupCd);

        return namedJdbc.query(sql, params, (rs, rowNum) ->
                new CodeDTO(
                        rs.getString("code_cd"),
                        rs.getString("code_nm")
                )
        );
    }

    
}
