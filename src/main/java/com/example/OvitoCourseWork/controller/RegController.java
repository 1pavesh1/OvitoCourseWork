package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegController
{
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/RegPage")
    public String getRegPage(Model model)
    {
        model.addAttribute("user", new User());
        return "RegPage";
    }

    @PostMapping("/reg")
    public String regUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            Model model)
    {

        if (bindingResult.hasErrors())
        {
            return "RegPage";
        }

        if (userService.findUserByLogin(user.getLogin()))
        {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "RegPage";
        }

        if (!userService.saveUser(user))
        {
            model.addAttribute("error", "Ошибка при сохранении пользователя");
            return "RegPage";
        }

        try
        {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getLogin(),
                    user.getSimplePassword());
            Authentication authenticated = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return "redirect:/MainSearchPage";
        }
        catch (AuthenticationException e)
        {
            return "redirect:/RegPage";
        }
    }
}
