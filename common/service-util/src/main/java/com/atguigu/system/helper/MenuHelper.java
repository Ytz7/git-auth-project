package com.atguigu.system.helper;

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

import static com.atguigu.common.result.Constant.SystemConstant.MENU_PARENT_ID;

public class MenuHelper {
    /**
     * 以树形结构封装菜单
     * @param menuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> menuList) {
        List<SysMenu> menuTree = new ArrayList<>();

        // 遍历菜单，为父节点封装子节点
        menuList.forEach(menu -> {
            if (menu.getParentId().equals(MENU_PARENT_ID)) {
                menuTree.add(findChildren(menu, menuList));
            }
        });

        return menuTree;
    }

    /**
     * 构建封装子节点
     * @param menu
     * @param menuList
     * @return
     */
    private static SysMenu findChildren(SysMenu menu, List<SysMenu> menuList) {
        menu.setChildren(new ArrayList<>());

        menuList.forEach(item -> {
            // 节点的父节点 parentId 为 menu 的 Id
            if (item.getParentId().equals(menu.getId())) {
                // 在接着寻找其子节点
                if (item.getChildren() == null) {
                    // 初始化
                    item.setChildren(new ArrayList<>());
                }
                menu.getChildren().add(findChildren(item, menuList));
            }
        });

        return menu;
    }
}
