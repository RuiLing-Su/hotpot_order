package com.p.order.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.p.order.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {
}
