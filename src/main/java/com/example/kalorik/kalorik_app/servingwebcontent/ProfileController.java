package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.domain.UserInfo;
import com.example.kalorik.kalorik_app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    MealsService mealsService;
    @Autowired
    MealFoodItemService itemService;
    @Autowired
    FoodService foodService;
    @Autowired
    UserService userService;

    @GetMapping("/profile")
    String getProfile(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        UserInfo ui = userInfoService.getUserInfoByUsr(u);
        model.addAttribute("UI", ui);
        model.addAttribute("editMode", false);
        model.addAttribute("editProfileMode", false);
        return "profile";
    }

    @GetMapping("profile/editPurposes")
    public String showEditProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        UserInfo ui = userInfoService.getUserInfoByUsr(u);
        model.addAttribute("UI", ui);
        model.addAttribute("editMode", true);
        model.addAttribute("editProfileMode", false);
        return "profile";
    }

    // Save the updated profile data
    @PostMapping("profile/save")
    public String savePurposes(@ModelAttribute("UI") UserInfo userInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        UserInfo ui=userInfoService.getUserInfoByUsr(u);
        ui.setDesiredWeight(userInfo.getDesiredWeight());
        ui.setCaloriesnum(userInfo.getCaloriesnum());
        ui.setPurpose(userInfo.getPurpose());
        ui.setWeightKg(userInfo.getWeightKg());

        userInfoService.save(ui);
        return "redirect:/profile";
    }

    @GetMapping("profile/editMainInfo")
    public String showEditInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        UserInfo ui = userInfoService.getUserInfoByUsr(u);

        model.addAttribute("UI", ui);
        model.addAttribute("editProfileMode", true);
        model.addAttribute("editMode", false);
        return "profile";
    }

    @PostMapping("profile/MainInfoSave")
    public String saveProfile(@ModelAttribute("UI") UserInfo userInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        UserInfo ui=userInfoService.getUserInfoByUsr(u);
        ui.setFirstName(userInfo.getFirstName());
        ui.setLastName(userInfo.getLastName());
        ui.setGender(userInfo.getGender());
        ui.setDateOfBirth(userInfo.getDateOfBirth());
        ui.setHeightCm(userInfo.getHeightCm());
        ui.setActivityLevel(userInfo.getActivityLevel());
        userInfoService.save(ui);
        return "redirect:/profile";
    }
}
