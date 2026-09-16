package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.DisposalRequest;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DisposalRequestMapper {
    //新增处置申请单
    int insert(DisposalRequest disposalRequest);
    //根据挂号id查询该患者所有处置单
    List<DisposalRequest> selectByRegisterId(@Param("registerId") Integer registerId);
    //根据id查询处置单
    DisposalRequest selectById(@Param("id") Integer id);
}
