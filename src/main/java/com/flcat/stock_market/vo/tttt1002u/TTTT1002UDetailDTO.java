package com.flcat.stock_market.vo.tttt1002u;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TTTT1002UDetailDTO {
    private String cano;
    private String acnt_prdt_cd;
    private String prdt_type_cd;
    private String ovrs_pdno;
    private String ovrs_item_name;
    private String frcr_evlu_pfls_amt;
    private String evlu_pfls_rt;
    private String pchs_avg_pric;
    private String ovrs_cblc_qty;
    private String ord_psbl_qty;
    private String frcr_pchs_amt1;
    private String ovrs_stck_evlu_amt;
    private String now_pric2;
    private String tr_crcy_cd;
    private String ovrs_excg_cd;
    private String loan_type_cd;
    private String loan_dt;
    private String expd_dt;
    private String ovrs_rlzt_pfls_amt;
    private String ovrs_tot_pfls;
    private String rlzt_erng_rt;
    private String ttot_evlu_pflss_amt;
    private String tot_pftrt;
    private String frcr_buy_amt_smtl1;
    private String ovrs_rlzt_pfls_amt2;
    private String frcr_buy_amt_smtl2;



}
