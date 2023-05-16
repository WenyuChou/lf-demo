package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.vo.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhouwenyu
 * date 2022-03-14
 */
@RestController
@RequestMapping("/shardingTest")
public class ShardingTestController {

    @Resource
    private UserMapper userMapper;

    @PostMapping("/getUser/{userId}")
    public ApiResult getUser(@PathVariable("userId") String userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserId,userId);
        return ApiResult.success(userMapper.selectOne(queryWrapper));
    }

    @PostMapping("/insert")
    public ApiResult insert(@RequestBody User user){
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return ApiResult.success(user);
    }

    @PostMapping("/update")
    public ApiResult update(@RequestBody User user){
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getUserId,user);
        userMapper.update(user,updateWrapper);
        return ApiResult.success();
    }
    @PostMapping("/delete/{userId}")
    public ApiResult delete(@PathVariable("userId")String userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserId,userId);
        userMapper.delete(queryWrapper);
        return ApiResult.success();
    }
}
