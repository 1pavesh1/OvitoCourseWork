package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.*;
import com.example.OvitoCourseWork.repository.AdsRepository;
import com.example.OvitoCourseWork.repository.BasketRepository;
import com.example.OvitoCourseWork.repository.HistorySalesRepository;
import com.example.OvitoCourseWork.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService
{
    @Autowired
    private BasketService basketService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdsService adsService;
    @Autowired
    private HistorySalesRepository historySalesRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BasketRepository basketRepository;

    @Transactional
    public boolean addToBasket(Long userId, Long productId, int price)
    {
        try
        {
            Basket basket = basketService.getBasketByUserId(userId);

            if (basket == null)
            {
                User user = userService.getUserByIdUser(userId);
                if (user == null)
                {
                    return false;
                }
                basketService.createBasketForUser(user);
                basket = basketService.getBasketByUserId(userId);
                if (basket == null)
                {
                    return false;
                }
            }

            Order existingOrder = orderRepository.findByIdBasketAndIdProduct(basket.getIdBasket(), productId);

            if (existingOrder == null)
            {
                Optional<Ads> adOpt = adsRepository.findById(productId);
                if (!adOpt.isPresent() || adOpt.get().getCount() <= 0)
                {
                    return false;
                }

                Order order = new Order();
                order.setIdBasket(basket.getIdBasket());
                order.setIdProduct(productId);
                order.setOrdered(false);
                orderRepository.save(order);

                updateBasketSum(basket);
                return true;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public void updateBasketSum(Basket basket)
    {
        List<Order> orders = orderRepository.findByIdBasketAndOrderedFalse(basket.getIdBasket());

        int sum = 0;
        for (Order order : orders)
        {
            Optional<Ads> adOpt = adsRepository.findById(order.getIdProduct());
            if (adOpt.isPresent())
            {
                sum += adOpt.get().getPrice();
            }
        }

        basket.setSumBasket(sum);
        basketRepository.save(basket);
    }

    public List<Order> getBasketItems(Long basketId)
    {
        return orderRepository.findByIdBasketAndOrderedFalse(basketId);
    }

    public List<Ads> getAdsFromBasket(List<Order> basketItems)
    {
        return basketItems.stream()
                .map(order -> adsService.getAdById(order.getIdProduct()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void confirmOrders(List<Order> orders, Long deliveryMethod, String address,
                              LocalDate deliveryDate, int finalSum)
    {
        for (Order order : orders)
        {
            Optional<Ads> adsOptional = adsRepository.findById(order.getIdProduct());
            if (adsOptional.isPresent())
            {
                Ads ad = adsOptional.get();

                ad.setStatusProduct(false);
                adsRepository.save(ad);

                Sale sale = new Sale();
                sale.setIdUser(ad.getIdUser());
                sale.setCount(1);
                sale.setPrice(ad.getPrice());
                sale.setName(ad.getName());
                sale.setDateSale(LocalDate.now());

                historySalesRepository.save(sale);
            }
        }
    }

    public Optional<Order> getOrderById(Long orderId)
    {
        return orderRepository.findById(orderId);
    }

    public List<Order> getUserOrders(Long idUser)
    {
        return orderRepository.findUserOrders(idUser);
    }

    public boolean isOrderBelongsToUser(Long idOrder, Long idUser)
    {
        return orderRepository.isOrderBelongsToUser(idOrder, idUser);
    }
}
