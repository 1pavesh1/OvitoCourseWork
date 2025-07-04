package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long>
{

}
