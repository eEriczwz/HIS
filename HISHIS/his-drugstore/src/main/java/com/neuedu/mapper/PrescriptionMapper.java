package com.neuedu.mapper;

import com.neuedu.entity.Prescription;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PrescriptionMapper {
    // 发药列表（联查药品信息与患者信息），state 传 pending 表示待发药
    List<Prescription> selectForDispense(@Param("state") String state,
                                         @Param("caseNumber") String caseNumber,
                                         @Param("drugName") String drugName);

    // 根据id查询处方（发药/退药前需读取药品与数量）
    Prescription selectById(@Param("id") Integer id);

    // 发药：已开立/已缴费 -> 已发药
    int issue(@Param("id") Integer id);

    // 退药：已发药 -> 已退药
    int returnDrug(@Param("id") Integer id);
}
