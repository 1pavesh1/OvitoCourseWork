package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery")
public class Delivery
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    idDelivery;     // ID доставки

    private String  name;           // Название доставки
    private String  phone;          // Номер доставки

    public Long getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(Long idDelivery) {
        this.idDelivery = idDelivery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
