package com.example.OvitoCourseWork.service;

import org.springframework.stereotype.Service;

@Service
public class IconService
{
    public String getIconForCategory(String categoryName)
    {
        return switch (categoryName.toLowerCase())
        {
            case "авто" -> "car";
            case "недвижимость" -> "home";
            case "электроника" -> "laptop";
            case "одежда" -> "tshirt";
            case "мебель" -> "couch";
            case "животные" -> "dog";
            default -> "tag";
        };
    }
}