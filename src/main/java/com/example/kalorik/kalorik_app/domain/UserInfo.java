package com.example.kalorik.kalorik_app.domain;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;


    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "activity_level", length = 50)
    private String activityLevel;

    @Column(name = "caloriesnum")
    private Integer caloriesnum;

    @Column(name = "purpose", length = 30)
    private String purpose;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false) // username больше не может быть null
    private User usr;
    @Column(name="desired_weight")
    private BigDecimal desiredWeight;

    @Column(name="image_url", length=255)
    private String imageUrl;

    public Integer calculateAge() {
        if (dateOfBirth == null) {
            return null; // Or throw an exception, or return a default value
        }

        LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }

    @Transient
    private Integer age = calculateAge();
    // Method to calculate age


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getDesiredWeight() {
        return desiredWeight;
    }

    public void setDesiredWeight(BigDecimal desiredWeight) {
        this.desiredWeight = desiredWeight;
    }

    // Конструкторы
    public UserInfo() {
    }

    public UserInfo(String firstName, String lastName, Date dateOfBirth, String gender, Integer heightCm, BigDecimal weightKg, String activityLevel, Integer caloriesnum, String purpose, User usr) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateOfBirth=dateOfBirth;
        this.gender=gender;
        this.heightCm=heightCm;
        this.weightKg=weightKg;
        this.activityLevel=activityLevel;
        this.caloriesnum=caloriesnum;
        this.purpose=purpose;
        this.usr=usr;
        this.age=calculateAge();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

//    @Override
//    public String toString() {
//        return "UserInfo{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", dateOfBirth=" + dateOfBirth +
//                ", gender='" + gender + '\'' +
//                ", heightCm=" + heightCm +
//                ", weightKg=" + weightKg +
//                ", activityLevel='" + activityLevel + '\'' +
//                ", caloriesnum=" + caloriesnum +
//                ", purpose='" + purpose + '\'' +
//                ", usr=" + usr +
//                '}';
//    }

    //Убираем поле username из конструктора, т.к. теперь используем Usr
    /*public UserInfo(String username) {
        this.username = username;
    }*/


    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }


    public Integer getCaloriesnum() {
        return caloriesnum;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setCaloriesnum(Integer caloriesnum) {
        this.caloriesnum = caloriesnum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public User getUsr() {
        return usr;
    }

    public void setUsr(User usr) {
        this.usr = usr;
    }

    // equals и hashCode (важно для коллекций и JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(id, userInfo.id) && Objects.equals(usr, userInfo.usr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usr);
    }

}
