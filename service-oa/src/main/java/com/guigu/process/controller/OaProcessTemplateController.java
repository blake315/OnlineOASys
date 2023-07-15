package com.guigu.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.common.result.Result;
import com.guigu.model.process.ProcessTemplate;
import com.guigu.process.service.OaProcessTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author zeeway
 * @since 2023-07-14
 */
@RestController
@RequestMapping("/admin/process/processTemplate")
public class OaProcessTemplateController {

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @ApiOperation(value = "获取分页审批模板数据")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit){
        Page<ProcessTemplate> page1 = new Page<>(page, limit);
        //在查询过程中，要同时查询到审批类型的名称
        IPage<ProcessTemplate> pageModel = oaProcessTemplateService.selectPageProcessTemplate(page1);
        return Result.success(pageModel);
    }

    @ApiOperation("新增审批模板")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate){
        oaProcessTemplateService.save(processTemplate);
        return Result.success();
    }

    @ApiOperation("删除审批模板")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        oaProcessTemplateService.removeById(id);
        return Result.success();
    }

    @ApiOperation("修改审批模板")
    @PutMapping("update")
    public Result update(@RequestBody ProcessTemplate processTemplate){
        oaProcessTemplateService.updateById(processTemplate);
        return Result.success();
    }

    @ApiOperation("根据id获取审批模板")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        ProcessTemplate byId = oaProcessTemplateService.getById(id);
        return Result.success(byId);
    }


    //上传审批流程模板
    @ApiOperation(value = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException{
        //设置上传的文件夹
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        File tempFile = new File(path + "/processes/");
        if (!tempFile.exists()){
            tempFile.mkdirs();
        }
        //文件写入
        String filename = file.getOriginalFilename();
        File file1 = new File(path + "/processes/" + filename);
        //保存文件
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            return Result.fail("上传失败！");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("processDefinitionPath", "processes/" + filename);
        map.put("processDefinitionKey", filename.substring(0,filename.lastIndexOf(".")));
        return Result.success(map);
    }


    @ApiOperation(value = "发布审批模板")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id){
        //修改状态
        oaProcessTemplateService.publish(id);
        return Result.success();
    }

}

