package com.atguigu.system.service;

import com.atguigu.model.system.SysLoginLog;
import com.atguigu.model.vo.SysLoginLogQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 记录登录信息
     * @param username 用户名
     * @param status 状态
     * @param ipaddr ip
     * @param message 消息内容
     */
    public void recordLoginLog(String username, Integer status, String ipaddr, String message);

    /**
     * 分页条件查询
     * @param page
     * @param limit
     * @param sysLoginLogQueryVo
     * @return
     */
    IPage<SysLoginLog> getPageModel(Long page, Long limit, SysLoginLogQueryVo sysLoginLogQueryVo);
}
