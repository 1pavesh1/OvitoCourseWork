package com.example.OvitoCourseWork.repository;

import com.example.OvitoCourseWork.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>
{

}
