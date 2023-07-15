package com.guigu.process.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.model.process.ProcessTemplate;
import com.guigu.model.process.ProcessType;
import com.guigu.process.mapper.OaProcessTemplateMapper;
import com.guigu.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guigu.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author zeeway
 * @since 2023-07-14
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {
    @Resource
    private OaProcessTemplateMapper oaProcessTemplateMapper;

    @Autowired
    private OaProcessTypeService oaProcessTypeService;



    @Override
    public IPage<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> page1) {
        //调用mapper方法实现分页
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(page1, null);


        //从分页数据中获取list集合，遍历之后得到每个对象的审批类型id
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        for (ProcessTemplate processTemplate: processTemplateList) {
            Long processTypeId = processTemplate.getProcessTypeId();

            LambdaQueryWrapper<ProcessType> wrapper  = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessType::getId,processTypeId);
            ProcessType processType = oaProcessTypeService.getOne(wrapper);
            if (processType == null){
                continue;
            }
            processTemplate.setProcessTypeName(processType.getName());
        }


        //封装数据返回

        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);


    }
}
