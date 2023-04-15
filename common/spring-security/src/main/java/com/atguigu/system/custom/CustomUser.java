package com.atguigu.system.custom;

import com.atguigu.model.system.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/** Spring Security
 * 自定义实体对象，设置实际的用户实体对象
 * 由于直接实现 UserDetails 接口需要重写很多方法
 * 可以继承 security.core.userdetails.User
 * 并且实现构造方法, 其中
 * authorities 为权限的封装集合
 */
public class CustomUser extends User {

    private SysUser sysUser;

    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
