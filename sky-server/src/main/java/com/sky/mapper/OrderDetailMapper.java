package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * desc
 *
 * @Author itcast
 * @Create 2024/6/18
 **/
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量保存订单明细
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
