package com.guigu.auth.controller;


import com.guigu.auth.service.SysMenuService;
import com.guigu.common.result.Result;
import com.guigu.model.system.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author zeeway
 * @since 2023-05-09
 */
@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation(value = "获取菜单")
    @GetMapping("findNodes")
    public Result findNodes(){
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.success();

    }

    @ApiOperation("新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu permission){
        sysMenuService.save(permission);
        return Result.success();
    }

    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu permission){
        sysMenuService.updateById(permission);
        return Result.success();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        sysMenuService.removeById(id);
        return Result.success();
    }




}

