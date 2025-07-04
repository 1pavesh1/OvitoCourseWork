package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthController
{
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/AuthPage")
    public String getAuthPage(Model model)
    {
        model.addAttribute("user", new User());
        return "AuthPage";
    }

    @PostMapping("/login")
    public String authUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request)
    {
        if (bindingResult.hasErrors())
        {
            return "AuthPage";
        }

        try
        {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getLogin(),
                    user.getSimplePassword());
            Authentication authenticated = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            User fullUser = userService.getUserByLogin(user.getLogin());
            request.getSession().setAttribute("currentUser", fullUser);

            return "redirect:/MainSearchPage";
        }
        catch (AuthenticationException e)
        {
            model.addAttribute("error", "Неверный логин или пароль");
            return "AuthPage";
        }
    }
}
