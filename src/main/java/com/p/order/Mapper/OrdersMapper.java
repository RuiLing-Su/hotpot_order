package com.p.order.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.p.order.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
