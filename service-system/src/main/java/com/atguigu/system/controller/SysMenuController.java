package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssginMenuVo;
import com.atguigu.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("树形查询全部菜单")
    @GetMapping("findAll")
    public Result<List<SysMenu>> findAll() {
        List<SysMenu> menus = sysMenuService.findAll();
        return Result.ok(menus);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping
    public Result addMenu(@RequestBody SysMenu menu) {
        boolean result = sysMenuService.save(menu);
        return result ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping
    public Result updateMenu(@RequestBody SysMenu menu) {
        boolean result = sysMenuService.updateById(menu);
        return result ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("{id}")
    public Result removeMenu(@PathVariable Long id) {
        return sysMenuService.removeMenu(id);
    }

    @ApiOperation(value = "改变菜单状态")
    @GetMapping("changeStatus/{id}/{status}")
    public Result changeStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") Integer status) {
        boolean result = sysMenuService.changeStatus(id, status);
        return result ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "根据 roleId 返回菜单权限列表")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        boolean result = sysMenuService.doAssign(assginMenuVo);
        return result ? Result.ok() : Result.fail();
    }
}

