package com.atguigu.system.filter;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.result.Constant.RedisConstant;
import com.atguigu.common.result.Constant.SystemConstant;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.IpUtil;
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.SysLoginLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** Spring Security
 * 自定义用户认证接口
 * <p>
 * 登录过滤器，继承 UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 * </p>
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final StringRedisTemplate stringRedisTemplate;

    SysLoginLogService sysLoginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager, StringRedisTemplate stringRedisTemplate, SysLoginLogService sysLoginLogService) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
        this.stringRedisTemplate = stringRedisTemplate;
        this.sysLoginLogService = sysLoginLogService;
    }

    /**
     * 登录认证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);

            // 认证信息/校验密码
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            // 向后传递认证结果
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录成功
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 验证成功，获得用户对象，并返回 token
        CustomUser customUser = (CustomUser) authResult.getPrincipal();

        String userId = customUser.getSysUser().getId();
        String username = customUser.getSysUser().getUsername();
        Collection<GrantedAuthority> userAuthorities = customUser.getAuthorities();

        // 返回 token
        String token = JwtHelper.createToken(userId, username);

        //保存权限数据
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_AUTH_KEY + username, JSON.toJSONString(userAuthorities));

        // 保存登录成功日志信息
        String ipAddress = IpUtil.getIpAddress(request);
        sysLoginLogService.recordLoginLog(username, SystemConstant.LOGIN_SUCCESS_STATUS, ipAddress, SystemConstant.LOGIN_SUCCESS_MESSAGE);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    /**
     * 登录失败
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 保存登录失败日志信息
        String ipAddress = IpUtil.getIpAddress(request);
        // 验证失败
        if(e.getCause() instanceof RuntimeException) {
            sysLoginLogService.recordLoginLog("No User", SystemConstant.LOGIN_FAILED_STATUS, ipAddress, e.getMessage());
            ResponseUtil.out(response, Result.build(null, 204, e.getMessage()));
        } else {
            sysLoginLogService.recordLoginLog("No User", SystemConstant.LOGIN_FAILED_STATUS, ipAddress, ResultCodeEnum.LOGIN_MOBLE_ERROR.getMessage());
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }
}
