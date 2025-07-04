package com.example.OvitoCourseWork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController
{
    @GetMapping("/MainPage")
    public String getMainPage(Model model)
    {
        // Добавляем данные о курсовой работе
        model.addAttribute("courseWorkTitle", "Система управления объявлениями Ovito");
        model.addAttribute("courseWorkDescription",
                "Данное веб-приложение представляет собой полнофункциональный маркетплейс с возможностью:\n\n" +
                        "• Регистрации и авторизации пользователей\n" +
                        "• Размещения и редактирования объявлений\n" +
                        "• Поиска товаров по категориям и местоположению\n" +
                        "• Добавления товаров в корзину и оформления заказов\n\n" +
                        "Технологический стек: Java 17, Spring Boot 3, Thymeleaf, PostgreSQL, Hibernate");

        // Информация о БД
        model.addAttribute("dbInfo", "AvitoDB - реляционная база данных, спроектированная для хранения:");
        model.addAttribute("dbSchema",
                "Основные таблицы:\n" +
                        "1. accounts - учетные записи пользователей\n" +
                        "2. product - товары и объявления\n" +
                        "3. categories - категории товаров\n" +
                        "4. delivery - доставка\n" +
                        "5. orders - заказы пользователей\n" +
                        "6. roles - роль пользователя\n" +
                        "7. basket - корзина пользователя\n" +
                        "8. history_sales - история продаж\n");

        return "MainPage";
    }
}