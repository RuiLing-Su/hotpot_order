package com.p.order.dto;

import com.p.order.entity.OrderDetail;
import com.p.order.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;

    private int setSumNum;
}
