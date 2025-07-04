package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.Basket;
import com.example.OvitoCourseWork.entity.Category;
import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.repository.BasketRepository;
import com.example.OvitoCourseWork.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService
{
    @Autowired
    private BasketRepository basketRepository;

    public void createBasketForUser(User user)
    {
        Basket basket = new Basket();
        basket.setIdUser(user.getIdUser());
        basket.setSumBasket(0); // Начальная сумма корзины 0
        basketRepository.save(basket);
    }

    public Basket getBasketByUserId(Long userId)
    {
        return basketRepository.findByIdUser(userId);
    }
}
