package com.neuedu.charge.controller;

import com.neuedu.charge.dto.FeeDetailDTO;
import com.neuedu.charge.entity.ChargeRecord;
import com.neuedu.charge.service.ChargeService;
import com.neuedu.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Resource
    private ChargeService chargeService;

    /**
     * 根据病历号或挂号 ID 计算费用明细（收费前预览）
     * 返回：registFee / drugTotalFee / checkTotalFee / inspectionTotalFee / disposalTotalFee / totalAmount
     */
    @GetMapping("/fee/{key}")
    public Result<FeeDetailDTO> feeDetail(@PathVariable String key) {
        FeeDetailDTO dto = chargeService.calcFee(key);
        if (dto == null) {
            return Result.error("挂号单不存在，请核对病历号或挂号ID");
        }
        return Result.success(dto);
    }

    /**
     * 收费结算（支持病历号或挂号 ID）
     */
    @PostMapping("/settle/{key}")
    public Result<String> settle(@PathVariable String key) {
        try {
            chargeService.settle(key);
            return Result.success("收费成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 已收费记录列表（退费页使用）
     */
    @GetMapping("/charged/list")
    public Result<List<ChargeRecord>> chargedList(@RequestParam(required = false) String caseNumber,
                                                  @RequestParam(required = false) String realName) {
        return Result.success(chargeService.chargedList(caseNumber, realName));
    }

    /**
     * 退费
     */
    @PostMapping("/refund/{chargeId}")
    public Result<String> refund(@PathVariable Integer chargeId) {
        try {
            chargeService.refund(chargeId);
            return Result.success("退费成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
