package com.dreamnalgae.tms.model.tmslabel;

import java.util.List;

import com.dreamnalgae.tms.entity.tmslabel.TmsDungeMst;

import lombok.Data;

@Data
public class TmsDungeMstSaveRequest {
    private String insertId;
    private String userCetCd;
    private String loginCustDivGb;
    private String loginCustName;
    private String loginCustCd;
    private List<TmsDungeMst> insertList;
    private List<TmsDungeMst> updateList;
    private List<TmsDungeMst> deleteList;
}
