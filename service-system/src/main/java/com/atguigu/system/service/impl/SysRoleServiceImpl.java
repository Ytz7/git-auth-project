package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.vo.AssginRoleVo;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.service.SysRoleService;
import com.atguigu.system.service.SysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author Ytz
 * @since 2023-04-11
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo) {
        return getBaseMapper().selectPage(pageParam, roleQueryVo);
    }

    /**
     * 根据用户 id 获取角色数据
     * @param userId
     * @return map -> {allRoles, userRoleIds}
     */
    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        // 获取所有角色
        List<SysRole> roles = list();

        // 根据用户 id 查询用户已分配的角色 id 列表
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        List<String> userRoleIds = userRoleService.list(queryWrapper).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        //创建返回的Map
        Map<String, Object> map = new HashMap<>();
        map.put("allRoles", roles);
        map.put("userRoleIds", userRoleIds);

        return map;
    }

    /**
     * 根据用户 分配角色
     * @param
     * @return
     */
    @Override
    public boolean doAssign(AssginRoleVo assginRoleVo) {
        // 删除已存在的角色列表
        // 判断是否存在记录
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());

        int count = userRoleService.count(queryWrapper);
        if (count > 0) {
            boolean remove = userRoleService.remove(queryWrapper);
            if (!remove) {
                return false;
            }
        }

        // 分配新的角色
        List<String> roleIdList = assginRoleVo.getRoleIdList();
        for (String roleId : roleIdList) {
            if (roleId != null) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assginRoleVo.getUserId());
                sysUserRole.setRoleId(roleId);

                boolean save = userRoleService.save(sysUserRole);
                if (!save) {
                    return false;
                }
            }
        }

        return true;
    }
}
