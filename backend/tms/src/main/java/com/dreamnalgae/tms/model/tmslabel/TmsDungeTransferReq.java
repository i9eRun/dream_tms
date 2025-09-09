package com.dreamnalgae.tms.model.tmslabel;

import java.util.List;

import lombok.Data;

@Data
public class TmsDungeTransferReq {
    private List<TmsDungeTransferItem> items;
}
