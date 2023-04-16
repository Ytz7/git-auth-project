package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysLoginLog;
import com.atguigu.model.vo.SysLoginLogQueryVo;
import com.atguigu.system.mapper.SysLoginLogMapper;
import com.atguigu.system.service.SysLoginLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public void recordLoginLog(String username, Integer status, String ipaddr, String message) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setStatus(status);
        sysLoginLog.setIpaddr(ipaddr);
        sysLoginLog.setMsg(message);

        save(sysLoginLog);
    }

    @Override
    public IPage<SysLoginLog> getPageModel(Long page, Long limit, SysLoginLogQueryVo sysLoginLogQueryVo) {
        Page<SysLoginLog> pageParam = new Page<>(page, limit);

        // 条件
        String username = sysLoginLogQueryVo.getUsername();
        Integer status = sysLoginLogQueryVo.getStatus();
        String createTimeBegin = sysLoginLogQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysLoginLogQueryVo.getCreateTimeEnd();

        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isNullOrEmpty(username), SysLoginLog::getUsername, username);
        queryWrapper.eq(status != null, SysLoginLog::getStatus, status);
        queryWrapper.gt(!StringUtils.isNullOrEmpty(createTimeBegin), SysLoginLog::getCreateTime, createTimeBegin);
        queryWrapper.lt(!StringUtils.isNullOrEmpty(createTimeBegin), SysLoginLog::getCreateTime, createTimeEnd);

        return page(pageParam, queryWrapper);
    }
}
