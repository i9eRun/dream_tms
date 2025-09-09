package com.dreamnalgae.tms.model.tmslabel;


import java.util.List;

import lombok.Data;

@Data
public class LabelPrintRequest {
    private String userCetCd;
    private String updateId;
    private int copiesPerItem = 2; // 한쌍(2부)
    private boolean updateOutYn = true;
    private List<LabelItem> items;
}
