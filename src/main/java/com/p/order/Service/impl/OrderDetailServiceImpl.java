package com.p.order.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p.order.Mapper.OrderDetailMapper;
import com.p.order.Service.OrderDetailService;
import com.p.order.entity.OrderDetail;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
