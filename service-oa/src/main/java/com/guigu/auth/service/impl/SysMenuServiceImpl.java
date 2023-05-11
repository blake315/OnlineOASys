package com.guigu.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guigu.auth.mapper.SysMenuMapper;
import com.guigu.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guigu.auth.service.SysRoleMenuService;
import com.guigu.auth.utils.MenuHelper;
import com.guigu.common.config.exception.SelfException;
import com.guigu.model.system.SysMenu;
import com.guigu.model.system.SysRoleMenu;
import com.guigu.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //构建树形机构
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    /**
     * 在删除之前先检查有无子节点（子目录）， 对于有子目录的菜单是无法删除的
     * @param id
     */
    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new SelfException(201, "菜单不能删除");
        }
        baseMapper.deleteById(id);

    }

    /**
     * 实现步骤
     * 1.查询所有菜单，条件是 status == 1
     * 2.根据 roleId 查询角色菜单表中的所有对应菜单 id
     * 3.根据菜单 id ， 获取对应的菜单对象 （使用实体类 SysMenu 中的 isSelect属性表示）
     * 4.返回规定的菜单格式
     *
     * 实际的实现和 SysRoleServiceImpl 中的实现是类似的。不过在这里使用了 stream流处理，后者使用循环遍历。本质上是一样的
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> allMenuList = baseMapper.selectList(wrapper);

        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        List<Long> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        allMenuList.stream().forEach(item -> {
            if (menuIdList.contains(item.getId())){
                item.setSelect(true);
            }else {
                item.setSelect(false);
            }
        });

        List<SysMenu> sysMenuList = MenuHelper.buildTree(allMenuList);

        return sysMenuList;
    }

    /**
     * 这里的逻辑和之前的类似，我第一次尝试模仿着自己写。如果有报错的话再看视频敲
     * @param assginMenuVo
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //删除之前的数据
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,assginMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        //增加新的数据
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for (Long menuId:
             menuIdList) {
            if (StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuService.save(sysRoleMenu);
        }
    }
}
