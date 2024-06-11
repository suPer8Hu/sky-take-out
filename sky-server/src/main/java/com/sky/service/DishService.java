package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * 菜品管理业务接口
 *
 * @Author itcast
 * @Create 2024/6/11
 **/
public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);
}
