package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    List<Role> findByIdUser(Long idUser);
    @Modifying
    @Transactional
    @Query("DELETE FROM Role r WHERE r.idUser = :idUser")
    void deleteByIdUser(@Param("idUser") Long idUser);
}