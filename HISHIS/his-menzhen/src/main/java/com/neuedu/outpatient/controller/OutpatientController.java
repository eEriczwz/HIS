package com.neuedu.outpatient.controller;

import com.alibaba.fastjson2.JSON;
import com.neuedu.common.Result;
import com.neuedu.outpatient.entity.MedicalRecord;
import com.neuedu.outpatient.entity.MedicalRecordDisease;
import com.neuedu.outpatient.entity.Register;
import com.neuedu.outpatient.entity.CheckRequest;
import com.neuedu.outpatient.entity.DisposalRequest;
import com.neuedu.outpatient.entity.Prescription;
import com.neuedu.outpatient.entity.Disease;
import com.neuedu.outpatient.entity.MedicalTechnology;
import com.neuedu.outpatient.vo.MedicalRecordVO;
import com.neuedu.outpatient.mapper.MedicalRecordDiseaseMapper;
import com.neuedu.outpatient.mapper.DiseaseMapper;
import com.neuedu.outpatient.mapper.MedicalTechnologyMapper;
import com.neuedu.outpatient.service.MedicalRecordService;
import com.neuedu.outpatient.service.RegisterService;
import com.neuedu.outpatient.service.CheckRequestService;
import com.neuedu.outpatient.service.DisposalRequestService;
import com.neuedu.outpatient.service.PrescriptionService;
import com.neuedu.outpatient.service.VisitDetailService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;


@RestController
@RequestMapping("/outpatient/doctor")
public class OutpatientController {

    @Resource
    private RegisterService registerService;

    @Resource
    private MedicalRecordService medicalRecordService;
    @Resource
    private MedicalRecordDiseaseMapper medicalRecordDiseaseMapper;

    @Resource
    private CheckRequestService checkRequestService;

    @Resource
    private VisitDetailService visitDetailService;

    @Resource
    private DisposalRequestService disposalRequestService;

    @Resource
    private PrescriptionService prescriptionService;

    @Resource
    private DiseaseMapper diseaseMapper;

    @Resource
    private MedicalTechnologyMapper medicalTechnologyMapper;



    // 查询当前医生待接诊患者
    @GetMapping("/waitPatient/{doctorId}")
    public Result<List<Register>> getWaitPatient(@PathVariable Integer doctorId){
        List<Register> list = registerService.getWaitPatient(doctorId);
        return Result.success(list);
    }

    // 接诊：visit_state 修改为2 已接诊
    @PutMapping("/receive/{registerId}")
    public Result receivePatient(@PathVariable Integer registerId){
        Register register = registerService.getRegisterById(registerId);
        if(register == null){
            return Result.error("挂号单不存在");
        }
        // 校验：只能接诊待接诊患者
        if(!register.getVisitState().equals(1)){
            return Result.error("该患者状态不是待接诊，无法接诊");
        }
        int rows = registerService.changeVisitState(registerId,2);
        return rows>0 ? Result.success("接诊成功，可以书写病历") : Result.error("接诊失败");
    }


    // ===================== 新增在这里！保存病历+绑定疾病 =====================
    @PostMapping("/saveMedicalRecord")
    @Transactional
    public Result saveMedicalRecord(@RequestBody Map<String,Object> params){
        Integer registerId = (Integer) params.get("registerId");
        List<Integer> diseaseIdList = (List<Integer>) params.get("diseaseIdList");
        MedicalRecord record = JSON.parseObject(JSON.toJSONString(params.get("record")), MedicalRecord.class);

        //校验：该挂号单是否已经写过病历
        MedicalRecord existRecord = medicalRecordService.getByRegisterId(registerId);
        if(existRecord != null){
            return Result.error("该挂号单已存在病历，不可重复新增");
        }
        record.setRegisterId(registerId);
        int row = medicalRecordService.addRecord(record);
        if(row <=0){
            return Result.error("病历保存失败");
        }
        //批量插入疾病关联
        if(diseaseIdList != null && diseaseIdList.size()>0){
            List<MedicalRecordDisease> drList = diseaseIdList.stream().map(did->{
                MedicalRecordDisease mrDisease = new MedicalRecordDisease();
                mrDisease.setMedicalRecordId(record.getId());
                mrDisease.setDiseaseId(did);
                return mrDisease;
            }).collect(Collectors.toList());
            medicalRecordDiseaseMapper.batchInsert(drList);
        }
        return Result.success("病历保存+疾病绑定成功");
    }

