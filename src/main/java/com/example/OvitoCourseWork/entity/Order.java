package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long        idOrder;        // ID заказа

    private Long        idBasket;       // ID корзины
    private Long        idProduct;      // ID товара
    private Long        idDelivery;     // ID доставки
    private int         costOrder;      // Стоимость заказа
    private String      address;        // Адрес доставки
    private LocalDate   dateDelivery;   // Дата доставки
    private boolean     ordered;        // Заказан?

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdBasket() {
        return idBasket;
    }

    public void setIdBasket(Long idBasket) {
        this.idBasket = idBasket;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Long getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(Long idDelivery) {
        this.idDelivery = idDelivery;
    }

    public int getCostOrder() {
        return costOrder;
    }

    public void setCostOrder(int costOrder) {
        this.costOrder = costOrder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(LocalDate dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }
}
