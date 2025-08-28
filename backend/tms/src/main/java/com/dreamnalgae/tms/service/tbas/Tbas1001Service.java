package com.dreamnalgae.tms.service.tbas;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.tbas.TmsCustInfoVO;
import com.dreamnalgae.tms.model.tbas.TmsCustVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tbas1001Service {
    private final NamedParameterJdbcTemplate jdbc;

    public List<TmsCustInfoVO> selectCustList(TmsCustInfoVO vo) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("    T.USER_CET_CD, ");
        sql.append("    T.CUST_CD, ");
        sql.append("    T.CUST_GB, ");
        sql.append("    T.CUST_DIV_GB, ");
        sql.append("    T.CUST_ABBR_NM, ");
        sql.append("    T.CUST_NM, ");
        sql.append("    T.BIZ_REG_NO, ");
        sql.append("    T.DAEPYO_NM, ");
        sql.append("    T.UPTAE_NM, ");
        sql.append("    T.JONGMOK_NM, ");
        sql.append("    T.TEL_NO, ");
        sql.append("    T.FAX_NO, ");
        sql.append("    T.EMAIL_ADDR, ");
        sql.append("    T.POST_NO, ");
        sql.append("    T.ADDR1, ");
        sql.append("    T.ADDR2, ");
        sql.append("    T.TRADE_FR_DT, ");
        sql.append("    T.TRADE_TO_DT, ");
        sql.append("    T.SINYONG_AMT, ");
        sql.append("    T.MISU_AMT, ");
        sql.append("    T.MAGAM_DD, ");
        sql.append("    T.CONTR_STORE_YN, ");
        sql.append("    T.JIYUK_NM, ");
        sql.append("    T.DELIV_PATH_CD, ");
        sql.append("    C1.CUS_NM AS DELIV_CAR_NO, ");
        sql.append("    T.DELIV_POS_CD, ");
        sql.append("    T.CHARGE_EMP_ID, ");
        sql.append("    U.USER_NM AS CHARGE_EMP_NM, ");
        sql.append("    T.BIGO, ");
        sql.append("    T.SALE_TYPE_GB, ");
        sql.append("    T.GULJAE_GB, ");
        sql.append("    T.CHULGO_GB, ");
        sql.append("    T.SOSOK_NM, ");
        sql.append("    T.BANPOM_YN, ");
        sql.append("    T.CUST_DIV_CD1, ");
        sql.append("    T.CUST_DIV_CD2, ");
        sql.append("    T.BILL_YN, ");
        sql.append("    T.DM_SEND_YN, ");
        sql.append("    T.BIZ_NM, ");
        sql.append("    T.TEAM_CD, ");
        sql.append("    T.TEAM_NM, ");
        sql.append("    T.DAMDANG_NM, ");
        sql.append("    T.DELIV_DIV_CD, ");
        sql.append("    T.BUNJI_PRT_YN, ");
        sql.append("    T.SEND_EMAIL_ADDR, ");
        sql.append("    T.WORK_USER_ID, ");
        sql.append("    T.WORK_DT, ");
        sql.append("    T.USE_YN, ");
        sql.append("    T.BANP_DAMDANG_NM, ");
        sql.append("    T.BANP_TEAM_CD, ");
        sql.append("    T.BANP_TEAM_NM, ");
        sql.append("    T.ORD_METHOD_GB, ");
        sql.append("    T.BOK_INI_BOKWAN_AMT, ");
        sql.append("    T.CHUNGU_AMT_PRT_YN, ");
        sql.append("    T.CHULPAN_SUB_CD, ");
        sql.append("    T.BANP_EMAIL_ADDR, ");
        sql.append("    T.BONSA_SALE_NM, ");
        sql.append("    T.BOX_AUTO_YN, ");
        sql.append("    T.HOMEPAGE_ADDR, ");
        sql.append("    T.HOMENAME_CUST_NM, ");
        sql.append("    T.HOMEPAGE_USER_ID, ");
        sql.append("    T.HOMEPAGE_YN, ");
        sql.append("    T.BAEBON_CD, ");
        sql.append("    T.SQUARE_SIZE, ");
        sql.append("    T.GARO_SIZE, ");
        sql.append("    T.SERO_SIZE, ");
        sql.append("    T.DEPTH_SIZE, ");
        sql.append("    T.RECV_POST_NO, ");
        sql.append("    T.RECV_ADDR1, ");
        sql.append("    T.RECV_DAMDANG_NM, ");
        sql.append("    T.RECV_NICK_NM, ");
        sql.append("    T.WEB_ORD_GB, ");
        sql.append("    T.CHULGO_SORT_GB, ");
        sql.append("    T.CHUNGU_DAMDANG_NM, ");
        sql.append("    T.CHUNGU_NICK_NM, ");
        sql.append("    T.JAKUP_IND_CNT, ");
        sql.append("    T.BOX_BOK_QTY_YN, ");
        sql.append("    T.BOK_WMS_CNV_YN, ");
        sql.append("    T.TAKBAE_CD, ");
        sql.append("    T.PARENT_CUST_CD, ");
        sql.append("    T.MGT_LEVEL, ");
        sql.append("    T.DAEPYO_EMAIL_ADDR, ");
        sql.append("    T.DAEPYO_NM_YN, ");
        sql.append("    T.DAEPYO_CP_TEL_NO, ");
        sql.append("    T.ACC_CD, ");
        sql.append("    T.ACC_EMAIL_ADDR, ");
        sql.append("    T.BANP_AUTO_JAES_GB, ");
        sql.append("    T.BANP_TRUST_YN, ");
        sql.append("    T.INSERT_DT, ");
        sql.append("    T.INSERT_ID, ");
        sql.append("    T.UPDATE_DT, ");
        sql.append("    T.UPDATE_ID, ");
        sql.append("    T.PISS_SCH_USE, ");
        sql.append("    T.BOK_PANSWAE_YN, ");
        sql.append("    T.SCAN_YN, ");
        sql.append("    T.TOT_SCAN_YN, ");
        sql.append("    T.ORD_METHOD_GB_D, ");
        sql.append("    T.PANSWAE_DT, ");
        sql.append("    T.SALES_DAMDANG_ID, ");
        sql.append("    T.BOK_PART, ");
        sql.append("    T.DREAM_BIGO, ");
        sql.append("    T.PISS_END_DT, ");
        sql.append("    T.MAIN_SALE, ");
        sql.append("    T.SUNLBL_USE_YN, ");
        sql.append("    T.PRT_GB, ");
        sql.append("    T.TEL_NO2, ");
        sql.append("    T.EXPRESS_DELIV_PATH_CD, ");
        sql.append("    C2.CUS_NM AS EXPRESS_DELIV_CAR_NO, ");
        sql.append("    T.EXPRESS_DELIV_POS_CD, ");
        sql.append("    T.JAEBUL_TRUST_YN, ");
        sql.append("    T.JULPAN_TRUST_YN, ");
        sql.append("    T.JAEBUL_TRUST_DT, ");
        sql.append("    T.PLT_USE_KPP, ");
        sql.append("    T.PLT_USE_AJ, ");
        sql.append("    T.PLT_USE_NG, ");
        sql.append("    T.PLT_USE_EX, ");
        sql.append("    T.CNV_STD_CD, ");
        sql.append("    T.CUST_X, ");
        sql.append("    T.CUST_Y, ");
        sql.append("    T.CUST_X3, ");
        sql.append("    T.SALES_MANAGER_NM, ");
        sql.append("    T.PAYMENT_MANAGER_NM, ");
        sql.append("    T.ORDER_MANAGER_NM, ");
        sql.append("    T.SALES_MANAGER_TEL, ");
        sql.append("    T.PAYMENT_MANAGER_TEL, ");
        sql.append("    T.ORDER_MANAGER_TEL ");
        sql.append("FROM TMS_CUST T ");
        sql.append("LEFT JOIN TMS_COM_USER U ON T.CHARGE_EMP_ID = U.USER_ID ");
        sql.append("LEFT JOIN TMS_CUS C1 ON T.DELIV_PATH_CD = C1.CUS_CD ");
        sql.append("LEFT JOIN TMS_CUS C2 ON T.EXPRESS_DELIV_PATH_CD = C2.CUS_CD ");
        sql.append("WHERE T.USER_CET_CD = :userCetCd ");

        // 조건절
        if (vo.getCustCd() != null && !vo.getCustCd().isEmpty()) {
        sql.append(" AND T.CUST_CD = :custCd ");
        } else {
            if (vo.getCustNm() != null && !vo.getCustNm().isEmpty()) {
                sql.append(" AND (T.CUST_NM LIKE '%' || :custNm || '%' OR T.CUST_ABBR_NM LIKE '%' || :custNm || '%') ");
            }
            if (vo.getUseYn() != null && !vo.getUseYn().isEmpty()) {
                sql.append(" AND T.USE_YN = :useYn ");
            }
            if (vo.getTradeEndYn() != null && !vo.getTradeEndYn().isEmpty()) {
                if ("1".equals(vo.getTradeEndYn())) {
                    sql.append(" AND T.TRADE_TO_DT IS NOT NULL ");
                } else {
                    sql.append(" AND T.TRADE_TO_DT IS NULL ");
                }
            }
            if (vo.getCustDivGb() != null && !vo.getCustDivGb().isEmpty()) {
                sql.append(" AND T.CUST_DIV_GB = :custDivGb ");
            }
        }

        sql.append(" ORDER BY DECODE(INSTR(T.CUST_NM, :custNm), 0, 999999, INSTR(T.CUST_NM, :custNm)), T.CUST_NM, T.CUST_ABBR_NM ");

        SqlParameterSource params = new BeanPropertySqlParameterSource(vo);

        System.out.println(sql.toString());

        return jdbc.query(sql.toString(), params, new BeanPropertyRowMapper<>(TmsCustInfoVO.class));
    }
    

    @Transactional
    public boolean insertCust(TmsCustVO vo) {
         String sql = """
            INSERT INTO TMS_CUST (
                USER_CET_CD, CUST_CD, CUST_GB, CUST_DIV_GB, CUST_ABBR_NM, CUST_NM, BIZ_REG_NO, DAEPYO_NM,
                UPTAE_NM, JONGMOK_NM, TEL_NO, FAX_NO, EMAIL_ADDR, POST_NO, ADDR1, ADDR2,
                TRADE_FR_DT, TRADE_TO_DT, SINYONG_AMT, MISU_AMT, MAGAM_DD, CONTR_STORE_YN, JIYUK_NM,
                DELIV_PATH_CD, DELIV_POS_CD, CHARGE_EMP_ID, BIGO, SALE_TYPE_GB, GULJAE_GB, CHULGO_GB,
                SOSOK_NM, BANPOM_YN, CUST_DIV_CD1, CUST_DIV_CD2, BILL_YN, DM_SEND_YN, BIZ_NM, TEAM_CD,
                TEAM_NM, DAMDANG_NM, DELIV_DIV_CD, BUNJI_PRT_YN, SEND_EMAIL_ADDR, WORK_USER_ID,
                WORK_DT, USE_YN, BANP_DAMDANG_NM, BANP_TEAM_CD, BANP_TEAM_NM, ORD_METHOD_GB,
                BOK_INI_BOKWAN_AMT, CHUNGU_AMT_PRT_YN, CHULPAN_SUB_CD, BANP_EMAIL_ADDR,
                BONSA_SALE_NM, BOX_AUTO_YN, HOMEPAGE_ADDR, HOMENAME_CUST_NM, HOMEPAGE_USER_ID,
                HOMEPAGE_YN, BAEBON_CD, SQUARE_SIZE, GARO_SIZE, SERO_SIZE, DEPTH_SIZE, RECV_POST_NO,
                RECV_ADDR1, RECV_DAMDANG_NM, RECV_NICK_NM, WEB_ORD_GB, CHULGO_SORT_GB,
                CHUNGU_DAMDANG_NM, CHUNGU_NICK_NM, JAKUP_IND_CNT, BOX_BOK_QTY_YN, BOK_WMS_CNV_YN,
                TAKBAE_CD, PARENT_CUST_CD, MGT_LEVEL, DAEPYO_EMAIL_ADDR, DAEPYO_NM_YN, DAEPYO_CP_TEL_NO,
                ACC_CD, ACC_EMAIL_ADDR, BANP_AUTO_JAES_GB, BANP_TRUST_YN, INSERT_DT, INSERT_ID,
                PISS_SCH_USE, BOK_PANSWAE_YN, SCAN_YN, TOT_SCAN_YN, ORD_METHOD_GB_D,
                PANSWAE_DT, SALES_DAMDANG_ID, BOK_PART, DREAM_BIGO, PISS_END_DT, MAIN_SALE,
                SUNLBL_USE_YN, PRT_GB, TEL_NO2, EXPRESS_DELIV_PATH_CD, EXPRESS_DELIV_POS_CD,
                JAEBUL_TRUST_YN, JULPAN_TRUST_YN, JAEBUL_TRUST_DT, PLT_USE_KPP, PLT_USE_AJ,
                PLT_USE_NG, PLT_USE_EX, CNV_STD_CD, CUST_X, CUST_Y, CUST_X3, CUST_Y3,
                SALES_MANAGER_NM, PAYMENT_MANAGER_NM, ORDER_MANAGER_NM, SALES_MANAGER_TEL, PAYMENT_MANAGER_TEL, ORDER_MANAGER_TEL
            ) VALUES (
                :userCetCd, :custCd, :custGb, :custDivGb, :custAbbrNm, :custNm, :bizRegNo, :daepyoNm,
                :uptaeNm, :jongmokNm, :telNo, :faxNo, :emailAddr, :postNo, :addr1, :addr2,
                :tradeFrDt, :tradeToDt, :sinyongAmt, :misuAmt, :magamDd, :contrStoreYn, :jiyukNm,
                :delivPathCd, :delivPosCd, :chargeEmpId, :bigo, :saleTypeGb, :guljaeGb, :chulgoGb,
                :sosokNm, :banpomYn, :custDivCd1, :custDivCd2, :billYn, :dmSendYn, :bizNm, :teamCd,
                :teamNm, :damdangNm, :delivDivCd, :bunjiPrtYn, :sendEmailAddr, :workUserId,
                :workDt, :useYn, :banpDamdangNm, :banpTeamCd, :banpTeamNm, :ordMethodGb,
                :bokIniBokwanAmt, :chunguAmtPrtYn, :chulpanSubCd, :banpEmailAddr,
                :bonsaSaleNm, :boxAutoYn, :homepageAddr, :homenameCustNm, :homepageUserId,
                :homepageYn, :baebonCd, :squareSize, :garoSize, :seroSize, :depthSize, :recvPostNo,
                :recvAddr1, :recvDamdangNm, :recvNickNm, :webOrdGb, :chulgoSortGb,
                :chunguDamdangNm, :chunguNickNm, :jakupIndCnt, :boxBokQtyYn, :bokWmsCnvYn,
                :takbaeCd, :parentCustCd, :mgtLevel, :daepyoEmailAddr, :daepyoNmYn, :daepyoCpTelNo,
                :accCd, :accEmailAddr, :banpAutoJaesGb, :banpTrustYn, :insertDt, :insertId,
                :pissSchUse, :bokPanswaeYn, :scanYn, :totScanYn, :ordMethodGbD,
                :panswaeDt, :salesDamdangId, :bokPart, :dreamBigo, :pissEndDt, :mainSale,
                :sunlblUseYn, :prtGb, :telNo2, :expressDelivPathCd, :expressDelivPosCd,
                :jaebulTrustYn, :julpanTrustYn, :jaebulTrustDt, :pltUseKpp, :pltUseAj,
                :pltUseNg, :pltUseEx, :cnvStdCd, :custX, :custY, :custX3, :custY3,
                :salesManagerNm, :paymentManagerNm, :orderManagerNm, :salesManagerTel, :paymentManagerTel, :orderManagerTel
            )
        """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }


    @Transactional
    public boolean updateCust(TmsCustVO vo) {
        String sql = """
            UPDATE TMS_CUST SET
                CUST_GB = :custGb,
                CUST_DIV_GB = :custDivGb,
                CUST_ABBR_NM = :custAbbrNm,
                CUST_NM = :custNm,
                BIZ_REG_NO = :bizRegNo,
                DAEPYO_NM = :daepyoNm,
                UPTAE_NM = :uptaeNm,
                JONGMOK_NM = :jongmokNm,
                TEL_NO = :telNo,
                FAX_NO = :faxNo,
                EMAIL_ADDR = :emailAddr,
                POST_NO = :postNo,
                ADDR1 = :addr1,
                ADDR2 = :addr2,
                TRADE_FR_DT = :tradeFrDt,
                TRADE_TO_DT = :tradeToDt,
                SINYONG_AMT = :sinyongAmt,
                MISU_AMT = :misuAmt,
                MAGAM_DD = :magamDd,
                CONTR_STORE_YN = :contrStoreYn,
                JIYUK_NM = :jiyukNm,
                DELIV_PATH_CD = :delivPathCd,
                DELIV_POS_CD = :delivPosCd,
                CHARGE_EMP_ID = :chargeEmpId,
                BIGO = :bigo,
                SALE_TYPE_GB = :saleTypeGb,
                GULJAE_GB = :guljaeGb,
                CHULGO_GB = :chulgoGb,
                SOSOK_NM = :sosokNm,
                BANPOM_YN = :banpomYn,
                CUST_DIV_CD1 = :custDivCd1,
                CUST_DIV_CD2 = :custDivCd2,
                BILL_YN = :billYn,
                DM_SEND_YN = :dmSendYn,
                BIZ_NM = :bizNm,
                TEAM_CD = :teamCd,
                TEAM_NM = :teamNm,
                DAMDANG_NM = :damdangNm,
                DELIV_DIV_CD = :delivDivCd,
                BUNJI_PRT_YN = :bunjiPrtYn,
                SEND_EMAIL_ADDR = :sendEmailAddr,
                WORK_USER_ID = :workUserId,
                WORK_DT = :workDt,
                USE_YN = :useYn,
                BANP_DAMDANG_NM = :banpDamdangNm,
                BANP_TEAM_CD = :banpTeamCd,
                BANP_TEAM_NM = :banpTeamNm,
                ORD_METHOD_GB = :ordMethodGb,
                BOK_INI_BOKWAN_AMT = :bokIniBokwanAmt,
                CHUNGU_AMT_PRT_YN = :chunguAmtPrtYn,
                CHULPAN_SUB_CD = :chulpanSubCd,
                BANP_EMAIL_ADDR = :banpEmailAddr,
                BONSA_SALE_NM = :bonsaSaleNm,
                BOX_AUTO_YN = :boxAutoYn,
                HOMEPAGE_ADDR = :homepageAddr,
                HOMENAME_CUST_NM = :homenameCustNm,
                HOMEPAGE_USER_ID = :homepageUserId,
                HOMEPAGE_YN = :homepageYn,
                BAEBON_CD = :baebonCd,
                SQUARE_SIZE = :squareSize,
                GARO_SIZE = :garoSize,
                SERO_SIZE = :seroSize,
                DEPTH_SIZE = :depthSize,
                RECV_POST_NO = :recvPostNo,
                RECV_ADDR1 = :recvAddr1,
                RECV_DAMDANG_NM = :recvDamdangNm,
                RECV_NICK_NM = :recvNickNm,
                WEB_ORD_GB = :webOrdGb,
                CHULGO_SORT_GB = :chulgoSortGb,
                CHUNGU_DAMDANG_NM = :chunguDamdangNm,
                CHUNGU_NICK_NM = :chunguNickNm,
                JAKUP_IND_CNT = :jakupIndCnt,
                BOX_BOK_QTY_YN = :boxBokQtyYn,
                BOK_WMS_CNV_YN = :bokWmsCnvYn,
                TAKBAE_CD = :takbaeCd,
                PARENT_CUST_CD = :parentCustCd,
                MGT_LEVEL = :mgtLevel,
                DAEPYO_EMAIL_ADDR = :daepyoEmailAddr,
                DAEPYO_NM_YN = :daepyoNmYn,
                DAEPYO_CP_TEL_NO = :daepyoCpTelNo,
                ACC_CD = :accCd,
                ACC_EMAIL_ADDR = :accEmailAddr,
                BANP_AUTO_JAES_GB = :banpAutoJaesGb,
                BANP_TRUST_YN = :banpTrustYn,
                UPDATE_DT = :updateDt,
                UPDATE_ID = :updateId,
                PISS_SCH_USE = :pissSchUse,
                BOK_PANSWAE_YN = :bokPanswaeYn,
                SCAN_YN = :scanYn,
                TOT_SCAN_YN = :totScanYn,
                ORD_METHOD_GB_D = :ordMethodGbD,
                PANSWAE_DT = :panswaeDt,
                SALES_DAMDANG_ID = :salesDamdangId,
                BOK_PART = :bokPart,
                DREAM_BIGO = :dreamBigo,
                PISS_END_DT = :pissEndDt,
                MAIN_SALE = :mainSale,
                SUNLBL_USE_YN = :sunlblUseYn,
                PRT_GB = :prtGb,
                TEL_NO2 = :telNo2,
                EXPRESS_DELIV_PATH_CD = :expressDelivPathCd,
                EXPRESS_DELIV_POS_CD = :expressDelivPosCd,
                JAEBUL_TRUST_YN = :jaebulTrustYn,
                JULPAN_TRUST_YN = :julpanTrustYn,
                JAEBUL_TRUST_DT = :jaebulTrustDt,
                PLT_USE_KPP = :pltUseKpp,
                PLT_USE_AJ = :pltUseAj,
                PLT_USE_NG = :pltUseNg,
                PLT_USE_EX = :pltUseEx,
                CNV_STD_CD = :cnvStdCd,
                CUST_X = :custX,
                CUST_Y = :custY,
                CUST_X3 = :custX3,
                CUST_Y3 = :custY3,
                SALES_MANAGER_NM = :salesManagerNm,
                PAYMENT_MANAGER_NM = :paymentManagerNm,
                ORDER_MANAGER_NM = :orderManagerNm,
                SALES_MANAGER_TEL = :salesManagerTel,
                PAYMENT_MANAGER_TEL = :paymentManagerTel,
                ORDER_MANAGER_TEL = :orderManagerTel
            WHERE USER_CET_CD = :userCetCd AND CUST_CD = :custCd
        """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(vo)) > 0;
    }


    @Transactional
    public boolean deleteCust(String userCetCd, String custCd) {
        String sql = "DELETE FROM TMS_CUST WHERE USER_CET_CD = :userCetCd AND CUST_CD = :custCd";

        Map<String, Object> params = Map.of(
            "userCetCd", userCetCd,
            "custCd", custCd
        );

        return jdbc.update(sql, params) > 0;
    }


}
