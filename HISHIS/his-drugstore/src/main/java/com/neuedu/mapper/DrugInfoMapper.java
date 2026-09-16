package com.neuedu.mapper;

import com.neuedu.entity.DrugInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DrugInfoMapper {

    //查询全部有效药品 delmark=1
    List<DrugInfo> list(@Param("drugCode") String drugCode, @Param("drugName") String drugName);

    //新增药品
    int add(DrugInfo drugInfo);

    //修改药品
    int update(DrugInfo drugInfo);

    //逻辑删除，修改delmark=0
    int deleteById(Integer id);

    //入库：库存增加
    int stockIn(@Param("id") Integer id, @Param("qty") Integer qty);

    //出库：库存减少（库存不足时返回0）
    int stockOut(@Param("id") Integer id, @Param("qty") Integer qty);
}
