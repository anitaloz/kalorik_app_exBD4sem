package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.Body;
import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.repositories.BodyRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class BodyService {
    private final BodyRepo bodyRepo;

    public BodyService (BodyRepo bodyRepo)
    {
        this.bodyRepo=bodyRepo;
    }

    public void save(Body body)
    {
        bodyRepo.save(body);
    }

    public List<Body> findBodiesByUser(User user)
    {
        return bodyRepo.findBodiesByUser(user);
    }

    public List<Body> findBodiesByUserAndDtBetween(User user, Date start, Date end){
        return bodyRepo.findBodiesByUserAndDtBetweenOrderByDt(user, start, end);
    }
    public List<Body> findBodiesByUserAndDt(User user, Date d){
        return bodyRepo.findBodiesByUserAndDt(user, d);
    }
    public List<Body> findBodiesByUserOrderByDt(User u){
        return bodyRepo.findBodiesByUserOrderByDt(u);
    }

    public List<Body> findBodiesByUserAndDtBeforeOrderByDt(User u, Date end){
        return bodyRepo.findBodiesByUserAndDtBeforeOrderByDt(u, end);
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}
