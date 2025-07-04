package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdsRepository extends JpaRepository<Ads, Long>
{
    List<Ads> findAllByStatusProduct(boolean status);
    List<Ads> findByIdUser(Long idUser);

    List<Ads> findByIdCategoryAndAddressContaining(Long idCategory, String city);
    List<Ads> findByIdCategory(Long idCategory);
    List<Ads> findByAddressContaining(String city);

    Ads findAdsByIdAds(Long idAds);

    @Query("SELECT a FROM Ads a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Ads> findByNameOrDescriptionContaining(@Param("query") String query);
}