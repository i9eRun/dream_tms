package com.dreamnalgae.tms.service.tsub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dreamnalgae.tms.model.tsub.tsub1003.CourseDetailDTO;
import com.dreamnalgae.tms.model.tsub.tsub1003.CourseMasterDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tsub1003Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<CourseMasterDTO> getCourseMaster(String infoDate, String cusGb, String cusAmpmGb, String cusCd) {
        StringBuilder sql = new StringBuilder("""
                    SELECT P.COURSE_CODE,
                        MAX(P.COURSE_NAME) AS COURSE_NAME,
                        P.COMUSER_CODE,
                        U.USER_NM          AS COMUSER_NAME,
                        P.CAR_CODE,
                        C.CAR_REGNUM       AS CAR_REGNUM,
                        SUM(P.INFO_BUSU)   AS TOTAL_BUSU,
                        SUM(P.INFO_DUNG_E) AS TOTAL_DUNG_E
                    FROM PDA2_PSG_PBS_INFO P
                        LEFT JOIN TMS_COM_USER U ON P.COMUSER_CODE = U.USER_ID
                        LEFT JOIN TMS_CAR C      ON P.CAR_CODE     = C.CAR_CD
                        LEFT JOIN TMS_CUS TC     ON P.COURSE_CODE  = TC.CUS_CD
                    WHERE 1=1
                """);

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(infoDate)) {
            sql.append(" AND P.INFO_DATE = :infoDate ");
            params.put("infoDate", infoDate);
        }

        if (StringUtils.hasText(cusGb)) {
            sql.append(" AND TC.CUS_GB = :cusGb ");
            params.put("cusGb", cusGb);
        }

        if (StringUtils.hasText(cusAmpmGb)) {
            sql.append(" AND TC.CUS_AMPM_GB = :cusAmpmGb ");
            params.put("cusAmpmGb", cusAmpmGb);
        }

        if (StringUtils.hasText(cusCd)) {
            sql.append(" AND P.COURSE_CODE = :cusCd ");
            params.put("cusCd", cusCd);
        }

        sql.append("""
            GROUP BY P.COURSE_CODE, P.COMUSER_CODE, U.USER_NM, P.CAR_CODE, C.CAR_REGNUM
            ORDER BY TO_NUMBER(P.COURSE_CODE)
        """);

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(CourseMasterDTO.class));
    }


    public List<CourseDetailDTO> getCourseDetail(String infoDate, String courseCode) {
        String sql = """
            SELECT P.INFO_DATE,
                P.COURSE_CODE,
                P.INFO_GEOCODE,
                P.INFO_WORKGUBUN,
                P.INFO_SUNBUN,
                P.INFO_GUBUN,
                P.INFO_BUSU,
                P.INFO_DUNG_E,
                P.INFO_WORK,
                P.INFO_WORK_NM,
                P.INFO_DEPT_TIME,
                P.INFO_INITALING_SIGN,
                P.INFO_CUST_SIGN,
                P.INFO_ARRIVE_TIME,
                P.INFO_MOVE_TIME,
                P.COURSE_NAME,
                P.COMUSER_CODE,
                U.USER_NM          AS COMUSER_NAME,
                P.CAR_CODE,
                C.CAR_REGNUM       AS CAR_REGNUM,
                P.SUBAESONG_MEMO,
                P.INFO_IKWAN,
                CU.CUST_ABBR_NM    AS CUST_NAME
            FROM PDA2_PSG_PBS_INFO P
                LEFT JOIN TMS_COM_USER U ON P.COMUSER_CODE = U.USER_ID
                LEFT JOIN TMS_CAR C      ON P.CAR_CODE     = C.CAR_CD
                LEFT JOIN TMS_CUST CU    ON P.INFO_GEOCODE = CU.CUST_CD
            WHERE P.INFO_DATE   = :infoDate
            AND P.COURSE_CODE = :courseCode
            ORDER BY TO_NUMBER(P.COURSE_CODE)
        """;

        Map<String, Object> params = Map.of(
            "infoDate", infoDate,
            "courseCode", courseCode
        );

        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(CourseDetailDTO.class));
    }





    
}
