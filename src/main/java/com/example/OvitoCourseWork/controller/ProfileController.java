package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.*;
import com.example.OvitoCourseWork.generator.ChartGenerator;
import com.example.OvitoCourseWork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdsService adsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HistorySalesService historySalesService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/ProfilePage")
    public String profilePage(Model model, Authentication authentication,
                              @RequestParam(defaultValue = "ads") String tab)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/AuthPage";
        }

        User user = userService.getUserByLogin(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("activeTab", tab);

        if (user.isAdmin())
        {
            if ("users".equals(tab))
            {
                List<User> users = userService.getAllUsers();
                model.addAttribute("users", users);
            }
        }
        else
        {
            switch (tab)
            {
                case "orders":
                    List<Order> orders = orderService.getUserOrders(user.getIdUser());
                    model.addAttribute("orders", orders);
                    break;
                case "sales":
                    List<Sale> sales = historySalesService.getUserSales(user.getIdUser());
                    model.addAttribute("sales", sales);
                    break;
                case "stats":
                    generateStatsCharts(user, model);
                    break;
                default: // "ads"
                    List<Ads> adsList = adsService.getAdsByUserId(user.getIdUser());
                    model.addAttribute("adsList", adsList);
                    break;
            }
        }

        return "ProfilePage";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam Long userId, Authentication authentication)
    {
        User admin = userService.getUserByLogin(authentication.getName());
        if (admin != null && admin.isAdmin())
        {
            userService.deleteUser(userId);
        }
        return "redirect:/ProfilePage?tab=users";
    }

    private void generateStatsCharts(User user, Model model)
    {
        List<Ads> userAds = adsService.getAdsByUserId(user.getIdUser());

        Map<String, Integer> categoryStats = new HashMap<>();
        for (Ads ad : userAds)
        {
            String categoryName = categoryService.getCategoryNameById(ad.getIdCategory());
            categoryStats.put(categoryName, categoryStats.getOrDefault(categoryName, 0) + 1);
        }

        int activeCount = 0;
        int soldCount = 0;

        for (Ads ad : userAds)
        {
            if (ad.isStatusProduct())
            {
                activeCount++;
            }
            else
            {
                soldCount++;
            }
        }

        String categoryChart = ChartGenerator.generatePieChart(
                categoryStats, ChartGenerator.CATEGORY_COLORS, 350, 250);

        String statusChart = ChartGenerator.generatePieChart(
                Map.of("В продаже", activeCount, "Продано", soldCount),
                ChartGenerator.STATUS_COLORS, 350, 250);

        model.addAttribute("categoryChart", categoryChart);
        model.addAttribute("statusChart", statusChart);
        model.addAttribute("categoryStats", categoryStats);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("soldCount", soldCount);
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(Authentication authentication, HttpServletRequest request)
    {
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal()))
        {
            String username = authentication.getName();
            User user = userService.getUserByLogin(username);
            if (user != null)
            {
                userService.deleteUser(user.getIdUser());
                request.getSession().invalidate();
            }
        }
        return "redirect:/login?deleted";
    }
}