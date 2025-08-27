package com.dreamnalgae.tms.controller.tsub;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.tsub.tsub1003.CourseDetailDTO;
import com.dreamnalgae.tms.model.tsub.tsub1003.CourseMasterDTO;
import com.dreamnalgae.tms.service.tsub.Tsub1003Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tsub/1003")
public class Tsub1003Controller {
    private final Tsub1003Service tsub1003Service;

    @GetMapping("/list")
    public List<CourseMasterDTO> getCourseMaster(
        @RequestParam String infoDate,
        @RequestParam(required = false, defaultValue = "") String cusGb,
        @RequestParam(required = false, defaultValue = "") String cusAmpmGb,
        @RequestParam(required = false, defaultValue = "") String cusCd) {
        return tsub1003Service.getCourseMaster(infoDate, cusGb, cusAmpmGb, cusCd);
    }

    @GetMapping("/detail")
    public List<CourseDetailDTO> getCourseDetail(
        @RequestParam String infoDate,
        @RequestParam String courseCode
    ) {
        return tsub1003Service.getCourseDetail(infoDate, courseCode);
    }
    
}
