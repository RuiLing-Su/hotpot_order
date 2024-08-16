package com.p.order.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p.order.Mapper.ShoppingCartMapper;
import com.p.order.Service.ShoppingCartService;
import com.p.order.entity.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
