package com.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asset.dto.CoopCommitFormProcDTO;
import com.asset.dto.FormInstRecCreate;
import com.asset.entity.AsFormInstDO;
import com.asset.exception.InfoException;
import com.asset.javabean.form.FormSheet;
import com.asset.service.FormInstService;
import com.asset.service.FormModelService;
import com.asset.service.ICooperationService;
import com.asset.utils.Constants;
import com.asset.utils.ProcUtils;
import org.dom4j.DocumentException;
import org.flowable.common.engine.api.FlowableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CooperationService implements ICooperationService {
    @Autowired
    FormInstService formInstService;
    @Autowired
    FormModelService formModelService;



    /**
     * 入口方法
     * 通过调用的uid找到对应的模板，导入到系统中，
     * 然后生成表单流程模型（这个导入的过程应该有一个设置默认值的过程），
     * 再由这个表单流程模型创建相应的实例，实例运行，接着把运行的实例id返回给第三方调用者
     * @param dto
     * @return
     */
    @Override
    public String commitFormProc(CoopCommitFormProcDTO dto) throws DocumentException, FlowableException, InfoException ,InterruptedException{
        //是部门内资产移交,原来的方案是如果代表资产间移交，就把接收部门改成 “本部门”，因为现在的逻辑是：如果接收部门是计算机学院那么就是在本部门之间进行流转
        //所以我们对condition这个条件其实不要也好
        if(dto.getCondition()==1)
        {
            String values = dto.getForm_value();
            JSONObject jsonObject = JSON.parseObject(values);
            for(Map.Entry<String,Object> map:jsonObject.entrySet()){
                if(map.getKey().equals("radio_1571659906791")){
                    map.setValue("否");
                    break;
                }
            }
            dto.setForm_value(jsonObject.toJSONString());
        } else {
            String values = dto.getForm_value();
            JSONObject jsonObject = JSON.parseObject(values);
            for(Map.Entry<String,Object> map:jsonObject.entrySet()){
                if(map.getKey().equals("radio_1571659906791")){
                    map.setValue("是");
                    break;
                }
            }
            dto.setForm_value(jsonObject.toJSONString());
        }

        //现在可以动态指定
//        //申请人和申请人所在部门都是固定的暂时
//        String committer = "金伟刚";
//        String committerSection = "计算机学院";
//
//        JSONObject jsonObject = JSON.parseObject(dto.getForm_value());
//        //申请人姓名
//        jsonObject.put("input_1568450723142",committer);
//        //申请人所在部门
//        jsonObject.put("input_1568450690311",committerSection);
//
//        dto.setForm_value(JSON.toJSONString(jsonObject));


        //以下省略导入模板的步骤
        //```
        //导入模板，生成对应的表单流程模型，这里先直接指定
        String formModelId = "";
        if(dto.getForm_proc_uid().equals("201908281106"))
            formModelId = "660d7d6f-d83f-11e9-a9e8-0242ac120006";

        //由导入的表单流程模型创建相应的实例
        String firstNodeFormSheetStr = formInstService.showNewFormSheet(formModelId).getString("form_json");
        FormSheet firstNodeFormSheet = JSON.parseObject(firstNodeFormSheetStr,FormSheet.class);

        FormInstRecCreate create = new FormInstRecCreate.Builder()
                .editor(dto.getCommitter_id())
                .form_inst_sheet(firstNodeFormSheet)
                .form_inst_value(dto.getForm_value())
                .form_model_id(formModelId)
                .build();

        return formInstService.commitFormInst(create);
    }

    /**
     * 入口方法
     * 获取之前通过业务中台发起的表单流程的运行情况，-1——运行出错，0——该流程还没开始运行，1——运行中，2——实例执行结束，且审批全部同意，3——实例因为中途有人点击拒绝审批到底实例执行结束
     * @param procInstId
     * @return
     */
    @Override
    public Integer getFormProcStatus(String procInstId) {
        Integer status = ProcUtils.isFinished(procInstId)==true ? Constants.COOP_PROC_COMPLETED : Constants.COOP_PROC_RUN;
        //若实例状态是结束，需要判断是否是中途有人在审批节点点击了“拒绝”，导致实例直接被结束
        //form_inst表中取出相应对象数组，看是不是有approve_result等于 APPROVE_DISAGREE
        if(status == Constants.COOP_PROC_COMPLETED)
        {
            List<AsFormInstDO> asFormInstDOS = formInstService.listFormInst(procInstId);
            for(AsFormInstDO formInst : asFormInstDOS)
            {
                if(formInst.getApproveResult()!=null &&
                        formInst.getApproveResult() == Constants.APPROVE_DISAGREE)
                {
                    status = Constants.COOP_PROC_COMPLETED_REJECTED;
                    break;
                }
            }
        }
        return  status;
    }
}
