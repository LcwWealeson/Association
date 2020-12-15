package com.example.association.controller;

import com.example.association.Service.ISuperAdminService;
import com.example.association.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "超级管理员接口")
@RequestMapping("/superadmin")
public class SuperadminController {
    @Autowired
    ISuperAdminService iSuperAdminService;

    //审核社团申请
    @GetMapping("/checkApplyAssociation")
    @ApiOperation(value = "审核社团申请",notes = "需要传入申请表id，管理员id和操作operation三个参数")
    public ServerResponse check(@ApiParam(name = "applyId",value = "申请表id",required = true) int applyId,
                                @ApiParam(name = "adminId",value = "管理员id",required = true) int adminId,
                                @ApiParam(name = "operation",value = "操作，当值为1的时候通过，为2的时候不通过",required = true) int operation){
        return iSuperAdminService.checkApplyAssociation(applyId,adminId,operation);
    }

    //查看社团的申请
    @GetMapping("/getAssociationCheckList")
    @ApiOperation(value = "查看社团的申请",notes = "有参数传入时就模糊查询，没有就获取全部")
    public ServerResponse getCheckList(@ApiParam(name = "assocName",value = "社团名字",required = true) String assocName){
        return iSuperAdminService.getCheckAssociationList(assocName);
    }

    //查看活动的申请
    @GetMapping("/getEventCheckList")
    @ApiOperation(value = "查看活动的申请",notes = "有参数传入时就模糊查询，没有就获取全部")
    public ServerResponse getEventCheckList(@ApiParam(name = "eventName",value = "活动名字",required = true) String eventName,
                                            @ApiParam(name = "assocName",value = "社团名称",required = true) String assocName,
                                            @ApiParam(name = "applicant",value = "申请人名字",required = true) String applicant){
        return iSuperAdminService.getEventCheckList(eventName,assocName,applicant);
    }

    //审核活动的申请e
    @GetMapping("/checkApplyEvent")
    @ApiOperation(value = "审核活动的申请",notes = "需要传入活动申请id和操作operation两个参数")
    public ServerResponse checkApplyEvent(@ApiParam(name = "eventId",value = "活动申请id",required = true) int eventId,
                                          @ApiParam(name = "operation",value = "操作，当值为1的时候通过，为2的时候不通过",required = true)int operation){
        return iSuperAdminService.checkApplyEvent(eventId,operation);
    }

    @GetMapping("/changePassword")
    @ApiOperation(value = "修改密码",notes = "需要传入用户id和新密码newPassword两个参数")
    public ServerResponse changePassword(@ApiParam(name = "userId",value = "用户id",required = true) int userId,
                                         @ApiParam(name = "newPassword",value = "新密码newPassword",required = true) String newPassword){
        return iSuperAdminService.changePassword(userId,newPassword);
    }

}
