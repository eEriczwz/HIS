package com.neuedu.charge.service;

import com.neuedu.charge.dto.FeeDetailDTO;
import com.neuedu.charge.entity.ChargeRecord;

import java.util.List;

public interface ChargeService {

    /** 根据挂号id或病历号计算费用明细（不落库，纯查询） */
    FeeDetailDTO calcFee(String key);

    /** 收费结算：生成收费记录并推进单据状态 */
    void settle(String key);

    /** 已收费记录列表 */
    List<ChargeRecord> chargedList(String caseNumber, String realName);

    /** 退费 */
    void refund(Integer chargeId);
}
