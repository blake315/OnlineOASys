package com.guigu.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guigu.model.system.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zeeway
 * @since 2023-05-02
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);


    SysUser getUserByUsername(String username);
}
