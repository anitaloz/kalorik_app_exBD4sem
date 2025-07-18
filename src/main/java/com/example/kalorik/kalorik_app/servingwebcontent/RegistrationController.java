package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.Role;
import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; // Инъекция PasswordEncoder

    @GetMapping("/registration")
    public String registration() {
        return "registration.html";
    }

    public static boolean isValidString(String str) {
        if (str == null || str.isEmpty()) {
            return false; // Или true, в зависимости от ваших требований
        }
        String regex = "^[a-zA-Z._0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "Такой пользователь уже зарегистрирован");
            return "registration.html";
        }
        if(user.getUsername()==null) {
            model.addAttribute("message", "Поле 'username' обязательно");
            return "registration.html";
        }
        if(!isValidString(user.getUsername())) {
            model.addAttribute("message", "Поле 'username' может содержать только латинские буквы, цифры и '_', '.'");
            return "registration.html";
        }

        if(user.getPassword()==null)
        {
            model.addAttribute("message", "Введите пароль");
            return "registration.html";
        }
        if(user.getPassword().length()<5)
        {
            model.addAttribute("message", "Длина пароля 5 символов и выше");
            return "registration.html";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }




}
