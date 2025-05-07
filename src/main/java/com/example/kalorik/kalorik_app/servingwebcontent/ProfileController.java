package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.Body;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

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
    @Autowired
    BodyService bodyService;

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
    public String savePurposes(@ModelAttribute("UI") UserInfo userInfo, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        UserInfo ui=userInfoService.getUserInfoByUsr(u);

        String weightKgError = userInfoService.validateWeightKg(userInfo.getWeightKg());
        String caloriesNumError = userInfoService.validateCaloriesNum(userInfo.getCaloriesnum());
        String purposeError = userInfoService.validatePurpose(userInfo.getPurpose());
        String desiredWeightError=userInfoService.validateDesiredWeight(userInfo.getDesiredWeight());

        if (weightKgError != null ||
                desiredWeightError != null || caloriesNumError != null || purposeError != null) {

            redirectAttributes.addFlashAttribute("weightKgError", weightKgError);
            redirectAttributes.addFlashAttribute("desiredWeightError", desiredWeightError);
            redirectAttributes.addFlashAttribute("caloriesNumError", caloriesNumError);
            redirectAttributes.addFlashAttribute("purposeError", purposeError);

            // Сохраняем значения полей, чтобы вернуть их в форму
            redirectAttributes.addFlashAttribute("UI", userInfo);

            return "redirect:/profile/editPurposes"; // Возвращаемся на форму
        }

        if(!userInfo.getWeightKg().equals(ui.getWeightKg()))
        {
            Body b=new Body();
            Date d=new Date();
            b.setDt(d);
            b.setWeight(userInfo.getWeightKg());
            b.setHeight(userInfoService.getUserInfoByUsr(u).getHeightCm());
            b.setUser(u);
            bodyService.save(b);
        }
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
    public String saveProfile(@RequestParam("coverImage") MultipartFile file, @ModelAttribute("UI") UserInfo userInfo, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());

        String firstNameError = userInfoService.validateFirstName(userInfo.getFirstName());
        String lastNameError = userInfoService.validateLastName(userInfo.getLastName());
        //String dateOfBirthError = validateDateOfBirth(dateOfBirth);
        String genderError = userInfoService.validateGender(userInfo.getGender());
        String heightCmError = userInfoService.validateHeightCm(userInfo.getHeightCm());
        String activityLevelError = userInfoService.validateActivityLevel(userInfo.getActivityLevel());

        if (firstNameError != null || lastNameError != null  ||
                genderError != null || heightCmError != null ||
                activityLevelError != null) {

            redirectAttributes.addFlashAttribute("firstNameError", firstNameError);
            redirectAttributes.addFlashAttribute("lastNameError", lastNameError);
            //redirectAttributes.addFlashAttribute("dateOfBirthError", dateOfBirthError);
            redirectAttributes.addFlashAttribute("genderError", genderError);
            redirectAttributes.addFlashAttribute("heightCmError", heightCmError);
            redirectAttributes.addFlashAttribute("activityLevelError", activityLevelError);

            // Сохраняем значения полей, чтобы вернуть их в форму
            redirectAttributes.addFlashAttribute("UI", userInfo);

            return "redirect:/profile/editMainInfo"; // Возвращаемся на форму
        }


        UserInfo ui=userInfoService.getUserInfoByUsr(u);
        ui.setFirstName(userInfo.getFirstName());
        ui.setLastName(userInfo.getLastName());
        ui.setGender(userInfo.getGender());
        ui.setDateOfBirth(userInfo.getDateOfBirth());
        ui.setHeightCm(userInfo.getHeightCm());
        ui.setActivityLevel(userInfo.getActivityLevel());

        if(!ui.getImageUrl().equals("/images/defaultprofile.png")) {
            userInfoService.deleteOldCoverImage(ui);
        }
        if (!file.isEmpty()) {
            try {
                userInfoService.saveCoverImage(ui, file);
            } catch (Exception e) {
                return "error";
            }
        }
        userInfoService.save(ui);
        return "redirect:/profile";
    }
}
