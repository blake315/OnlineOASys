package com.guigu.auth.controller;

import com.guigu.auth.service.SysRoleService;
import com.guigu.model.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// http://localhost:8800/admin/system/sysRole/findAll

@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService service;

    @GetMapping("/findAll")
    public List<SysRole> findAll(){
        List<SysRole> list = service.list();
        return list;
    }


}
