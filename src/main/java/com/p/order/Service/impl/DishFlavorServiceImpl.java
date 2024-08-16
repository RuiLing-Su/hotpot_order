package com.p.order.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p.order.Mapper.DishFlavorMapper;
import com.p.order.Service.DishFlavorService;
import com.p.order.entity.DishFlavor;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
