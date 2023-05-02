package com.guigu.auth.controller;


import com.guigu.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    //login
    @PostMapping("login")
    public Result login(){
        Map<String, Object> map = new HashMap<>();
        map.put("token", "admin-token");
        return Result.success(map);
    }

    //info
    @GetMapping("info")
    public Result info(){
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", "admin");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.success(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.success();

    }


}
