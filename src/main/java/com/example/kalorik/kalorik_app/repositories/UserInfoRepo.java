package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {
    UserInfo findUserInfoByUsr(User usr);
}
