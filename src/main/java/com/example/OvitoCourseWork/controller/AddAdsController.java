package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Category;
import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.repository.AdsRepository;
import com.example.OvitoCourseWork.repository.CategoryRepository;
import com.example.OvitoCourseWork.service.AdsService;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AddAdsController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdsService adsService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/AddAdsPage")
    public String getAddAdsPage(Model model)
    {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("ads", new Ads());
        return "AddAdsPage";
    }

    @PostMapping("/AddAds")
    public String addAds(
            @ModelAttribute("ads") @Valid Ads ads,
            @RequestParam("idCategory") Long idCategory,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            Authentication authentication) throws IOException
    {

        String name = authentication.getName();
        User currentUser = userService.getUserByLogin(name);

        ads.setIdUser(currentUser.getIdUser());
        ads.setIdCategory(idCategory);
        ads.setStatusProduct(true);
        ads.setDatePlacement(LocalDate.now());

        if (photo != null && !photo.isEmpty())
        {
            ads.setPhotoProducts(photo.getBytes());
        }

        adsService.addAds(ads);
        return "redirect:/ProfilePage";
    }
}
