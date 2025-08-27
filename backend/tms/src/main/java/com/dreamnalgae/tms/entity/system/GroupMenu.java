package com.dreamnalgae.tms.entity.system;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TMS_GROUP_MENU_M")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GroupMenu {
 
    @Id
    @Column(name = "USER_CET_CD")
    private String userCetCd;

    @Id
    @Column(name = "GROUP_CD")
    private String groupCd;

    @Id
    @Column(name = "MENU_ID")
    private String menuId;

    @Column(name = "INSERT_DT")
    private LocalDateTime insertDt;

    @Column(name = "INSERT_ID")
    private String insertId;

    @Column(name = "UPDATE_DT")
    private LocalDateTime updateDt;

    @Column(name = "UPDATE_ID")
    private String updateId;

}
