package com.guigu.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.common.result.Result;
import com.guigu.model.process.ProcessType;
import com.guigu.process.service.OaProcessTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author zeeway
 * @since 2023-07-14
 */
@RestController
@RequestMapping("/admin/process/processType")
public class OaProcessTypeController {
    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @GetMapping("findAll")
    public Result findAll(){
        List<ProcessType> list = oaProcessTypeService.list();
        return Result.success(list);
    }

    //@PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit){
        Page<ProcessType> Page = new Page<>(page, limit);
        IPage<ProcessType> pageModel = oaProcessTypeService.page(Page);
        return Result.success(pageModel);

    }

    //@PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "根据ID获取")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        ProcessType processType = oaProcessTypeService.getById(id);
        return Result.success(processType);
    }

    //@PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessType processType){
        oaProcessTypeService.save(processType);
        return Result.success();
    }

    //@PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation(value = "根据Id删除")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        oaProcessTypeService.removeById(id);
        return Result.success();
    }

    //@PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result update(@RequestBody ProcessType processType){
        oaProcessTypeService.updateById(processType);
        return Result.success();
    }

}