    //开立检查申请单
    @PostMapping("/addCheckRequest")
    public Result addCheckRequest(@RequestBody CheckRequest checkRequest){
        //默认状态：待检查
        checkRequest.setCheckState("待检查");
        //开单时间：当前时间
        checkRequest.setCreationTime(new Date());
        int rows = checkRequestService.addCheckRequest(checkRequest);
        return rows>0 ? Result.success("检查申请单开立成功") : Result.error("开立失败");
    }

    //根据挂号id，查询该患者全部检查单
    @GetMapping("/listCheck/{registerId}")
    public Result<List<CheckRequest>> listCheck(@PathVariable Integer registerId){
        List<CheckRequest> list = checkRequestService.getByRegisterId(registerId);
        return Result.success(list);
    }

    //开立处置申请单
    @PostMapping("/addDisposalRequest")
    public Result addDisposalRequest(@RequestBody DisposalRequest disposalRequest){
        //默认状态：待处置
        disposalRequest.setDisposalState("待处置");
        //开单时间：当前时间
        disposalRequest.setCreationTime(new Date());
        int rows = disposalRequestService.addDisposalRequest(disposalRequest);
        return rows>0 ? Result.success("处置申请单开立成功") : Result.error("开立失败");
    }

    //根据挂号id，查询该患者全部处置单
    @GetMapping("/listDisposal/{registerId}")
    public Result<List<DisposalRequest>> listDisposal(@PathVariable Integer registerId){
        List<DisposalRequest> list = disposalRequestService.getByRegisterId(registerId);
        return Result.success(list);
    }

    //开立处方
    @PostMapping("/addPrescription")
    public Result addPrescription(@RequestBody Prescription prescription){
        //处方默认状态：已开立
        prescription.setDrugState("已开立");
        //开立时间：当前时间
        prescription.setCreationTime(new Date());
        int rows = prescriptionService.addPrescription(prescription);
        if (rows < 0) {
            return Result.error("库存不足，无法开立该药品");
        }
        return rows>0 ? Result.success("处方开立成功，药房可查看") : Result.error("处方开立失败");
    }

    //根据挂号id，查询该患者全部处方
    @GetMapping("/listPrescription/{registerId}")
    public Result<List<Prescription>> listPrescription(@PathVariable Integer registerId){
        List<Prescription> list = prescriptionService.getByRegisterId(registerId);
        return Result.success(list);
    }

    //查询患者本次就诊全部资料（病历+疾病+检查+处置+处方）
    @GetMapping("/detail/{registerId}")
    public Result<MedicalRecordVO> getDetail(@PathVariable Integer registerId){
        MedicalRecordVO vo = visitDetailService.getMedicalRecordDetail(registerId);
        if(vo.getRegister() == null){
            return Result.error("挂号单不存在");
        }
        return Result.success(vo);
    }

    // 疾病字典：按名称/编码/ICD 模糊查询（用于病历诊断多选）
    @GetMapping("/diseases")
    public Result<List<Disease>> listDiseases(@RequestParam(required = false) String keyword){
        return Result.success(diseaseMapper.selectByKeyword(keyword));
    }

    // 医技项目字典：按类型（检查/检验/处置）+ 名称模糊查询（用于检查/处置申请单）
    @GetMapping("/medicalTechnologies")
    public Result<List<MedicalTechnology>> listMedicalTechnologies(
            @RequestParam(required = false) String techType,
            @RequestParam(required = false) String keyword){
        return Result.success(medicalTechnologyMapper.selectByType(techType, keyword));
    }

}
