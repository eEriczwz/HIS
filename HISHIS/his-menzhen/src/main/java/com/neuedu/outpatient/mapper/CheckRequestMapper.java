package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.CheckRequest;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CheckRequestMapper {
    //新增检查申请单
    int insert(CheckRequest checkRequest);
    //根据挂号id查询该患者所有检查单
    List<CheckRequest> selectByRegisterId(@Param("registerId") Integer registerId);
    //根据id查询检查单
    CheckRequest selectById(@Param("id") Integer id);
}
