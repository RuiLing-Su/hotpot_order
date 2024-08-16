package com.p.order.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.p.order.Service.OrderDetailService;
import com.p.order.Service.OrdersService;
import com.p.order.Service.ShoppingCartService;
import com.p.order.Service.UserService;
import com.p.order.common.BaseContext;
import com.p.order.common.R;
import com.p.order.dto.OrderDto;
import com.p.order.entity.OrderDetail;
import com.p.order.entity.Orders;
import com.p.order.entity.ShoppingCart;
import com.p.order.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info(orders.toString());
        ordersService.submit(orders);
        return R.success("下单成功！");
    }

    /**
     * 用户通过订单id查询订单明细
     * @param orderId
     * @return
     */
    //这里抽离出来是为了避免在stream中遍历的时候直接使用构造条件来查询导致eq叠加，从而导致后面查询的数据都是null
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId){
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        return orderDetailList;
    }

    /**
     * 用户端展示自己的订单分页查询
     * @param page
     * @param pageSize
     * @return
     * */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrderDto> pageDto = new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,BaseContext.getCurrentId());
        //这里是直接把当前用户分页的全部结果查询出来，要添加用户id作为查询条件，否则会出现用户可以查询到其他用户的订单情况
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo,queryWrapper);

        //通过OrderId查询对应的OrderDetail
        LambdaQueryWrapper<OrderDetail> queryWrapper2 = new LambdaQueryWrapper<>();

        //对OrderDto进行需要的属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrderDto> orderDtoList = records.stream().map((item) ->{
            OrderDto orderDto = new OrderDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = this.getOrderDetailListByOrderId(orderId);
            BeanUtils.copyProperties(item,orderDto);
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //使用dto的分页有点难度.....需要重点掌握
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        pageDto.setRecords(orderDtoList);
        return R.success(pageDto);
    }

    /**
     * 再来一单
     * @param orders1
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders1){
        //查看订单明细表
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,orders1.getId());
        List<OrderDetail> list = orderDetailService.list(queryWrapper);
        //删除购物车已有的数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(wrapper);
        //向购物车中插入多条数据
        List<ShoppingCart> shoppingCartList = list.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            //设置购物车ID
            long shoppingCartId = IdWorker.getId();
            //向购物车插入数据
            shoppingCart.setId(shoppingCartId);
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setDishId(item.getDishId());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setSetmealId(item.getSetmealId());
            shoppingCart.setImage(item.getImage());
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartService.saveBatch(shoppingCartList);

        return R.success("下单成功！");
    }

    /**
     * 管理端查看订单
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime,String endTime){
        Page<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        if (beginTime!=null && endTime!=null) {
            queryWrapper.ge(Orders::getOrderTime, beginTime);
            queryWrapper.le(Orders::getCheckoutTime, endTime);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(page1, queryWrapper);

        return R.success(page1);
    }

    /**
     * 管理员更改订单状态
     * @param orderDto
     * @return
     */
    @PutMapping
    public R<String> changeStatus(@RequestBody OrderDto orderDto){
        Long id = orderDto.getId();
        Orders orders = ordersService.getById(id);
        orders.setStatus(orderDto.getStatus());
        ordersService.updateById(orders);
        return R.success("订单状态修改成功！");
    }


}
