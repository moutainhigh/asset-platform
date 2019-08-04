package com.asset.controller.admin;

import com.asset.entity.AsFormModel;
import com.asset.javabean.AdminFormModelVO;
import com.asset.javabean.RespBean;
import com.asset.service.AdminFormModelService;
import com.asset.service.impl.AsFormModelServiceImpl;
import com.asset.utils.Condition;
import com.asset.utils.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 控制端表单模型
 * @author YBY
 */
@RestController
@RequestMapping("/admin")
public class AdminFormModelController {

    @Autowired
    AdminFormModelService adminFormModelService;
    @Autowired
    AsFormModelServiceImpl asFormModelService;

    /**
     * 获取表单模型信息
     * @param role
     * @param query
     * @return
     */
    @GetMapping("/form_model/list")
    public RespBean getFormModelListPlus(@ApiIgnore @RequestParam Map<String, Object> role, Query query){
        QueryWrapper<AsFormModel> queryWrapper = Condition.getQueryWrapper(role, AsFormModel.class);
        PageHelper.startPage(query.getPage(),query.getSize());
        PageInfo<AdminFormModelVO> list = new PageInfo<>(asFormModelService.listAdminFormModelInfo(queryWrapper));
        return RespBean.ok("",list);
    }

}
