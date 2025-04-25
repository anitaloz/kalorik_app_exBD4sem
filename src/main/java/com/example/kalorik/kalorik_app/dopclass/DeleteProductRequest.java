package com.example.kalorik.kalorik_app.dopclass;

import java.util.Date;

public class DeleteProductRequest {
    private Long productId;
    private String mealTitle;
    private Date mealDate;

    public DeleteProductRequest(){}


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMealTitle() {
        return mealTitle;
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