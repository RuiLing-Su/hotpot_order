package com.p.order.dto;

import com.p.order.entity.Setmeal;
import com.p.order.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
