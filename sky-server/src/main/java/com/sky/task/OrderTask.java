package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 自定义定时任务：定时处理订单的状态
 *
 * @Author itcast
 * @Create 2024/6/23
 **/
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理支付超时订单
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void processTimeoutOrder(){
        log.info("定时任务自动执行，处理支付超时订单：" + LocalDateTime.now());

        //LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(15);
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, localDateTime);
        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);//已取消
                orders.setCancelReason("支付超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }

    /**
     * 处理“派送中”状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送中订单：{}", LocalDateTime.now());

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);//查询上个工作日产生的订单

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, localDateTime);

        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }

}
