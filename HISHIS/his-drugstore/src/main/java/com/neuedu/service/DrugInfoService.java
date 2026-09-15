package com.neuedu.service;

import com.neuedu.entity.DrugInfo;
import java.util.List;

public interface DrugInfoService {
    List<DrugInfo> list(String drugCode, String drugName);
    int add(DrugInfo drugInfo);
    int update(DrugInfo drugInfo);
    int deleteById(Integer id);
}
