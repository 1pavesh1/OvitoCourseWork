package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.*;
import com.example.OvitoCourseWork.repository.BasketRepository;
import com.example.OvitoCourseWork.repository.OrderRepository;
import com.example.OvitoCourseWork.service.AdsService;
import com.example.OvitoCourseWork.service.BasketService;
import com.example.OvitoCourseWork.service.OrderService;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BasketController
{
    @Autowired
    private BasketService basketService;
    @Autowired
    UserService userService;
    @Autowired
    private AdsService adsService;
    @Autowired
    OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/BasketPage")
    public String showBasket(Model model, Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/login";
        }

        User user = userService.getUserByLogin(authentication.getName());
        Basket basket = basketService.getBasketByUserId(user.getIdUser());

        List<Order> basketItems = new ArrayList<>();
        List<Ads> adsList = new ArrayList<>();
        int totalSum = 0;

        if (basket != null)
        {
            basketItems = orderRepository.findByIdBasketAndOrderedFalse(basket.getIdBasket());
            for (Order order : basketItems)
            {
                Optional<Ads> adOpt = adsService.getAdById(order.getIdProduct());
                adOpt.ifPresent(adsList::add);
                if (adOpt.isPresent())
                {
                    totalSum += adOpt.get().getPrice();
                }
            }
        }

        int deliveryCost = (int) Math.round(totalSum * 0.05);

        model.addAttribute("basketItems", basketItems);
        model.addAttribute("adsList", adsList);
        model.addAttribute("basket", basket);
        model.addAttribute("userAddress", user.getAddress());
        model.addAttribute("deliveryCost", deliveryCost);
        model.addAttribute("totalSum", totalSum);
        model.addAttribute("finalSum", totalSum + deliveryCost);

        return "BasketPage";
    }


    @PostMapping("/basket/delete/{id}")
    public String deleteOrder(@PathVariable Long id, Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/login";
        }

        Optional<Order> orderOpt = orderRepository.findById(id);
        if (!orderOpt.isPresent())
        {
            return "redirect:/BasketPage";
        }

        Order order = orderOpt.get();

        User user = userService.getUserByLogin(authentication.getName());
        Basket basket = basketService.getBasketByUserId(user.getIdUser());

        if (!order.getIdBasket().equals(basket.getIdBasket()))
        {
            return "redirect:/BasketPage";
        }

        orderRepository.delete(order);

        orderService.updateBasketSum(basket);

        return "redirect:/BasketPage";
    }

    @PostMapping("/basket/order")
    public String prepareOrder(
            @RequestParam(name = "agreement", defaultValue = "false") boolean agreement,
            @RequestParam(name = "selectedItems", required = false) List<Long> selectedItems,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            HttpSession session)
    {

        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/login";
        }

        User user = userService.getUserByLogin(authentication.getName());
        Basket basket = basketService.getBasketByUserId(user.getIdUser());

        if (basket == null)
        {
            redirectAttributes.addFlashAttribute("error", "Корзина не найдена");
            return "redirect:/BasketPage";
        }

        if (!agreement)
        {
            redirectAttributes.addFlashAttribute("error", "Вы должны согласиться с правилами, чтобы оформить заказ.");
            return "redirect:/BasketPage";
        }

        List<Order> orders = orderRepository.findByIdBasketAndOrderedFalse(basket.getIdBasket());

        if (orders == null || orders.isEmpty())
        {
            redirectAttributes.addFlashAttribute("error", "Ваша корзина пуста");
            return "redirect:/BasketPage";
        }

        if (selectedItems == null || selectedItems.isEmpty())
        {
            selectedItems = orders.stream()
                    .map(Order::getIdOrder)
                    .collect(Collectors.toList());
        }

        session.setAttribute("selectedOrderIds", selectedItems);
        session.setAttribute("basketId", basket.getIdBasket());

        return "redirect:/OrderPage";
    }
}