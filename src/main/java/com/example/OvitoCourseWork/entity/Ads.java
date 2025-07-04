package com.example.OvitoCourseWork.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Ads
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long        idAds;                          // ID объявления

    private Long        idUser;                         // ID пользователя создателя
    private Long        idCategory;                     // ID категории
    private int         price;                          // Цена
    private int         count;                          // Количество
    private String      name;                           // Название
    private String      address;                        // Адрес
    private String      description;                    // Описание
    private LocalDate   datePlacement;                  // Дата размещения
    private boolean     statusProduct;                  // Статус товара

    @Column(name = "photo_product", columnDefinition = "bytea")
    private byte[]      photoProduct;                   // Фото товара

    @Transient
    private List<byte[]> photosList;                    // Временный лист для работы с фотографиями

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public void setDatePlacement(LocalDate datePlacement)
    {
        this.datePlacement = datePlacement;
    }

    public LocalDate getDatePlacement()
    {
        return datePlacement;
    }

    public void setStatusProduct(boolean statusProduct) {
        this.statusProduct = statusProduct;
    }

    public boolean isStatusProduct() {
        return statusProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getIdAds() {
        return idAds;
    }

    public void setIdAds(Long idAds) {
        this.idAds = idAds;
    }

    public byte[] getPhotoProducts() {
        return photoProduct;
    }

    public void setPhotoProducts(byte[] photoProducts) {
        this.photoProduct = photoProducts;
    }

    public List<byte[]> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<byte[]> photosList) {
        this.photosList = photosList;
    }
}