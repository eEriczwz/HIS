package com.neuedu.registration.service.impl;

import com.neuedu.registration.dto.RegisterDTO;
import com.neuedu.registration.dto.RegisterQueryDTO;
import com.neuedu.registration.entity.Register;
import com.neuedu.registration.mapper.RegisterMapper;
import com.neuedu.registration.service.RegisterService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private RegisterMapper registerMapper;

    @Override
    public String createRegister(RegisterDTO dto) {
        Register reg = new Register();
        //生成病历/挂号单号
        String caseNumber = "REG" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        reg.setCaseNumber(caseNumber);
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
        reg.setVisitState(0); //0=未就诊（新建挂号默认）
        registerMapper.insert(reg);
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
