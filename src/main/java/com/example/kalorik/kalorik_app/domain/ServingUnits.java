package com.example.kalorik.kalorik_app.domain; // Replace with your actual package

import jakarta.persistence.*; // Using Jakarta Persistence API (JPA)

@Entity
@Table(name = "serving_units")
public class ServingUnits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    // Constructors (at least a default one is good practice)
    public ServingUnits() {
    }

    public ServingUnits(String name) {
        this.name = name;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServingUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
