package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Category;
import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.service.AdsService;
import com.example.OvitoCourseWork.service.CategoryService;
import com.example.OvitoCourseWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController
{
    @Autowired
    private UserService userService;
    @Autowired
    private AdsService adsService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/MainSearchPage")
    public String getMainPage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String searchQuery,
            Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal()))
        {
            String username = authentication.getName();
            User user = userService.getUserByLogin(username);
            model.addAttribute("user", user);

            boolean isProfileComplete = userService.isProfileComplete(user);
            model.addAttribute("isProfileComplete", isProfileComplete);
        }
        List<Ads> adsList = adsService.filterAds(categoryId, city, searchQuery);
        model.addAttribute("adsList", adsList);
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedCity", city);
        model.addAttribute("searchQuery", searchQuery);
        return "MainSearchPage";
    }

    @GetMapping("/ads/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id)
    {
        Optional<Ads> ad = adsService.getAdById(id);
        if (ad.isPresent())
        {
            byte[] photo = ad.get().getPhotoProducts();
            if (photo != null && photo.length > 0)
            {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(photo, headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}