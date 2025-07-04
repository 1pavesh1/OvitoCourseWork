package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByLogin(String login);
    Optional<User> findByIdUser(Long idUser);
}
