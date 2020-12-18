package com.example.association.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.association.service.ICommonService;
import com.example.association.common.ServerResponse;
import com.example.association.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "通用接口")
@RestController
@RequestMapping("/commom")
public class CommonController {

    @Autowired
    ICommonService iCommonService;



    //用户注册
    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "提交一个用户对象")
    public ServerResponse register(@ApiParam(name = "user",value = "用户实体",required = true)
                                   @RequestBody User user){
        return iCommonService.register(user);
    }

    //用户登陆
    @PostMapping("/login")
    @ApiOperation(value = "用户登陆",notes = "传入用户名和密码，用户名为username，密码为password")
    public ServerResponse login(@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        return iCommonService.login(username,password);
    }

    //用户信息修改
    @PostMapping("/changeInfo")
    @ApiOperation(value = "个人信息修改",notes = "个人信息修改，前端应该只允许修改非关键的字段，不能自行修改id，用户名等等")
    public ServerResponse changeInfo(@ApiParam(name = "user",value = "用户对象",required = true)
                                     @RequestBody User user){
        return iCommonService.changeInfo(user);
    }

    //获取学院
    @GetMapping("/getAcademy")
    @ApiOperation(value = "获取学院列表",notes = "不需要传参数")
    public ServerResponse getAcademy(){
        return iCommonService.getAcademy();
    }

    //获取专业
    @GetMapping("/getMajor")
    @ApiOperation(value = "获取专业列表",notes = "需要传入学院的ID，获取该学院下的专业列表")
    public ServerResponse getMajor(@ApiParam(name = "academyId",value = "学院ID",required = true) int academyId){
        return iCommonService.getMajor(academyId);
    }


    @GetMapping("/getClasses")
    @ApiOperation(value = "获取班级列表",notes = "需要传入学院的ID和专业ID，获取该专业下的班级列表")
    public ServerResponse getClasses(@ApiParam(name = "academyId",value = "学院id",required = true) int academyId,
                                     @ApiParam(name = "majorId",value = "专业Id",required = true) int majorId){
        return iCommonService.getClasses(academyId,majorId);
    }
}