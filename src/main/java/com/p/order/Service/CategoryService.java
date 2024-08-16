package com.p.order.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.p.order.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
