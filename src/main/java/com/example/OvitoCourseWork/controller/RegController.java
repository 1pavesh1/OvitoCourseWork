package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController
{
    @Autowired
    private UserService userService;

    @GetMapping("/RegPage")
    public String registration(Model model)
    {
        model.addAttribute("userForm", new User());
        return "RegPage";
    }

    @PostMapping("/RegPage")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            return "RegPage";
        }
        if (!userForm.getPassword().equals(userForm.getHashPassword()))
        {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "RegPage";
        }
        if (!userService.saveUser(userForm))
        {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "RegPage";
        }

        return "RegPage";
    }
}
