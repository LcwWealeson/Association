package com.example.association.controller;

import com.example.association.service.IStudentService;
import com.example.association.common.ServerResponse;
import com.example.association.pojo.ApplyAssociation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "学生功能的接口")
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService iStudentService;


    //申请社团
    @PostMapping("/applyAssociation")
    @ApiOperation(value = "申请新社团",notes = "申请新社团，需要传入一个申请社团的实体")
    public ServerResponse applyCreateAssociation(@ApiParam(name = "applyAssociation",value = "申请社团的实体",required = true)
                                                     @RequestBody ApplyAssociation applyAssociation){
        return iStudentService.apply(applyAssociation);
    }

    //查看已有社团
    @GetMapping("/getAssociationList")
    @ApiOperation(value = "查看已有社团",notes = "不传入参数时获取列表全部，传入时查询")
    public ServerResponse getAssociationList(@ApiParam(name = "assocName",value = "社团的模糊名称",required = false) String assocName){
        return iStudentService.getAssociationList(assocName);
    }

    //申请加入社团
    @GetMapping("/applyJoinAssociation")
    @ApiOperation(value = "申请加入社团",notes = "传入个人id，和社团id")
    public ServerResponse applyJoinAssociation(@ApiParam(name = "userId",value = "用户id",required = true) int userId,
                                               @ApiParam(name = "associationId",value = "社团id",required = true) int associationId){
        return iStudentService.applyJoinAssociation(userId,associationId);
    }

    //申请参加活动
    @GetMapping("/applyJoinEvent")
    @ApiOperation(value = "申请加入活动",notes ="传入用户id和活动id" )
    public ServerResponse applyJoinEvent(@ApiParam(name = "userId",value = "用户id",required = true)int userId,
                                         @ApiParam(name = "eventId",value = "活动id",required = true)int eventId){
        return iStudentService.applyJoinEvent(userId,eventId);
    }

    //查看已有活动
    @GetMapping("/getCheckedEvent")
    @ApiOperation(value = "查看所有活动",notes ="无需传参数" )
    public ServerResponse getCheckedEvent(){
        return iStudentService.getCheckedEvent();
    }

}
