package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 购物车持久层接口
 *
 * @Author itcast
 * @Create 2024/6/17
 **/
@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> list(ShoppingCartDTO shoppingCartDTO);

    /**
     * 更新商品数量
     * @param number
     * @param id
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(int number, Long id);

    /**
     * 新增购物车数据
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);
}
