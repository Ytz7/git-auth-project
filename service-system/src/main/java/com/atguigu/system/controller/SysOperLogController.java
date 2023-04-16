package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysOperLog;
import com.atguigu.model.vo.SysOperLogQueryVo;
import com.atguigu.system.service.SysOperLogService;
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
 * 操作日志记录 前端控制器
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Api(tags = "操作日志管理")
@RestController
@RequestMapping("/admin/system/sysOperLog")
public class SysOperLogController {

    @Autowired
    private SysOperLogService sysOperLogService;

    @ApiOperation(value = "分页条件查询操作日志")
    @GetMapping("{page}/{limit}")
    public Result<IPage<SysOperLog>> getPageInfo(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            SysOperLogQueryVo sysOperLogQueryVo) {
        IPage<SysOperLog> pageModel = sysOperLogService.getPageInfo(page, limit, sysOperLogQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "根据 id 获取操作日志信息")
    @GetMapping("{id}")
    public Result<SysOperLog> getInfo(@PathVariable Long id) {
        SysOperLog sysOperLog = sysOperLogService.getById(id);
        return Result.ok(sysOperLog);
    }

}

