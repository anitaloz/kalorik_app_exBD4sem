package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;


//Таблица приемов пищи
@Entity
public class Meals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Используем IDENTITY для автоинкремента
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date meal_date;
    private String meal_title;
    public Meals(){}

    public Meals (Date meal_date, String meal_title)
    {
        this.meal_date=meal_date;
        this.meal_title=meal_title;
    }

    public Date getMeal_date() {
        return meal_date;
    }

    public String getMeal_title() {
        return meal_title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMeal_date(Date meal_date) {
        this.meal_date = meal_date;
    }

    public void setMeal_title(String meal_title) {
        this.meal_title = meal_title;
    }
}