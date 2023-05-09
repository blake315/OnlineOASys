package com.guigu.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guigu.model.system.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zeeway
 * @since 2023-05-09
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();
}
