package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.*;
import com.example.OvitoCourseWork.repository.AdsRepository;
import com.example.OvitoCourseWork.repository.DeliveryRepository;
import com.example.OvitoCourseWork.repository.HistorySalesRepository;
import com.example.OvitoCourseWork.repository.OrderRepository;
import com.example.OvitoCourseWork.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderController
{
    @Autowired
    private UserService userService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private HistorySalesRepository historySalesRepository;

    @GetMapping("/OrderPage")
    public String showOrderPage(Model model, Principal principal, HttpSession session)
    {
        if (principal == null)
        {
            return "redirect:/AuthPage";
        }

        List<Long> selectedOrderIds = (List<Long>) session.getAttribute("selectedOrderIds");
        Long basketId = (Long) session.getAttribute("basketId");

        if (selectedOrderIds == null || selectedOrderIds.isEmpty() || basketId == null)
        {
            return "redirect:/BasketPage";
        }

        User user = userService.getUserByLogin(principal.getName());
        Basket basket = basketService.getBasketByUserId(user.getIdUser());

        if (basket == null || !basket.getIdBasket().equals(basketId))
        {
            return "redirect:/BasketPage";
        }

        List<Order> basketItems = orderRepository.findAllById(selectedOrderIds).stream()
                .filter(order -> order.getIdBasket().equals(basketId))
                .collect(Collectors.toList());

        if (basketItems.isEmpty())
        {
            return "redirect:/BasketPage";
        }

        List<Ads> adsList = orderService.getAdsFromBasket(basketItems);

        int totalSum = adsList.stream().mapToInt(Ads::getPrice).sum();
        int deliveryCost = (int) Math.round(totalSum * 0.05);
        int finalSum = totalSum + deliveryCost;

        List<Delivery> deliveries = deliveryRepository.findAll();
        LocalDate deliveryDate = LocalDate.now().plusDays(2 + (int)(Math.random() * 2));
        String formattedDate = deliveryDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        model.addAttribute("basketItems", basketItems);
        model.addAttribute("adsList", adsList);
        model.addAttribute("userAddress", user.getAddress());
        model.addAttribute("deliveryCost", deliveryCost);
        model.addAttribute("totalSum", totalSum);
        model.addAttribute("finalSum", finalSum);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("deliveryDate", formattedDate);

        return "OrderPage";
    }

    @PostMapping("/order/confirm")
    public String confirmOrder(
            @RequestParam String paymentMethod,
            @RequestParam Long deliveryMethod,
            @RequestParam String address,
            Principal principal,
            HttpSession session,
            RedirectAttributes redirectAttributes)
    {

        if (principal == null)
        {
            return "redirect:/AuthPage";
        }

        List<Long> selectedOrderIds = (List<Long>) session.getAttribute("selectedOrderIds");
        Long basketId = (Long) session.getAttribute("basketId");

        if (selectedOrderIds == null || selectedOrderIds.isEmpty() || basketId == null)
        {
            return "redirect:/BasketPage";
        }

        User user = userService.getUserByLogin(principal.getName());
        Basket basket = basketService.getBasketByUserId(user.getIdUser());

        if (basket == null || !basket.getIdBasket().equals(basketId))
        {
            return "redirect:/BasketPage";
        }

        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryMethod);
        if (!deliveryOpt.isPresent())
        {
            redirectAttributes.addFlashAttribute("error", "Выбранный способ доставки не найден");
            return "redirect:/OrderPage";
        }

        List<Order> orders = orderRepository.findAllById(selectedOrderIds).stream()
                .filter(order -> order.getIdBasket().equals(basketId))
                .collect(Collectors.toList());

        LocalDate deliveryDate = LocalDate.now().plusDays(2 + (int)(Math.random() * 2));

        List<Ads> adsList = orderService.getAdsFromBasket(orders);
        int totalSum = adsList.stream().mapToInt(Ads::getPrice).sum();
        int deliveryCost = (int) Math.round(totalSum * 0.05);
        int finalSum = totalSum + deliveryCost;

        for (Order order : orders)
        {
            order.setOrdered(true);
            order.setIdDelivery(deliveryMethod);
            order.setAddress(address);
            order.setDateDelivery(deliveryDate);
            order.setCostOrder(finalSum);
            orderRepository.save(order);

            Optional<Ads> adOptional = adsRepository.findById(order.getIdProduct());
            if (adOptional.isPresent())
            {
                Ads ad = adOptional.get();
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

        session.removeAttribute("selectedOrderIds");
        session.removeAttribute("basketId");

        orderService.updateBasketSum(basket);

        return "redirect:/ProfilePage?tab=orders";
    }
}