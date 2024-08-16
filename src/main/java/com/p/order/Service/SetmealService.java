package com.p.order.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.p.order.dto.SetmealDto;
import com.p.order.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据ID查询套餐
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);


    public void updateByIdWithDish(SetmealDto setmealDto);


    public void removeByIdWithDish(List<Long> ids);

}
