package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

/**
 * 订单业务接口
 *
 * @Author itcast
 * @Create 2024/6/18
 **/
public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(String outTradeNo);
}
