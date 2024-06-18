package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
}
