package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;

import java.util.Date;


//Таблица приемов пищи
@Entity
public class Meals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Используем IDENTITY для автоинкремента
    private Long id;
    @Temporal(TemporalType.DATE)
    @Column(name="meal_date")
    private Date mealDate;
    @Column(name="meal_title")
    private String mealTitle;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // username больше не может быть null
    private User user;
    public Meals(){}

    public Meals (Date mealDate, String mealTitle, User user)
    {
        this.mealDate=mealDate;
        this.mealTitle=mealTitle;
        this.user=user;
    }

    public Date getMeal_date() {
        return mealDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMealTitle() {
        return mealTitle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMealTitle(String mealTitle) {
        this.mealTitle = mealTitle;
    }

    public Date getMealDate() {
        return mealDate;
    }

    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }
}