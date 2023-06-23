package com.guigu.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.auth.service.SysUserService;
import com.guigu.common.result.Result;
import com.guigu.common.utils.MD5;
import com.guigu.model.system.SysUser;
import com.guigu.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zeeway
 * @since 2023-05-02
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService service;

    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String userName = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //like：模糊查询
        //ge：大于等于
        //le：小于等于
        if (!StringUtils.isEmpty(userName)){
            wrapper.like(SysUser::getUsername,userName);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }
        IPage<SysUser> paged = service.page(pageParam, wrapper);

        return Result.success(paged);
    }


    @ApiOperation("获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        SysUser byId = service.getById(id);
        return Result.success(byId);
    }

    @ApiOperation("保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user){
        user.setPassword(MD5.encrypt(user.getPassword()));
        service.save(user);
        return Result.success();
    }

    @ApiOperation("更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user){
        boolean is_success   = service.updateById(user);
        if (is_success){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("删除用户")
    @DeleteMapping("remove/{id}")
    public Result deleteById(@PathVariable Long id){
        service.removeById(id);
        return Result.success();
    }

    @ApiOperation("更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status){
        service.updateStatus(id, status);
        return Result.success();
    }

}

