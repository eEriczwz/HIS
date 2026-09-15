package com.neuedu.registration.mapper;

import com.neuedu.registration.entity.Register;
import com.neuedu.registration.dto.RegisterQueryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface RegisterMapper {
    //新增挂号
    int insert(Register register);
    //条件分页列表
    List<Register> selectList(@Param("query") RegisterQueryDTO query);
    //根据id查询挂号
    Register selectById(Integer id);
    //修改就诊状态 visitState
    int updateVisitState(@Param("id")Integer id,@Param("visitState")Integer visitState);
}
