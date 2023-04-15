package com.atguigu.system.service;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.atguigu.model.vo.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findAll();

    Result removeMenu(Long id);

    boolean changeStatus(Long id, Integer status);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    boolean doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> getUserRouterList(String id);

    List<String> getPermsList(String id);
}
