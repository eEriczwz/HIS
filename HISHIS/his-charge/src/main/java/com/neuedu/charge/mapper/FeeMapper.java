package com.neuedu.charge.mapper;

import com.neuedu.charge.dto.FeeDetailDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 费用计算 + 收费/退费时对业务单据（处方/检查/检验/处置）的状态流转
 */
public interface FeeMapper {

    /**
     * 根据挂号id或病历号计算该挂号单的全部费用明细
     * 挂号单不存在时返回 null
     */
    FeeDetailDTO calcFee(@Param("key") String key);

    /** 收费：处方「已开立」→「已缴费」 */
    int markPrescriptionPaid(@Param("registerId") Integer registerId);

    /** 收费：检查申请单「待检查」→「已缴费」 */
    int markCheckPaid(@Param("registerId") Integer registerId);

    /** 收费：检验申请单「已开立/待检查」→「已缴费」 */
    int markInspectionPaid(@Param("registerId") Integer registerId);

    /** 收费：处置申请单「待处置」→「已缴费」 */
    int markDisposalPaid(@Param("registerId") Integer registerId);
}
