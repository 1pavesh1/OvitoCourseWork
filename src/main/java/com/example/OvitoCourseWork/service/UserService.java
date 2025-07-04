package com.example.OvitoCourseWork.service;

import com.example.OvitoCourseWork.entity.Ads;
import com.example.OvitoCourseWork.entity.Basket;
import com.example.OvitoCourseWork.entity.Role;
import com.example.OvitoCourseWork.entity.User;
import com.example.OvitoCourseWork.repository.BasketRepository;
import com.example.OvitoCourseWork.repository.RoleRepository;
import com.example.OvitoCourseWork.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService
{
    @Autowired
    BasketService basketService;
    @Autowired
    AdsService adsService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByLogin(username);

        if (user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean findUserByLogin(String login)
    {
        User userFromDB = userRepository.findByLogin(login);
        if (userFromDB != null)
        {
            return true;
        }
        return false;
    }

    public User getUserByLogin(String login)
    {
        User userFromDB = userRepository.findByLogin(login);
        if (userFromDB != null)
        {
            return userFromDB;
        }
        return null;
    }

    public boolean saveUser(User user)
    {
        User userFromDB = userRepository.findByLogin(user.getLogin());

        if (userFromDB != null)
        {
            System.out.println("Пользователь с логином " + user.getLogin() + " уже существует");
            return false;
        }

        user.setHashPassword(bCryptPasswordEncoder.encode(user.getSimplePassword()));
        User savedUser = userRepository.save(user);

        String roleName = user.getLogin().toLowerCase().contains("admin") ?
                Role.ROLE_ADMIN : Role.ROLE_USER;

        Role role = new Role();
        role.setIdUser(savedUser.getIdUser());
        role.setName(roleName);
        roleRepository.save(role);

        savedUser.setRoles(List.of(role));

        basketService.createBasketForUser(savedUser);
        System.out.println("Пользователь сохранён: " + user.getLogin() + " с ролью " + roleName);
        return true;
    }

    public List<Role> getUserRoles(Long idUser)
    {
        return roleRepository.findByIdUser(idUser);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User getUserByIdUser(Long idUser)
    {
        return userRepository.findByIdUser(idUser).orElse(null);
    }

    public boolean isProfileComplete(User user)
    {
        return user != null &&
                user.getName() != null && !user.getName().isEmpty() &&
                user.getLastName() != null && !user.getLastName().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty() &&
                user.getPhone() != null && !user.getPhone().isEmpty() &&
                user.getAddress() != null && !user.getAddress().isEmpty();
    }

    public boolean updateUser(User user)
    {
        User userFromDb = userRepository.findByLogin(user.getLogin());
        if (userFromDb == null)
        {
            return false;
        }

        userFromDb.setName(user.getName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setPhone(user.getPhone());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setAddress(user.getAddress());

        userRepository.save(userFromDb);
        return true;
    }

    public boolean logUser(User user)
    {
        User userFromDB = userRepository.findByLogin(user.getLogin());

        if (userFromDB == null)
        {
            return false;
        }

        return bCryptPasswordEncoder.matches(user.getSimplePassword(), userFromDB.getHashPassword());
    }

    @Transactional
    public boolean deleteUser(Long userId)
    {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent())
        {
            User user = userOptional.get();

            List<Ads> userAds = adsService.getAdsByUserId(user.getIdUser());
            for (Ads ad : userAds)
            {
                adsService.deleteAds(ad.getIdAds());
            }

            Basket userBasket = basketService.getBasketByUserId(user.getIdUser());
            if (userBasket != null)
            {
                basketRepository.delete(userBasket);
            }

            roleRepository.deleteByIdUser(user.getIdUser());

            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
