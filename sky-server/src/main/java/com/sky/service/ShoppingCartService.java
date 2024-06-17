package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * 购物车业务接口
 *
 * @Author itcast
 * @Create 2024/6/17
 **/
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
