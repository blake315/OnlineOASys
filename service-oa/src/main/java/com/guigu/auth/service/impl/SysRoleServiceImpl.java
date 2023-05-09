package com.guigu.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guigu.auth.mapper.SysRoleMapper;
import com.guigu.auth.service.SysRoleService;
import com.guigu.auth.service.SysUserRoleService;
import com.guigu.model.system.SysRole;
import com.guigu.model.system.SysUserRole;
import com.guigu.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    // 查询所有角色 和 当前用户所属的角色
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //得到全部的角色
        List<SysRole> allRoleList = baseMapper.selectList(null);
        // 根据userId查询 角色用户关系表， 查询userId对应的所有角色Id
        LambdaQueryWrapper<SysUserRole> warpper = new LambdaQueryWrapper<>();
        warpper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(warpper);

        //从list中得到角色ID
        List<Long> RoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //根据角色id到全部角色的list中进行比较
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole:
             allRoleList ) {
            if (RoleIdList.contains(sysRole.getId())){
                assignRoleList.add(sysRole);
            }
        }

        //封装结果返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRoleList",allRoleList);
        return roleMap;
    }

    // 为用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //删除之前的数据
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //重新输入新的数据
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId :
                roleIdList) {
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
