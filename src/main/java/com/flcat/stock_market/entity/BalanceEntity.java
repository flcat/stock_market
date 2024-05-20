package com.flcat.stock_market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String rt_cd;
    private String msg_cd;
    private String msg1;
    private String ctx_area_fk200;
    private String ctx_area_nk200;
    private String output1;
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
    private String output2;
    private String ovrs_rlzt_pfls_amt;
    private String ovrs_tot_pfls;
    private String rlzt_erng_rt;
    private String tot_evlu_pfls_amt;
    private String tot_pftrt;
    private String frcr_buy_amt_smtl1;
    private String ovrs_rlzt_pfls_amt2;
    private String frcr_buy_amt_smtl2;

    public BalanceEntity(String rt_cd, String msg_cd, String msg1, String ctx_area_fk200, String ctx_area_nk200, String output1, String cano, String acnt_prdt_cd, String prdt_type_cd, String ovrs_pdno, String ovrs_item_name, String frcr_evlu_pfls_amt, String evlu_pfls_rt, String pchs_avg_pric, String ovrs_cblc_qty, String ord_psbl_qty, String frcr_pchs_amt1, String ovrs_stck_evlu_amt, String now_pric2, String tr_crcy_cd, String ovrs_excg_cd, String loan_type_cd, String loan_dt, String expd_dt, String output2, String ovrs_rlzt_pfls_amt, String ovrs_tot_pfls, String rlzt_erng_rt, String tot_evlu_pfls_amt, String tot_pftrt, String frcr_buy_amt_smtl1, String ovrs_rlzt_pfls_amt2, String frcr_buy_amt_smtl2) {
        this.rt_cd = rt_cd;
        this.msg_cd = msg_cd;
        this.msg1 = msg1;
        this.ctx_area_fk200 = ctx_area_fk200;
        this.ctx_area_nk200 = ctx_area_nk200;
        this.output1 = output1;
        this.cano = cano;
        this.acnt_prdt_cd = acnt_prdt_cd;
        this.prdt_type_cd = prdt_type_cd;
        this.ovrs_pdno = ovrs_pdno;
        this.ovrs_item_name = ovrs_item_name;
        this.frcr_evlu_pfls_amt = frcr_evlu_pfls_amt;
        this.evlu_pfls_rt = evlu_pfls_rt;
        this.pchs_avg_pric = pchs_avg_pric;
        this.ovrs_cblc_qty = ovrs_cblc_qty;
        this.ord_psbl_qty = ord_psbl_qty;
        this.frcr_pchs_amt1 = frcr_pchs_amt1;
        this.ovrs_stck_evlu_amt = ovrs_stck_evlu_amt;
        this.now_pric2 = now_pric2;
        this.tr_crcy_cd = tr_crcy_cd;
        this.ovrs_excg_cd = ovrs_excg_cd;
        this.loan_type_cd = loan_type_cd;
        this.loan_dt = loan_dt;
        this.expd_dt = expd_dt;
        this.output2 = output2;
        this.ovrs_rlzt_pfls_amt = ovrs_rlzt_pfls_amt;
        this.ovrs_tot_pfls = ovrs_tot_pfls;
        this.rlzt_erng_rt = rlzt_erng_rt;
        this.tot_evlu_pfls_amt = tot_evlu_pfls_amt;
        this.tot_pftrt = tot_pftrt;
        this.frcr_buy_amt_smtl1 = frcr_buy_amt_smtl1;
        this.ovrs_rlzt_pfls_amt2 = ovrs_rlzt_pfls_amt2;
        this.frcr_buy_amt_smtl2 = frcr_buy_amt_smtl2;
    }
}
