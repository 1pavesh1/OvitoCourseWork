package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistorySalesRepository extends JpaRepository<Sale, Long>
{
    List<Sale> findByIdUser(Long idUser);
}
