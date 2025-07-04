package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>
{
    Optional<Delivery> findById(Long idDelivery);
}