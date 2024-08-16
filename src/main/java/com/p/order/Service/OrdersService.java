package com.p.order.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.p.order.entity.Orders;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders);
}
