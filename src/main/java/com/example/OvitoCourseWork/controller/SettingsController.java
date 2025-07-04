package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SettingsController {
    @Autowired
    private UserService userService;

    @GetMapping("/SettingsPage")
    public String getSettingsPage(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal()))
        {
            String username = authentication.getName();
            User user = userService.getUserByLogin(username);
            model.addAttribute("user", user);
        }

        return "SettingsPage";
    }

    @PostMapping("/updateDataUser")
    public String updateDataUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated())
            {
                User currentUser = userService.getUserByLogin(auth.getName());
                user.setIdUser(currentUser.getIdUser());
                user.setLogin(currentUser.getLogin());
                user.setHashPassword(currentUser.getHashPassword());
            }
            model.addAttribute("user", user);
            return "SettingsPage";
        }

        boolean updated = userService.updateUser(user);

        if (updated)
        {
            model.addAttribute("success", "Данные успешно обновлены");
            return "redirect:/ProfilePage";
        }
        else
        {
            model.addAttribute("error", "Ошибка при обновлении пользователя");
            return "SettingsPage";
        }
    }
}