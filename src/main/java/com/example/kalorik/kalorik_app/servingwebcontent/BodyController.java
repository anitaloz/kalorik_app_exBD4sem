package com.example.kalorik.kalorik_app.servingwebcontent;


import com.example.kalorik.kalorik_app.services.BodyService;
import com.example.kalorik.kalorik_app.services.UserInfoService;
import com.example.kalorik.kalorik_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BodyController {
    @Autowired
    BodyService bodyService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserService userService;
}
