package com.atguigu.system.controller;

import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.common.result.Constant.SystemConstant;
import com.atguigu.system.execption.GuiguException;
import com.atguigu.system.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        // 登录流程
        // 1. 获取用户
        SysUser sysUser = sysUserService.getByUserName(loginVo.getUsername());
        if (sysUser == null) {
            // 用户不存在
            throw new GuiguException(ResultCodeEnum.ACCOUNT_ERROR);
        }

        // 验证状态
        if (!sysUser.getStatus().equals(SystemConstant.NORMAL_STATUS)) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }

        // 验证密码是否正确
        if (!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())) {
            throw new GuiguException(ResultCodeEnum.PASSWORD_ERROR);
        }

        Map<String, Object> map = new HashMap<>();
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        map.put("token", token);
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        // 获取 token
        String username = JwtHelper.getUsername(request.getHeader("token"));

        // 获取用户信息
        Map<String, Object> userInfo = sysUserService.getUserInfo(username);
//        Map<String, Object> map = new HashMap<>();
//        map.put("roles","[admin]");
//        map.put("name","admin");
//        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(userInfo);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
