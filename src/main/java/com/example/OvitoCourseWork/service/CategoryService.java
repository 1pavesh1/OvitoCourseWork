package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.Category;
import com.example.OvitoCourseWork.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService
{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public String getCategoryNameById(Long id)
    {
        if (id == null)
        {
            return "Другое";
        }

        Optional<Category> category = categoryRepository.findById(id);
        return category.map(Category::getName).orElse("Другое");
    }

    public Map<Long, String> getCategoriesMap()
    {
        Map<Long, String> categoriesMap = new HashMap<>();
        List<Category> categories = findAllCategories();

        for (Category category : categories)
        {
            categoriesMap.put(category.getIdCategory(), category.getName());
        }

        return categoriesMap;
    }
}