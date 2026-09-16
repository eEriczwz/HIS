package com.neuedu.outpatient.mapper;

import org.apache.ibatis.annotations.Param;

public interface DrugInfoMapper {
    // 查询药品库存（用于开立处方时的库存校验）
    Integer selectStockById(@Param("id") Integer id);
}
