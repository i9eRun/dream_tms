package com.dreamnalgae.tms.entity.tmslabel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmsDungeMsgId  implements Serializable {
    private String userCetCd;
    private Long rowSeq;
}

