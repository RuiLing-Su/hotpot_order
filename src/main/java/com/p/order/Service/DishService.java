package com.p.order.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.p.order.dto.DishDto;
import com.p.order.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
