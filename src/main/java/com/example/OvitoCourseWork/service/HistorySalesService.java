package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.Sale;
import com.example.OvitoCourseWork.repository.HistorySalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorySalesService
{
    @Autowired
    private HistorySalesRepository historySalesRepository;

    public List<Sale> getUserSales(Long idUser)
    {
        return historySalesRepository.findByIdUser(idUser);
    }
}

