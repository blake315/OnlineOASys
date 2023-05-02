package com.guigu.auth.service.impl;


import com.guigu.auth.mapper.SysUserMapper;
import com.guigu.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guigu.model.system.SysUser;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zeeway
 * @since 2023-05-02
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
