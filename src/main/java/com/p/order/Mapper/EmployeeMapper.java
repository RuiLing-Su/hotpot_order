package com.p.order.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.p.order.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
