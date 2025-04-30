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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {
    @Autowired
    BodyService bodyService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserService userService;

//    @GetMapping("/statistics")
//    String getStat(Model model)
//    {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User u=userService.findByUsername(auth.getName());
//        List<Body> lb=bodyService.findBodiesByUser(u);
//        return "statistics";
//    }

    @GetMapping("/statistics")
    public String showBodyChart(
                                @RequestParam(name = "startDate", required = false) LocalDate startDate,
                                @RequestParam(name = "endDate", required = false) LocalDate endDate,
                                Model model) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userService.findByUsername(auth.getName());
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

        model.addAttribute("labels", labels);
        model.addAttribute("weightData", weightData);
        model.addAttribute("heightData", heightData);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "statistics";
    }
}
