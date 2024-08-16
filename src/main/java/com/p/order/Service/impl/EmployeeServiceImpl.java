package com.p.order.Service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p.order.Mapper.EmployeeMapper;
import com.p.order.Service.EmployeeService;
import com.p.order.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
