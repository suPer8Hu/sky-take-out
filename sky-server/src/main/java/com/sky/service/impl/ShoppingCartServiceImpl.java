package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车业务实现
 *
 * @Author itcast
 * @Create 2024/6/17
 **/
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //1、查询购物车中是否存在该商品(有可能是菜品，也可能是套餐)
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCartDTO);

        //2、如果已经存在，则数量 + 1
        if(list != null && list.size() > 0){
            ShoppingCart shoppingCart = list.get(0);
            //update shopping_cart set number = ? where id = ?
            shoppingCartMapper.updateNumberById(shoppingCart.getNumber() + 1,shoppingCart.getId());
        }else{
            //3、如果不存在，向购物车表插入1条数据
            // insert into shopping_cart(...) values(...)
            //判断本次添加的商品是菜品还是套餐
            ShoppingCart shoppingCart = null;
            if(shoppingCartDTO.getDishId() != null){
                //本次添加的是菜品，查询菜品表，获取菜品的名称、图片路径、单价
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                //封装购物车对象
                shoppingCart = ShoppingCart.builder()
                        .dishId(shoppingCartDTO.getDishId())
                        .name(dish.getName())
                        .image(dish.getImage())
                        .dishFlavor(shoppingCartDTO.getDishFlavor())
                        .amount(dish.getPrice())
                        .build();
            }else {
                //本次添加的是套餐，查询套餐表，获取套餐的名称、图片路径、单价
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                //封装购物车对象
                shoppingCart = ShoppingCart.builder()
                        .setmealId(shoppingCartDTO.getSetmealId())
                        .name(setmeal.getName())
                        .image(setmeal.getImage())
                        .amount(setmeal.getPrice())
                        .build();
            }
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 查看购物车
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        //select * from shopping_cart where user_id = ?
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.getByUserId(userId);
        return list;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {//方法名体现的是业务功能
        // delete from shopping_cart where user_id = ?
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());//体现的是数据库操作
    }
}
