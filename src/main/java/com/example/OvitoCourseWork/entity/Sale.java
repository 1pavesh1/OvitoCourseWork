package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "history_sales")
public class Sale
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long        idHistorySales; // ID истории продажи

    private Long        idUser;         // ID пользователя
    private int         count;          // Количество проданного товара
    private int         price;          // Цена
    private String      name;           // Название товара
    private LocalDate   dateSale;       // Дата продажи

    public Long getIdHistorySales() {
        return idHistorySales;
    }

    public void setIdHistorySales(Long idHistorySales) {
        this.idHistorySales = idHistorySales;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateSale() {
        return dateSale;
    }

    public void setDateSale(LocalDate dateSale) {
        this.dateSale = dateSale;
    }
}
