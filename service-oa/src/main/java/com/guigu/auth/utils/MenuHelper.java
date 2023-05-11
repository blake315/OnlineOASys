package com.guigu.auth.utils;

import com.guigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    //使用递归方法构建菜单
    public static List<SysMenu>  buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList){
            //parentId == 0 的时候就说明这个节点是根节点（一级目录），从他开始往下递归遍历。
            if (sysMenu.getParentId().longValue()==0){
                trees.add(getChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        //遍历菜单数据，判断id和parentId的对应关系
        for (SysMenu sysMenu1 : sysMenuList){
            //如果sysMenu的id等于遍历中的sysMenu1的父id，说明是后者的父节点，往下加入孩子节点，继续往下遍历。
            if (sysMenu.getId().longValue() == sysMenu1.getParentId().longValue()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<SysMenu>());
                }
                sysMenu.getChildren().add(getChildren(sysMenu1, sysMenuList));
            }
        }
        return sysMenu;
    }
}
