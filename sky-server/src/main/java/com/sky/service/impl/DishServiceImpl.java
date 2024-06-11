package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishflavorMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品管理业务实现
 *
 * @Author itcast
 * @Create 2024/6/11
 **/
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishflavorMapper dishflavorMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //操作几张表？2张表： 菜品表和口味表

        //1.向菜品表插入1条数据，需要返回菜品的id
        dishMapper.insert(dish);

        Long dishId = dish.getId();//获取生成的主键值

        List<DishFlavor> flavors = dishDTO.getFlavors();//获得口味
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

            //2.向口味表插入n条数据
            dishflavorMapper.insertBatch(flavors);
        }

    }
}
