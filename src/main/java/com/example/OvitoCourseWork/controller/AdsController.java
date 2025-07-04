package com.example.OvitoCourseWork.controller;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Category;
import com.example.OvitoCourseWork.entity.Order;
import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.repository.AdsRepository;
import com.example.OvitoCourseWork.repository.CategoryRepository;
import com.example.OvitoCourseWork.service.AdsService;
import com.example.OvitoCourseWork.service.CategoryService;
import com.example.OvitoCourseWork.service.OrderService;
import com.example.OvitoCourseWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ads")
public class AdsController {
    @Autowired
    private AdsService adsService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public String viewAd(@PathVariable Long id,
                         @RequestParam(name = "imageIndex", defaultValue = "0") int imageIndex,
                         Model model,
                         Authentication authentication)
    {
        Optional<Ads> adOpt = adsService.getAdById(id);

        if (!adOpt.isPresent())
        {
            return "redirect:/MainSearchPage";
        }

        Ads ad = adOpt.get();
        List<byte[]> photos = new ArrayList<>();

        if (ad.getPhotoProducts() != null && ad.getPhotoProducts().length > 0)
        {
            photos.add(ad.getPhotoProducts());
        }

        int totalImages = photos.size();

        if (imageIndex >= totalImages && totalImages > 0)
        {
            imageIndex = totalImages - 1;
        }

        if (authentication != null && authentication.isAuthenticated())
        {
            User user = userService.getUserByLogin(authentication.getName());
            model.addAttribute("user", user);
            boolean canBuy = userService.isProfileComplete(user) && !ad.getIdUser().equals(user.getIdUser());
            model.addAttribute("canBuy", canBuy);
        }

        model.addAttribute("ad", ad);
        model.addAttribute("currentImageIndex", imageIndex);
        model.addAttribute("totalImages", totalImages);

        return "AdsPage";
    }

    @PostMapping("/{id}/show-phone")
    public String showPhone(@PathVariable Long id,
                            Model model,
                            Authentication authentication)
    {
        Optional<Ads> adOpt = adsService.getAdById(id);
        if (adOpt.isPresent())
        {
            Ads ad = adOpt.get();
            model.addAttribute("ad", ad);

            // Сохраняем информацию о фотографиях
            List<byte[]> photos = new ArrayList<>();

            if (ad.getPhotoProducts() != null && ad.getPhotoProducts().length > 0)
            {
                photos.add(ad.getPhotoProducts());
            }

            model.addAttribute("totalImages", photos.size());
            model.addAttribute("currentImageIndex", 0);

            User seller = userService.getUserByIdUser(ad.getIdUser());
            model.addAttribute("seller", seller);
            model.addAttribute("phone", seller.getPhone());

            User user = null;
            boolean canBuy = false;

            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal()))
            {
                user = userService.getUserByLogin(authentication.getName());
                model.addAttribute("user", user);
                canBuy = userService.isProfileComplete(user) && !ad.getIdUser().equals(user.getIdUser());
            }

            model.addAttribute("canBuy", canBuy);

            return "AdsPage";
        }
        return "redirect:/MainSearchPage";
    }

    @GetMapping("/edit/{id}")
    public String editAdForm(@PathVariable Long id, Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUserByLogin(username);

        Optional<Ads> ad = adsService.getAdById(id);
        if (ad.isPresent() && (ad.get().getIdUser().equals(currentUser.getIdUser()) || currentUser.isAdmin()))
        {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("ad", ad.get());
            model.addAttribute("isAdmin", currentUser.isAdmin());
            return "EditAdsPage";
        }
        return "redirect:/ads/" + id;
    }

    @PostMapping("/edit/{id}")
    public String editAdSubmit(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam int price,
                               @RequestParam int count,
                               @RequestParam String address,
                               @RequestParam Long idCategory,
                               @RequestParam(value = "photo", required = false) MultipartFile photo,
                               Authentication authentication) throws IOException
    {

        String username = authentication.getName();
        User currentUser = userService.getUserByLogin(username);

        Optional<Ads> existingAd = adsService.getAdById(id);
        if (existingAd.isPresent() &&
                (existingAd.get().getIdUser().equals(currentUser.getIdUser()) || currentUser.isAdmin()))
        {

            Ads ad = existingAd.get();
            ad.setName(name);
            ad.setDescription(description);
            ad.setPrice(price);
            ad.setCount(count);
            ad.setAddress(address);
            ad.setIdCategory(idCategory);

            if (photo != null && !photo.isEmpty())
            {
                ad.setPhotoProducts(photo.getBytes());
            }

            adsService.updateAds(ad);
        }
        return "redirect:/ads/" + id;
    }

    @PostMapping("/{id}/add-to-cart")
    public String addToCart(@PathVariable Long id,
                            Model model,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/login";
        }

        Optional<Ads> adOpt = adsService.getAdById(id);

        if (!adOpt.isPresent())
        {
            redirectAttributes.addFlashAttribute("error", "Товар не найден");
            return "redirect:/MainSearchPage";
        }

        Ads ad = adOpt.get();
        User currentUser = userService.getUserByLogin(authentication.getName());

        if (ad.getIdUser().equals(currentUser.getIdUser()))
        {
            redirectAttributes.addFlashAttribute("warning", "Вы не можете добавить в корзину свой собственный товар!");
            return "redirect:/ads/" + id;
        }

        if (ad.getCount() <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "Товара нет в наличии");
            return "redirect:/ads/" + id;
        }

        boolean added = orderService.addToBasket(currentUser.getIdUser(), ad.getIdAds(), ad.getPrice());

        if (!added)
        {
            redirectAttributes.addFlashAttribute("error", "Не удалось добавить товар в корзину");
            return "redirect:/ads/" + id;
        }

        redirectAttributes.addFlashAttribute("success", "Товар добавлен в корзину");
        return "redirect:/BasketPage";
    }

    @PostMapping("/{id}")
    public String deleteAd(@PathVariable Long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUserByLogin(username);

        Optional<Ads> ad = adsService.getAdById(id);
        if (ad.isPresent() && (ad.get().getIdUser().equals(currentUser.getIdUser()) || currentUser.isAdmin()))
        {
            adsService.deleteAds(id);
            return "redirect:/ProfilePage";
        }
        return "redirect:/ads/" + id;
    }

    @GetMapping("/images/{id}/{index}")
    @ResponseBody
    public ResponseEntity<byte[]> getAdImage(@PathVariable Long id, @PathVariable int index)
    {
        Optional<Ads> adOpt = adsService.getAdById(id);
        if (!adOpt.isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        Ads ad = adOpt.get();
        if (index == 0 && ad.getPhotoProducts() != null && ad.getPhotoProducts().length > 0)
        {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(ad.getPhotoProducts());
        }
        return ResponseEntity.notFound().build();
    }
}