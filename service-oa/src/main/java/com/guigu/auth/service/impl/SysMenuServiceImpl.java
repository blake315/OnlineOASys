package com.guigu.auth.service.impl;


import com.guigu.auth.mapper.SysMenuMapper;
import com.guigu.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guigu.model.system.SysMenu;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zeeway
 * @since 2023-05-09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> findNodes() {
        return null;
    }
}
