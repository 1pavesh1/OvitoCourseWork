package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.Delivery;
import com.example.OvitoCourseWork.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService
{

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<Delivery> getAllDeliveries()
    {
        return deliveryRepository.findAll();
    }

    public Optional<Delivery> getDeliveryById(Long idDelivery)
    {
        return deliveryRepository.findById(idDelivery);
    }
}
