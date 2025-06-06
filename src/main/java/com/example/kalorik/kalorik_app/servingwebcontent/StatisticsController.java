package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.Body;
import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.services.BodyService;
import com.example.kalorik.kalorik_app.services.UserInfoService;
import com.example.kalorik.kalorik_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

@Controller
public class StatisticsController {
    @Autowired
    BodyService bodyService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserService userService;


    @GetMapping("/statistics")
    public String showBodyChart(
                                @RequestParam(name = "startDate", required = false) LocalDate startDate,
                                @RequestParam(name = "endDate", required = false) LocalDate endDate,
                                Model model) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
        if (userInfoService.getUserInfoByUsr(u)==null){
            return "redirect:/addInfo";
        }
        List<Body> bodyData=bodyService.findBodiesByUserOrderByDt(u);
        if (startDate != null && endDate != null) {
            //Получаем данные за определенный период
            bodyData = bodyService.findBodiesByUserAndDtBetween(u, BodyService.convertLocalDateToDate(startDate), BodyService.convertLocalDateToDate(endDate));
        }
        if (startDate != null && endDate == null) {
            endDate = LocalDate.now();
            bodyData = bodyService.findBodiesByUserAndDtBetween(u, BodyService.convertLocalDateToDate(startDate), BodyService.convertLocalDateToDate(endDate));
        }
        if (startDate == null && endDate != null) {
            bodyData=bodyService.findBodiesByUserAndDtBeforeOrderByDt(u, BodyService.convertLocalDateToDate(endDate));
        }

        // Преобразуем данные для Chart.js
        List<String> labels = bodyData.stream().map(body -> body.getDt().toString()).collect(Collectors.toList());
        List<Double> weightData = bodyData.stream().map(body -> body.getWeight().doubleValue()).collect(Collectors.toList());
        List<Integer> heightData = bodyData.stream().map(Body::getHeight).collect(Collectors.toList());
        List<Double> imt=new ArrayList<>();
        for(int i=0; i<weightData.size(); i++)
        {
            double h=heightData.get(i).doubleValue()/100;
            double val=weightData.get(i)/pow(h,2);
            imt.add(val);
        }
        for(int i=0; i<imt.size(); i++)
        {
            System.out.println(imt.get(i));
        }
        model.addAttribute("labels", labels);
        model.addAttribute("weightData", weightData);
        model.addAttribute("imtData", imt);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        String avatar=userInfoService.getUserInfoByUsr(u).getImageUrl();
        model.addAttribute("avatar", avatar);
        return "statistics";
    }
}
