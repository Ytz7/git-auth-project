package com.atguigu.system;

import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MyBatisPlusTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Test()
    void test() {
        List<SysRole> list = sysRoleService.list();
        System.out.println(list);
    }

    @Test
    void getUserMenuList() {
        String userId = "8";
        List<SysMenu> userMenuList = sysMenuMapper.getUserMenuList(userId);
        userMenuList.forEach(System.out::println);
    }

}
