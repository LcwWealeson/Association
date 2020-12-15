package com.example.association.service.impl;

import com.example.association.service.ICommonService;
import com.example.association.VO.ClassesVO;
import com.example.association.common.ServerResponse;
import com.example.association.dao.AcademyMapper;
import com.example.association.dao.ClassesMapper;
import com.example.association.dao.MajorMapper;
import com.example.association.dao.UserMapper;
import com.example.association.pojo.Academy;
import com.example.association.pojo.Classes;
import com.example.association.pojo.Major;
import com.example.association.pojo.User;
import com.example.association.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AcademyMapper academyMapper;

    @Autowired
    MajorMapper majorMapper;

    @Autowired
    ClassesMapper classesMapper;

    //用户注册
    @Override
    public ServerResponse register(User user) {
        String password = user.getPassword();
        String md5password = MD5Util.getMD5(password);
        user.setRole(0);
        user.setPassword(md5password);
        int resultRow = userMapper.insert(user);
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //用户登陆
    @Override
    public ServerResponse login(String username, String password) {
        User user = userMapper.selectUserByName(username);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        if(!user.getPassword().equals(MD5Util.getMD5(password))){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        return ServerResponse.createBySuccessMessage("登陆成功",user);
    }

    //用户修改个人信息
    @Override
    public ServerResponse changeInfo(User user) {
        int resultRow = userMapper.updateByPrimaryKeySelective(user);
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    @Override
    public ServerResponse getAcademy() {
        List<Academy> academies = academyMapper.getAll();
        return ServerResponse.createBySuccessMessage("查询成功",academies);
    }

    @Override
    public ServerResponse getMajor(int academyId) {
        List<Major> majors = majorMapper.getAll(academyId);
        return ServerResponse.createBySuccessMessage("查询成功",majors);
    }

    @Override
    public ServerResponse getClasses(int academyId, int majorId) {
        List<Classes> classesList = classesMapper.getAll(academyId,majorId);
        List<ClassesVO> classesVOS = new ArrayList<>();
        for(Classes classes:classesList){
            ClassesVO classesVO = new ClassesVO();
            BeanUtils.copyProperties(classes,classesVO);
            classesVO.setClassName(majorMapper.getNameById(majorId)+classes.getClassNumber()+"班");
            classesVOS.add(classesVO);

        }
        return ServerResponse.createBySuccessMessage("查询成功",classesVOS);
    }
}
