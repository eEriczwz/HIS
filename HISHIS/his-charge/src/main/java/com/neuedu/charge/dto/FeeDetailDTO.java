package com.neuedu.charge.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 收费费用明细（返回给前端）
 * 字段名与前端 hissystemui/src/api/charge.js 约定一致：
 *   registFee          挂号费
 *   drugTotalFee       药品总费用
 *   checkTotalFee      检验项目总费用（inspection_request 检验申请单）
 *   inspectionTotalFee 检查项目总费用（check_request 检查申请单）
 *   disposalTotalFee   处置治疗总费用（disposal_request 处置申请单）
 *   totalAmount        合计总金额
 */
@Data
public class FeeDetailDTO {
    private Integer registerId;
    private String caseNumber;
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
}
