package com.neuedu.charge.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费记录（charge_record 表）
 * 一次收费 = 一条记录；退费时将该记录状态置为「已退费」
 */
@Data
public class ChargeRecord {
    /** 收费单号 */
    private Integer id;
    /** 挂号id */
    private Integer registerId;
    /** 病历号 */
    private String caseNumber;
    /** 姓名 */
    private String realName;
    /** 挂号费 */
    private BigDecimal registFee;
    /** 药品总费用 */
    private BigDecimal drugTotalFee;
    /** 检验项目总费用 */
    private BigDecimal checkTotalFee;
    /** 检查项目总费用 */
    private BigDecimal inspectionTotalFee;
    /** 处置治疗总费用 */
    private BigDecimal disposalTotalFee;
    /** 合计总金额 */
    private BigDecimal totalAmount;
    /** 状态：已收费 / 已退费 */
    private String chargeState;
    /** 收费时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date chargeTime;
    /** 退费时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;
}
