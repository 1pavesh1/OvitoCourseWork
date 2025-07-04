package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    Order findByIdBasketAndIdProduct(Long idBasket, Long idProduct);
    List<Order> findByIdBasketAndOrderedFalse(Long idBasket);
    @Query("SELECT o FROM Order o WHERE o.idBasket IN (SELECT b.idBasket FROM Basket b WHERE b.idUser = :userId) AND o.ordered = true")
    List<Order> findUserOrders(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.idOrder = :orderId AND o.idBasket IN (SELECT b.idBasket FROM Basket b WHERE b.idUser = :userId) AND o.ordered = true")
    boolean isOrderBelongsToUser(@Param("orderId") Long orderId, @Param("userId") Long userId);
}