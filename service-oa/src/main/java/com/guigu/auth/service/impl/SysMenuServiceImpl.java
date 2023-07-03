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
import com.guigu.vo.system.MetaVo;
import com.guigu.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    /**
     * 1. 先判断角色是否为管理员，如果是就查询所有菜单列表，如果不是再根据userId 查询可以操作的菜单列表
     * 2. 在后者中涉及多表查询：用户角色关系表，角色菜单关系表，菜单表
     * 3. 将查询得到的数据列表构建为要求的路由数据结构
     * @param userId
     * @return
     */
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerList = this.buildRouter(sysMenus);
        return routerList;
    }

    /**
     * 此方法和上面的方法逻辑类似，
     * @param userId
     * @return
     */
    @Override
    public List<String> findUserPermsListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(item -> item.getPerms()).collect(Collectors.toList());
        return permsList;
    }


    private List<RouterVo> buildRouter(List<SysMenu> sysMenus){
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu : sysMenus){
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            if (menu.getType().intValue() == 1){
                //加载出下面的隐藏路由
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList){
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));

                    routers.add(hiddenRouter);
                }
            }else {
                if (!CollectionUtils.isEmpty(children)){
                    if (children.size() > 0){
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);

        }


        return routers;
    }

    public String getRouterPath(SysMenu menu){
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0){
            routerPath = menu.getPath();
        }
        return routerPath;
    }


}
