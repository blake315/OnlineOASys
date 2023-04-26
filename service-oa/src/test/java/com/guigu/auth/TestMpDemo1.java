package com.guigu.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guigu.auth.mapper.SysRoleMapper;
import com.guigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    @Autowired
    private SysRoleMapper mapper;

    //查询所有记录
    @Test
    public void getAll(){
        List<SysRole> list = mapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void add(){
        SysRole role = new SysRole();
        role.setRoleName("测试管理员1");
        role.setRoleCode("test1");
        role.setDescription("测试管理员1");

        int rows = mapper.insert(role);
        System.out.println(rows);
        System.out.println(role.getId());
    }

    @Test
    public void update(){
        //根据id查询
        SysRole role = mapper.selectById(10);
        role.setRoleName("修改管理员");
        role.setRoleCode("update");
        role.setDescription("修改管理员");
        int rows = mapper.updateById(role);
        System.out.println(rows);
    }

    @Test
    public void testQuery1(){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "测试管理员");
        SysRole role = mapper.selectOne(wrapper);
        System.out.println(role);
    }

    @Test
    public void testQuery2(){
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleCode,"test");
        SysRole role = mapper.selectOne(wrapper);
        System.out.println(role);
    }
}
