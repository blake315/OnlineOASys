package com.guigu.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guigu.auth.service.SysMenuService;
import com.guigu.auth.service.SysUserService;
import com.guigu.common.config.exception.SelfException;
import com.guigu.common.jwt.JwtHelper;
import com.guigu.common.result.Result;
import com.guigu.common.utils.MD5;
import com.guigu.model.system.SysUser;
import com.guigu.vo.system.LoginVo;
import com.guigu.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    //login
    //登录接口，验证用户是否有效正确，并在登录成功之后保持登录状态（使用JWT实现，同时涉及解决cookie跨域的问题）
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserService.getOne(wrapper);

        if (user == null) throw new SelfException(201, "该用户不存在");

        String password_db = user.getPassword();
        String password_input = MD5.encrypt(loginVo.getPassword());

        if (!password_db.equals(password_input)) throw new SelfException(201, "密码错误，请重试");

        if (user.getStatus().intValue() == 0) throw new SelfException(201,"该用户已被禁用");

        String token = JwtHelper.createToken(user.getId(), user.getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.success(map);
    }

    //info
    //主要是在用户登录之后，返回用户对应的权限信息
    @GetMapping("info")
    public Result info(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);

        SysUser user = sysUserService.getById(userId);

        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);
        List<String> permsList = sysMenuService.findUserPermsListByUserId(userId);


        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", user.getName());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("routers",routerList);
        map.put("buttons",permsList);
        return Result.success(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.success();

    }


}
