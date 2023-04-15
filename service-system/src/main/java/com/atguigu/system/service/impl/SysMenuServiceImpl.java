package com.atguigu.system.service.impl;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.system.helper.MenuHelper;
import com.atguigu.system.helper.RouterHelper;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.atguigu.common.result.ResultCodeEnum.NODE_ERROR;
import static com.atguigu.common.result.Constant.SystemConstant.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService roleMenuService;

    /**
     * 查询所有菜单，构造树形结构
     * @return
     */
    @Override
    public List<SysMenu> findAll() {
        // 查询所有菜单
        List<SysMenu> menuList = list();

        if (menuList == null || CollectionUtils.isEmpty(menuList)) {
            return null;
        }

        //构建树形数据
        return MenuHelper.buildTree(menuList);
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Override
    public Result removeMenu(Long id) {
        // 查询是否存在子节点
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId, id);
        int count = count(queryWrapper);
        if (count > 0) {
            return Result.build(null, NODE_ERROR);
        }

        boolean result = removeById(id);
        return result ? Result.ok() : Result.fail();
    }

    /**
     * 改变菜单状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public boolean changeStatus(Long id, Integer status) {
        UpdateWrapper<SysMenu> queryWrapper = new UpdateWrapper<>();
        queryWrapper.lambda().eq(SysMenu::getId, id).set(SysMenu::getStatus, status);
        return update(queryWrapper);
    }

    /**
     * 根据 roleId 返回菜单权限列表
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        // 获取 status = 1 的所有菜单
        List<SysMenu> menus = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, NORMAL_STATUS));

        // 获取 roleId 对应的 所有 [menuId]
        List<String> menuIds = roleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        // 为菜单中的 isSelect 赋值
        menus.forEach(menu -> {
            if (menuIds.contains(menu.getId())) {
                menu.setSelect(IS_SELECT);
            }
        });

        // 构造树形菜单列表
        return MenuHelper.buildTree(menus);
    }

    /**
     * 给角色分配权限
     * @param assginMenuVo
     * @return
     */
    @Override
    public boolean doAssign(AssginMenuVo assginMenuVo) {
        // 判断是否已经存在相关记录
        String roleId = assginMenuVo.getRoleId();
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);

        int count = roleMenuService.count(queryWrapper);

        if (count > 0) {
            // 存在就删除
            boolean remove = roleMenuService.remove(queryWrapper);
            if (!remove) {
                return false;
            }
        }

        // 添加记录
        List<String> menuIdList = assginMenuVo.getMenuIdList();
        for (String menuId : menuIdList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            boolean save = roleMenuService.save(sysRoleMenu);
            if (!save) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取用户菜单权限列表
     * @param userId
     * @return
     */
    @Override
    public List<RouterVo> getUserRouterList(String userId) {
        List<SysMenu> menuList = getUserMenuList(userId);

        // 封装
        // 构建树形结构 -> 封装路由数据结构
        List<SysMenu> buildTree = MenuHelper.buildTree(menuList);
        return RouterHelper.buildRouters(buildTree);
    }

    /**
     * 获取用户按钮权限列表
     * @param userId
     * @return
     */
    @Override
    public List<String> getPermsList(String userId) {
        List<SysMenu> menuList = getUserMenuList(userId);

        //创建返回的集合
        List<String> permissionList = new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            if(sysMenu.getType() == 2){
                // 为按钮类型
                permissionList.add(sysMenu.getPerms());
            }
        }
        return permissionList;
    }

    public List<SysMenu> getUserMenuList(String userId) {
        List<SysMenu> menuList = null;
        if (userId.equals(ADMIN_ID)) {
            // 返回所有
            menuList = list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, NORMAL_STATUS)
                    .orderByAsc(SysMenu::getSortValue));
        } else {
            // 不是超级管理员，则再进行筛选
            menuList = baseMapper.getUserMenuList(userId);
        }

        return menuList;
    }

}
