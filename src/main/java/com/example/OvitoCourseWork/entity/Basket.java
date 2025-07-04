package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basket")
public class Basket
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    idBasket;                                  // ID корзины

    private Long    idUser;                                    // ID пользователя которому принадлежит корзина
    private int     sumBasket;                                 // Сумма корзины

    public Long getIdBasket() {
        return idBasket;
    }

    public void setIdBasket(Long idBasket) {
        this.idBasket = idBasket;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public int getSumBasket() {
        return sumBasket;
    }

    public void setSumBasket(int sumBasket) {
        this.sumBasket = sumBasket;
    }
}
