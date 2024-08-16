package com.p.order.Service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p.order.Mapper.SetmealMapper;
import com.p.order.Service.CategoryService;
import com.p.order.Service.SetmealDishService;
import com.p.order.Service.SetmealService;
import com.p.order.common.CustomException;
import com.p.order.dto.SetmealDto;
import com.p.order.entity.Setmeal;
import com.p.order.entity.SetmealDish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * 根据ID查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     * 根据ID保存更改后的套餐
     * @param setmealDto
     */
    @Override
    public void updateByIdWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        Long id = setmealDto.getId();
        setmealDishService.removeById(id);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //解决dish重复问题，把相关的setmealDish移除后重新添加
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> list = setmealDishes.stream().map((item) -> {
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联关系
     * @param ids
     */
    @Transactional
    public void removeByIdWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.in(Setmeal::getId,ids);

        int count = (int) this.count(queryWrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中，无法删除...");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(wrapper);
    }


}
