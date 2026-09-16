package com.neuedu.charge.service.impl;

import com.neuedu.charge.dto.FeeDetailDTO;
import com.neuedu.charge.entity.ChargeRecord;
import com.neuedu.charge.mapper.ChargeRecordMapper;
import com.neuedu.charge.mapper.FeeMapper;
import com.neuedu.charge.service.ChargeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ChargeServiceImpl implements ChargeService {

    private static final String STATE_CHARGED = "已收费";
    private static final String STATE_REFUNDED = "已退费";

    @Resource
    private FeeMapper feeMapper;

    @Resource
    private ChargeRecordMapper chargeRecordMapper;

    @Override
    public FeeDetailDTO calcFee(String key) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }
        FeeDetailDTO dto = feeMapper.calcFee(key.trim());
        if (dto == null) {
            return null;
        }
        // 合计 = 挂号费 + 药品 + 检验 + 检查 + 处置（空值按0处理）
        dto.setTotalAmount(sum(
                dto.getRegistFee(),
                dto.getDrugTotalFee(),
                dto.getCheckTotalFee(),
                dto.getInspectionTotalFee(),
                dto.getDisposalTotalFee()
        ));
        return dto;
    }

    @Override
    @Transactional
    public void settle(String key) {
        FeeDetailDTO dto = calcFee(key);
        if (dto == null) {
            throw new RuntimeException("挂号单不存在，请核对病历号或挂号ID");
        }
        Integer registerId = dto.getRegisterId();
        // 防止重复收费
        ChargeRecord exist = chargeRecordMapper.selectByRegisterIdAndState(registerId, STATE_CHARGED);
        if (exist != null) {
            throw new RuntimeException("该挂号单已收费，请勿重复收费");
        }
        // 生成收费记录
        ChargeRecord record = new ChargeRecord();
        record.setRegisterId(registerId);
        record.setCaseNumber(dto.getCaseNumber());
        record.setRealName(dto.getRealName());
        record.setRegistFee(dto.getRegistFee());
        record.setDrugTotalFee(dto.getDrugTotalFee());
        record.setCheckTotalFee(dto.getCheckTotalFee());
        record.setInspectionTotalFee(dto.getInspectionTotalFee());
        record.setDisposalTotalFee(dto.getDisposalTotalFee());
        record.setTotalAmount(dto.getTotalAmount());
        record.setChargeState(STATE_CHARGED);
        record.setChargeTime(new Date());
        chargeRecordMapper.insert(record);

        // 推进业务单据状态：开立/待办 → 已缴费
        feeMapper.markPrescriptionPaid(registerId);
        feeMapper.markCheckPaid(registerId);
        feeMapper.markInspectionPaid(registerId);
        feeMapper.markDisposalPaid(registerId);
    }

    @Override
    public List<ChargeRecord> chargedList(String caseNumber, String realName) {
        return chargeRecordMapper.selectChargedList(caseNumber, realName);
    }

    @Override
    @Transactional
    public void refund(Integer chargeId) {
        ChargeRecord record = chargeRecordMapper.selectById(chargeId);
        if (record == null) {
            throw new RuntimeException("收费记录不存在");
        }
        if (!STATE_CHARGED.equals(record.getChargeState())) {
            throw new RuntimeException("该记录当前状态为「" + record.getChargeState() + "」，无法退费");
        }
        chargeRecordMapper.updateToRefunded(chargeId);
    }

    /** null 安全的金额累加 */
    private BigDecimal sum(BigDecimal... amounts) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal a : amounts) {
            if (a != null) {
                total = total.add(a);
            }
        }
        return total;
    }
}
