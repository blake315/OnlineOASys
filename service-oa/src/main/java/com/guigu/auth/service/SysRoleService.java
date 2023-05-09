package com.guigu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guigu.model.system.SysRole;
import com.guigu.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> findRoleDataByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
