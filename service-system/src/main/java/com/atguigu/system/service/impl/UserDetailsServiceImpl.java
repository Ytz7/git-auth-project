package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysUser;
import com.atguigu.common.result.Constant.SystemConstant;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** Spring Security
 * 自定义业务类 @Component
 * 实现 UserDetailsService 接口
 * 重写 loadUserByUsername 方法来获取自定义对象
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUserName(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if (!sysUser.getStatus().equals(SystemConstant.NORMAL_STATUS)) {
            throw new RuntimeException("账号以停用");
        }

        // 获取用户权限（按钮）列表
        List<String> permsList = sysMenuService.getPermsList(sysUser.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 将用户对应权限封装至权限列表中
        for (String perm : permsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }

        return new CustomUser(sysUser, authorities);
    }
}
