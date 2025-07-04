package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Basket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long>
{
    Basket findByIdUser(Long idUser);

    @Modifying
    @Transactional
    @Query("DELETE FROM Basket b WHERE b.idUser = :idUser")
    void deleteByIdUser(@Param("idUser") Long idUser);
}
