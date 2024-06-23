package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * desc
 *
 * @Author itcast
 * @Create 2024/6/18
 **/
@Mapper
public interface OrderMapper {

    @Insert("insert into orders\n" +
            "        (number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark,\n" +
            "         phone, address, consignee, estimated_delivery_time, delivery_status, pack_amount, tableware_number,\n" +
            "         tableware_status)\n" +
            "        values (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{payMethod},\n" +
            "                #{payStatus}, #{amount}, #{remark}, #{phone}, #{address}, #{consignee},\n" +
            "                #{estimatedDeliveryTime}, #{deliveryStatus}, #{packAmount}, #{tablewareNumber}, #{tablewareStatus})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Orders order);

    @Select("select * from orders where number = #{number}")
    Orders getByNumber(String number);

    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     * @return
     */
    //select * from orders where status = 未支付 and order_time < 当前时间 - 15分钟
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);
}
