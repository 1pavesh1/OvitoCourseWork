package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.repository.AdsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsService
{
    @Autowired
    private AdsRepository adsRepository;

    public List<Ads> getAllActiveAds()
    {
        return adsRepository.findAllByStatusProduct(true);
    }

    public List<Ads> getAdsByUserId(Long idUser) {
        return adsRepository.findByIdUser(idUser);
    }

    public Optional<Ads> getAdById(Long id)
    {
        Optional<Ads> ad = adsRepository.findById(id);
        ad.ifPresent(a -> {
            a.getPhotosList();
        });
        return ad;
    }

    public boolean addAds(Ads ads)
    {
        adsRepository.save(ads);
        return true;
    }

    public List<Ads> searchAds(String searchQuery)
    {
        if (searchQuery == null || searchQuery.trim().isEmpty())
        {
            return getAllActiveAds();
        }
        return adsRepository.findByNameOrDescriptionContaining(searchQuery);
    }

    public List<Ads> filterAds(Long categoryId, String city, String searchQuery)
    {
        if (searchQuery != null && !searchQuery.trim().isEmpty())
        {
            List<Ads> searchResults = searchAds(searchQuery);

            if (categoryId != null && city != null && !city.isEmpty())
            {
                searchResults.removeIf(ad ->
                        !ad.getIdCategory().equals(categoryId) ||
                                !ad.getAddress().toLowerCase().contains(city.toLowerCase()));
            }
            else if (categoryId != null)
            {
                searchResults.removeIf(ad -> !ad.getIdCategory().equals(categoryId));
            }
            else if (city != null && !city.isEmpty())
            {
                searchResults.removeIf(ad -> !ad.getAddress().toLowerCase().contains(city.toLowerCase()));
            }

            return searchResults;
        }

        if (categoryId != null && city != null && !city.isEmpty())
        {
            return adsRepository.findByIdCategoryAndAddressContaining(categoryId, city);
        }
        else if (categoryId != null)
        {
            return adsRepository.findByIdCategory(categoryId);
        }
        else if (city != null && !city.isEmpty())
        {
            return adsRepository.findByAddressContaining(city);
        }
        else
        {
            return getAllActiveAds();
        }
    }

    public boolean updateAds(Ads ads)
    {
        Ads adsFromDb = adsRepository.findAdsByIdAds(ads.getIdAds());
        if (adsFromDb == null)
        {
            return false;
        }

        adsFromDb.setName(ads.getName());
        adsFromDb.setDescription(ads.getDescription());
        adsFromDb.setPrice(ads.getPrice());
        adsFromDb.setCount(ads.getCount());
        adsFromDb.setAddress(ads.getAddress());
        adsFromDb.setIdCategory(ads.getIdCategory());

        if (ads.getPhotoProducts() != null && ads.getPhotoProducts().length > 0)
        {
            adsFromDb.setPhotoProducts(ads.getPhotoProducts());
        }

        adsRepository.save(adsFromDb);
        return true;
    }

    public boolean deleteAds(Long idAds)
    {
        if (adsRepository.existsById(idAds))
        {
            adsRepository.deleteById(idAds);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteAdsByIdUser(Long IdUser)
    {
        List<Ads> userAds = adsRepository.findByIdUser(IdUser);
        for (Ads ad : userAds)
        {
            adsRepository.delete(ad);
        }
    }
}