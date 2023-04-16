package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysOperLog;
import com.atguigu.model.vo.SysOperLogQueryVo;
import com.atguigu.system.mapper.SysOperLogMapper;
import com.atguigu.system.service.SysOperLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Override
    public IPage<SysOperLog> getPageInfo(Long page, Long limit, SysOperLogQueryVo sysOperLogQueryVo) {
        Page<SysOperLog> pageParam = new Page<>(page, limit);

        // 条件
        String title = sysOperLogQueryVo.getTitle();
        String operName = sysOperLogQueryVo.getOperName();
        String createTimeBegin = sysOperLogQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysOperLogQueryVo.getCreateTimeEnd();

        LambdaQueryWrapper<SysOperLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isNullOrEmpty(title), SysOperLog::getTitle, title);
        queryWrapper.like(!StringUtils.isNullOrEmpty(operName), SysOperLog::getOperName, operName);
        queryWrapper.gt(!StringUtils.isNullOrEmpty(createTimeBegin), SysOperLog::getCreateTime, createTimeBegin);
        queryWrapper.like(!StringUtils.isNullOrEmpty(createTimeEnd), SysOperLog::getCreateTime, createTimeEnd);

        return page(pageParam, queryWrapper);
    }
}
