package com.atguigu.system.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> getUserPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo);

    boolean changeStatus(Long id, Integer status);

    SysUser getByUserName(String username);

    Map<String, Object> getUserInfo(String username);
}
