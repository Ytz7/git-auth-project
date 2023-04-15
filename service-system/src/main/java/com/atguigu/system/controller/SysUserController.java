package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("分页条件查询")
    @GetMapping("{page}/{limit}")
    public Result<IPage<SysUser>> getPageList(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            SysUserQueryVo userQueryVo) {
        Page<SysUser> pageParam = new Page<>(page, limit);
        IPage<SysUser> pageModel = sysUserService.getUserPage(pageParam, userQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("根据 id 获取用户信息")
    @GetMapping("{id}")
    public Result<SysUser> getUserInfo(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation("添加用户")
    @PostMapping
    public Result addUser(@RequestBody SysUser user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        boolean result = sysUserService.save(user);
        return result ? Result.ok() : Result.fail();
    }

    @ApiOperation("更新用户")
    @PutMapping
    public Result updateUser(@RequestBody SysUser user) {
        boolean result = sysUserService.updateById(user);
        return result ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据 id 删除用户")
    @DeleteMapping("{id}")
    public Result removeById(@PathVariable Long id) {
        boolean result = sysUserService.removeById(id);
        return result ? Result.ok() : Result.fail();
    }

    @ApiOperation("修改用户状态")
    @GetMapping("changeStatus/{id}/{status}")
    public Result changeStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") Integer status) {
        boolean result = sysUserService.changeStatus(id, status);
        return result ? Result.ok() : Result.fail();
    }
}

