package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Category;
import com.example.OvitoCourseWork.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>
{

}
