package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;


//Таблица с продуктами
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Используем IDENTITY для автоинкремента
    private Long id;
    private String name; //название
    private Double calories; //калории на 100 грамм
    private Double bel; //белки на 100 грамм
    private Double fats;//жиры
    private Double ch; //углеводы

    @Column(name = "serving_size") // Явно указываем имя столбца
    private Double servingSize;//вес/размер порции

    @ManyToOne
    @JoinColumn(name = "serving_unit", referencedColumnName = "id", nullable = false)
    private ServingUnits servingUnit;//единица измерения порции

    public Food(){

    }
    public Food(String name, Double calories, Double bel, Double fats, Double ch, Double servingSize, ServingUnits servingUnit)
    {
        this.name=name;
        this.calories=calories;
        this.bel=bel;
        this.ch=ch;
        this.fats=fats;
        this.servingSize=servingSize;
        this.servingUnit=servingUnit;
    }
    public Double getBel() {
        return bel;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getCh() {
        return ch;
    }

    public Double getFats() {
        return fats;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setBel(Double bel) {
        this.bel = bel;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public void setCh(Double ch) {
        this.ch = ch;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getServingSize() { // Исправлено имя геттера
        return servingSize;
    }

    public ServingUnits getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(ServingUnits servingUnit) {
        this.servingUnit = servingUnit;
    }

    public void setServingSize(Double servingSize) {
        this.servingSize = servingSize;
    }
}
