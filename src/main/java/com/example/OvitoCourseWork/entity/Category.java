package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    idCategory;                              // ID категории

    private String  name;                                    // Название категории

    public Long getIdCategory() {
        return idCategory;
    }

    public String getName() {
        return name;
    }
}
