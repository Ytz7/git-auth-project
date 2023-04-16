package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysLoginLog;
import com.atguigu.model.vo.SysLoginLogQueryVo;
import com.atguigu.system.service.SysLoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统访问记录 前端控制器
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Api(tags = "登录日志管理")
@RestController
@RequestMapping("/admin/system/sysLoginLog")
public class SysLoginLogController {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @ApiOperation(value = "登录日志分页查询")
    @GetMapping("{page}/{limit}")
    public Result<IPage<SysLoginLog>> index(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            SysLoginLogQueryVo sysLoginLogQueryVo) {
        IPage<SysLoginLog> pageModel = sysLoginLogService.getPageModel(page, limit, sysLoginLogQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "根据 id 获取登录日志信息")
    @GetMapping("{id}")
    public Result<SysLoginLog> getInfo(@PathVariable Long id) {
        SysLoginLog sysLoginLog = sysLoginLogService.getById(id);
        return Result.ok(sysLoginLog);
    }

}

