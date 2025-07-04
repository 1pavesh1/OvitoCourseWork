package com.example.OvitoCourseWork.entity;

import com.example.OvitoCourseWork.repository.RoleRepository;
import com.example.OvitoCourseWork.service.UserService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;                                    // ID пользователя

    @Size(max = 50, message = "Имя должно быть не длиннее 50 символов")
    private String name;                                    // Имя пользователя

    @Size(max = 50, message = "Фамилия должна быть не длиннее 50 символов")
    private String lastName;                                // Фамилия пользователя

    @Size(min = 5, max = 20, message = "Логин должен быть от 5 до 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Логин должен содержать только английские буквы и цифры")
    private String login;                                   // Логин пользователя

    private String hashPassword;                            // Хэшированный пароль пользователя

    @Email(message = "Некорректный формат email")
    private String email;                                   // Почта пользователя

    @Pattern(regexp = "^ул\\.\\s[а-яА-ЯёЁ\\s-]+,\\sг\\.\\s[а-яА-ЯёЁ\\s-]+,\\sд\\.\\s\\d+[а-яА-Я]?,\\sкв\\.\\s\\d+$",
            message = "Адрес должен быть в формате: ул. Название, г. Город, д. Номер, кв. Номер")
    private String address;                                 // Адрес пользователя

    @Pattern(regexp = "^\\+7-\\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}$",
            message = "Телефон должен быть в формате: +7-(999)-999-99-99")
    private String phone;                                   // Телефон пользователя

    @Size(min = 5, max = 20, message = "Пароль должен быть от 5 до 20 символов")
    @Pattern(regexp = ".*[!%?*_].*", message = "Пароль должен содержать хотя бы один из символов: !,%,?,*,_")
    @Transient
    private String simplePassword;                          // Простой пароль пользователя

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    private List<Role> roles;                               // Лист ролей (роль пользователя)

    public String getSimplePassword() {
        return simplePassword;
    }

    public void setSimplePassword(String simplePassword) {
        this.simplePassword = simplePassword;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long id) {
        this.idUser = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Аккаунт активен
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Пароль не просрочен
    }

    @Override
    public boolean isEnabled() {
        return true; // Аккаунт включён
    }

    @Override
    public String getPassword() {
        return hashPassword;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Используем поле roles вместо вызова сервиса
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    public boolean isAdmin() {
        return getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public <E> void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
