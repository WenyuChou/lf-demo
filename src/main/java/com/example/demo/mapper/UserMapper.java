package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhouwenyu
 * date 2022-03-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
