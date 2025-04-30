package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.Body;
import com.example.kalorik.kalorik_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BodyRepo extends JpaRepository<Body, Long> {
    List<Body> findBodiesByUser(User user);
    List<Body> findBodiesByUserAndDtBetweenOrderByDt(User user, Date start, Date end);
    List<Body> findBodiesByUserAndDt(User user, Date d);
    List<Body> findBodiesByUserOrderByDt(User u);
    List<Body> findBodiesByUserAndDtBeforeOrderByDt(User u, Date end);
}
