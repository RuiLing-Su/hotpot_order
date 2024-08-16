package com.p.order.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p.order.Mapper.UserMapper;
import com.p.order.Service.UserService;
import com.p.order.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
