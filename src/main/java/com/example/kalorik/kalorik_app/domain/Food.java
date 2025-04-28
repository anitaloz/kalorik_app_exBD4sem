package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;


//Таблица с продуктами
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Используем IDENTITY для автоинкремента
    private Long id;
    private String name; //название
    private Float calories; //калории на 100 грамм
    private Float bel; //белки на 100 грамм
    private Float fats;//жиры
    private Float ch; //углеводы

    @Column(name = "serving_size") // Явно указываем имя столбца
    private Float servingSize;//вес/размер порции

    @ManyToOne
    @JoinColumn(name = "serving_unit", referencedColumnName = "id", nullable = false)
    private ServingUnits servingUnit;//единица измерения порции

    public Food(){

    }
    public Food(String name, Float calories, Float bel, Float fats, Float ch, Float servingSize, ServingUnits servingUnit)
    {
        this.name=name;
        this.calories=calories;
        this.bel=bel;
        this.ch=ch;
        this.fats=fats;
        this.servingSize=servingSize;
        this.servingUnit=servingUnit;
    }
    public Float getBel() {
        return bel;
    }

    public Float getCalories() {
        return calories;
    }

    public Float getCh() {
        return ch;
    }

    public Float getFats() {
        return fats;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setBel(Float bel) {
        this.bel = bel;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public void setCh(Float ch) {
        this.ch = ch;
    }

    public void setFats(Float fats) {
        this.fats = fats;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getServingSize() { // Исправлено имя геттера
        return servingSize;
    }

    public ServingUnits getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(ServingUnits servingUnit) {
        this.servingUnit = servingUnit;
    }

    public void setServingSize(Float servingSize) {
        this.servingSize = servingSize;
    }
}
