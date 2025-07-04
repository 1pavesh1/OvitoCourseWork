package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    idRole;         // ID роли

    private Long    idUser;         // ID пользователя
    private String  name;           // Имя роли

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
