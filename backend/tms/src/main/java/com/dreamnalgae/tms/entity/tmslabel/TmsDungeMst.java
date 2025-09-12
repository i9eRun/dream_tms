package com.dreamnalgae.tms.entity.tmslabel;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TMS_DUNGE_MST", schema = "TMS_WMS")
@IdClass(TmsDungeMsgId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmsDungeMst {
    @Id
    @Column(name = "USER_CET_CD", length = 14, nullable = false)
    private String userCetCd;

    @Id
    @Column(name = "ROW_SEQ", insertable = false, updatable = false)
    private Long rowSeq;

    @Column(name = "CET_CD", length = 14)
    private String cetCd;

    @Column(name = "ORD_NO", length = 15)
    private String ordNo;   // 전표번호 (DB 함수 SF_TMS_NO로 생성 필요)

    @Column(name = "SUJUM_CD", length = 8)
    private String sujumCd;

    @Column(name = "SUJUM_NM", length = 50)
    private String sujumNm;

    @Column(name = "JIYUK_NM", length = 50)
    private String jiyukNm;

    @Column(name = "COURSE_CD")
    private Long courseCd;

    @Column(name = "TEL_NO", length = 20)
    private String telNo;

    @Column(name = "DUNGE")
    private Integer dunge;

    @Column(name = "CHUL_NM", length = 50)
    private String chulNm;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "BIGO", length = 150)
    private String bigo;

    @Column(name = "DUNGE_DT", length = 8)
    private String dungeDt; // YYYYMMDD 형태 저장

    @Column(name = "OUT_YN", length = 1)
    private String outYn;

    @Column(name = "DAY_GB", length = 2)
    private String dayGb;

    @Column(name = "TRANS_GB", length = 1)
    private String transGb;

    @Column(name = "INSERT_DT", nullable = false, updatable = false, insertable = false)
    private Date insertDt;

    @Builder.Default
    @Column(name = "DREAM_UPDATE", length = 1, nullable = false)
    private String dreamUpdate = "0";

    @Column(name = "CHULPAN_CD", length = 14)
    private String chulpanCd;

    @Column(name = "DAE_NO", length = 10)
    private String daeNo;

    @Column(name = "INSERT_ID", length = 20)
    private String insertId;

    @Column(name = "UPDATE_DT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDt;

    @Column(name = "UPDATE_ID", length = 20)
    private String updateId;
}
