package com.example.kalorik.kalorik_app.domain;

import com.example.kalorik.kalorik_app.domain.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "body")
public class Body {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_body"))
    private User user;

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "dt")
    @Temporal(TemporalType.DATE)
    private Date dt;

    public Body() {
        // Обязательный конструктор без аргументов для JPA
    }

    // Геттеры и сеттеры для всех полей
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        return "Body{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", weight=" + weight +
                ", height=" + height +
                ", dt=" + dt +
                '}';
    }
}
