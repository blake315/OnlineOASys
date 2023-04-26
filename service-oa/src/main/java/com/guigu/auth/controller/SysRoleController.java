package com.guigu.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.auth.service.SysRoleService;
import com.guigu.common.result.Result;
import com.guigu.model.system.SysRole;
import com.guigu.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 接口路径 http://localhost:8800/admin/system/sysRole/findAll

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService service;

//    @GetMapping("/findAll")
//    public List<SysRole> findAll(){
//        List<SysRole> list = service.list();
//        return list;
//    }
//    上下对比，上面是直接返回查询结果，下面是将结果经过统一返回对象类封装之后返回，这样做的目的是提高开发人员的协作。

    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll(){
        List<SysRole> list = service.list();
        return Result.success(list);
    }

    /**
     *
     * @param page 当前页
     * @param limit 每页显示条数
     * @param sysRoleQueryVo 条件查询对象
     * @return
     */
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo){
        Page<SysRole> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)){
            wrapper.like(SysRole::getRoleName, roleName);
        }


        IPage<SysRole> rolePage = service.page(pageParam, wrapper);

        return Result.success(rolePage);
    }


}