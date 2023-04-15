package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public IPage<SysUser> getUserPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo) {
        return getBaseMapper().getUserPage(pageParam, userQueryVo);
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysUser::getId, id).set(SysUser::getStatus, status);
        boolean result = update(updateWrapper);
        return result;
    }

    @Override
    public SysUser getByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return getOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        // 获取对应用户
        SysUser sysUser = getByUserName(username);

        // 获取菜单权限
        List<RouterVo> routerVoList = sysMenuService.getUserRouterList(sysUser.getId());
        // 获取按钮权限
        List<String> permsList = sysMenuService.getPermsList(sysUser.getId());

        // 返回
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("roles","[admin]");
        map.put("routers", routerVoList);
        map.put("buttons", permsList);
        return map;
    }
}
