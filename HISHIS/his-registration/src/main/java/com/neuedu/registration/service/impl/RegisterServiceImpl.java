package com.neuedu.registration.service.impl;

import com.neuedu.registration.dto.RegisterDTO;
import com.neuedu.registration.dto.RegisterQueryDTO;
import com.neuedu.registration.entity.Register;
import com.neuedu.registration.mapper.RegisterMapper;
import com.neuedu.registration.service.RegisterService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private RegisterMapper registerMapper;

    @Override
    public String createRegister(RegisterDTO dto) {
        Register reg = new Register();
        // 病历号在拿到数据库自增 id 后再生成，先不赋值（case_number 允许为 null）
        reg.setRealName(dto.getRealName());
        reg.setGender(dto.getGender());
        reg.setCardNumber(dto.getCardNumber());
        reg.setBirthday(dto.getBirthday());
        reg.setAge(dto.getAge());
        reg.setAgeType(dto.getAgeType());
        reg.setHomeAddress(dto.getHomeAddress());
        reg.setVisitDate(dto.getVisitDate());
        reg.setNoon(dto.getNoon());
        reg.setDeptmentId(dto.getDeptmentId());
        reg.setEmployeeId(dto.getEmployeeId());
        reg.setRegistLevelId(dto.getRegistLevelId());
        reg.setSettleCategoryId(dto.getSettleCategoryId());
        reg.setIsBook(dto.getIsBook());
        reg.setRegistMethod(dto.getRegistMethod());
        reg.setRegistMoney(dto.getRegistMoney());
        reg.setVisitState(1); //1=已挂号（新建挂号默认）
        registerMapper.insert(reg); // useGeneratedKeys 回填 reg.id

        // 病历号 = 当前最大病历号 + 1，保证连续（首单从 1000001 开始）
        Integer id = reg.getId();
        int maxCase = registerMapper.selectMaxCaseNumber();
        int base = maxCase > 0 ? maxCase : 1000000;
        String caseNumber = String.valueOf(base + 1);
        registerMapper.updateCaseNumber(id, caseNumber);
        return caseNumber;
    }

    @Override
    public List<Register> getList(RegisterQueryDTO queryDTO) {
        return registerMapper.selectList(queryDTO);
    }

    @Override
    public Register getById(Integer id) {
        return registerMapper.selectById(id);
    }

    @Override
    public boolean changeState(Integer id, Integer visitState) {
        return registerMapper.updateVisitState(id,visitState) >0;
    }
}
