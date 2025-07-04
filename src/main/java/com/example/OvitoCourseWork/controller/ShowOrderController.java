package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.*;
import com.example.OvitoCourseWork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ShowOrderController
{
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdsService adsService;
    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/order/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model, Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/login";
        }

        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (!orderOpt.isPresent())
        {
            return "redirect:/ProfilePage#orders";
        }

        Order order = orderOpt.get();
        User user = userService.getUserByLogin(authentication.getName());

        if (!orderService.isOrderBelongsToUser(order.getIdOrder(), user.getIdUser()))
        {
            return "redirect:/ProfilePage#orders";
        }

        Optional<Ads> adOpt = adsService.getAdById(order.getIdProduct());
        Optional<Delivery> deliveryOpt = deliveryService.getDeliveryById(order.getIdDelivery());

        model.addAttribute("order", order);
        model.addAttribute("ad", adOpt.orElse(null));
        model.addAttribute("delivery", deliveryOpt.orElse(null));
        model.addAttribute("user", user);

        return "ShowOrder";
    }
}