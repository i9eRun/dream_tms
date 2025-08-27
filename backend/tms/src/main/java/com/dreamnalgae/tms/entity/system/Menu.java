package com.dreamnalgae.tms.entity.system;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TMS_MENU_M")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @Column(name = "MENU_SEQ")
    private Long menuSeq;

    @Column(name = "USER_CET_CD")
    private String userCetCd;

    @Column(name = "MENU_ID")
    private String menuId;

    @Column(name = "MENU_NM")
    private String menuNm;

    @Column(name = "MENU_LEVEL")
    private Integer menuLevel;

    @Column(name = "MENU_ICON")
    private String menuIcon;

    @Column(name = "PARENT_MENU_ID")
    private String parentMenuId;

    @Column(name = "PGM_YN")
    private String pgmYn;

    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "EXEC_URL")
    private String execUrl;

    @Column(name = "EXEC_TYPE")
    private String execType;

    @Column(name = "MENU_PARAM")
    private String menuParam;

    @Column(name = "MENU_ORD")
    private Integer menuOrd;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "INSERT_DT")
    private LocalDateTime insertDt;

    @Column(name = "INSERT_ID")
    private String insertId;

    @Column(name = "UPDATE_DT")
    private LocalDateTime updateDt;

    @Column(name = "UPDATE_ID")
    private String updateId;

    @Column(name = "RUN_TYPE")
    private String runType;

    @Column(name = "LOCK_TYPE")
    private String lockType;

    // @Column(name = "XTYPE")
    // private String xtype;

    @Transient
    private List<Menu> children;

    @Transient
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    @Transient
    public boolean isExpanded() {
        return true;
    }

    @Transient
    @Builder.Default
    private boolean checked = false; // ✅ 체크박스 표시용 필드
}
