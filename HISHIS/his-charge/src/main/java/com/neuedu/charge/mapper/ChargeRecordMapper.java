package com.neuedu.charge.mapper;

import com.neuedu.charge.entity.ChargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收费记录表 charge_record 的增删改查
 */
public interface ChargeRecordMapper {

    int insert(ChargeRecord record);

    ChargeRecord selectById(@Param("id") Integer id);

    /** 查询某挂号单是否已存在「已收费」记录（防止重复收费） */
    ChargeRecord selectByRegisterIdAndState(@Param("registerId") Integer registerId,
                                           @Param("chargeState") String chargeState);

    /** 已收费记录列表（退费页使用），支持按病历号/姓名过滤 */
    List<ChargeRecord> selectChargedList(@Param("caseNumber") String caseNumber,
                                         @Param("realName") String realName);

    /** 退费：状态「已收费」→「已退费」，记录退费时间 */
    int updateToRefunded(@Param("id") Integer id);
}
