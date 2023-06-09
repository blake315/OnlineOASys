package com.guigu.security.filter;

import com.alibaba.fastjson.JSON;
import com.guigu.common.jwt.JwtHelper;
import com.guigu.common.result.ResponseUtil;
import com.guigu.common.result.Result;
import com.guigu.common.result.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;
    public TokenAuthenticationFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        logger.info("uri:" + httpServletRequest.getRequestURI());
        //如果访问的是登录接口，则直接通过，不用进行认证
        if ("/admin/system/index/login".equals(httpServletRequest.getRequestURI())){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(httpServletRequest);
        if (null != authentication){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            ResponseUtil.out(httpServletResponse, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)){
            String username = JwtHelper.getUsername(token);
            logger.info("username:" + username);
            if (!StringUtils.isEmpty(username)){
                //这里在redis中获取权限数据，进行封装并返回
                String authString = (String) redisTemplate.opsForValue().get(username);
                //封装过程
                if (!StringUtils.isEmpty(authString)){
                    List<Map> maps = JSON.parseArray(authString, Map.class);
                    List<SimpleGrantedAuthority> authlist = new ArrayList<>();
                    for (Map map : maps){
                        authlist.add(new SimpleGrantedAuthority((String)map.get("authority")));
                    }
                    return new UsernamePasswordAuthenticationToken(username, null, authlist);
                }else {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }

            }
        }
        return null;

    }
}
