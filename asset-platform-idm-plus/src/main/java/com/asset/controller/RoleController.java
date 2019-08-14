
package com.asset.controller;

import com.asset.bean.RespBean;
import com.asset.bean.Role;
import com.asset.common.model.Query;
import com.asset.service.IRoleService;
import com.asset.utils.Condition;
import com.asset.utils.Func;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author hjhu
 */
@RestController
@RequestMapping(value = "sys/role")
@AllArgsConstructor
@Api(value = "平台角色管理", tags = "平台角色管理")
public class RoleController {

    IRoleService roleService;

    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色别名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleNameZh", value = "角色中文名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "起始页", defaultValue = "1", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "size", value = "数据量大小", defaultValue = "10", paramType = "query", dataType = "integer")
    })
    @ApiOperation(value = "平台角色列表", notes = "（已完成）传入role实体")
    public R roleList(@ApiIgnore @RequestParam Map<String, Object> role, Query query){
        PageHelper.startPage(query.getPage(), query.getSize());
        QueryWrapper<Role> queryWrapper = Condition.getQueryWrapper(role, Role.class);
        queryWrapper.lambda().eq(Role::getIsDeleted, 0);
        return R.data(new PageInfo<>(roleService.list(queryWrapper)));
    }

    @GetMapping("detail")
    @ApiOperation(value = "获取平台角色详细信息", notes = "（已完成）传入角色id")
    public RespBean getRoleDetail(Role role){
        return RespBean.data(roleService.getOne(new QueryWrapper<>(role)));
    }

    @PostMapping("submit")
    @ApiOperation(value = "新增角色信息", notes = "（已完成）传入Role实体;roleName必填")
    public RespBean addRole(@RequestBody Role role){
        if (roleService.roleExists(role)){
            return RespBean.error("角色名称已存在，请更换后重试");
        }
        return RespBean.status(roleService.save(role));
    }

    @PutMapping("edit")
    @ApiOperation(value = "修改角色信息", notes = "传入Role实体;id必填")
    public RespBean UpdateRole(@RequestBody Role role){
        //TODO:更新前判断名称是否重复
        return RespBean.status(roleService.updateById(role));
    }

    @PostMapping("remove")
    @ApiOperation(value = "删除", notes = "（已完成）传入ids")
    public RespBean remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return RespBean.status(roleService.batchDelete(org.springblade.core.tool.utils.Func.toIntList(",", ids)));
    }

    @PostMapping("grant")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "menuIds", value = "菜单id的数组", required = true, dataType = "String")
    })
    @ApiOperation(value = "编辑平台角色权限", notes = "已完成")
    public RespBean grant(@RequestParam Integer roleId, @RequestParam String menuIds){
        return RespBean.status(roleService.grant(roleId, Func.toLongList(",", menuIds)));
    }


}