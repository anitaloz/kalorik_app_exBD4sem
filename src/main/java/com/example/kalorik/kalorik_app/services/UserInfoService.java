package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.domain.UserInfo;
import com.example.kalorik.kalorik_app.repositories.UserInfoRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserInfoService {
    private final UserInfoRepo userInfoRepo;

    public UserInfoService(UserInfoRepo userInfoRepo)
    {
        this.userInfoRepo=userInfoRepo;
    }

    public UserInfo getUserInfoByUsr(User usr){
        return userInfoRepo.findUserInfoByUsr(usr);
    }

    public void save(UserInfo userInfo)
    {
        userInfoRepo.save(userInfo);
    }

    public String validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return "Имя не может быть пустым.";
        } else if (firstName.length() > 50) {
            return "Имя не может быть длиннее 50 символов.";
        } else if (!firstName.matches("^[a-zA-Zа-яА-Я\\s]+$")) { // Added whitespace
            return "Имя должно содержать только буквы и пробелы.";
        }
        return null;
    }

    public String validateLastName(String lastName) {
        if (lastName != null && lastName.length() > 50) { // Фамилия не обязательна
            return "Фамилия не может быть длиннее 50 символов.";
        }
        else if (lastName != null && !lastName.trim().isEmpty() && !lastName.matches("^[a-zA-Zа-яА-Я\\s]+$")) {
            return "Фамилия должна содержать только буквы и пробелы.";
        }
        return null;
    }

//    public String validateDateOfBirth(Date dateOfBirth) {
//        if (dateOfBirth == null) { // Дата рождения не обязательна, но если указана, то проверяем формат.
//            try {
//                Date d=new Date();
//                dateOfBirth.after(d);
//            } catch (Exception e) {
//                return "Неверный формат даты рождения (YYYY-MM-DD).";
//        }
//        return null;
//    }

    public String validateGender(String gender) {
        if (gender != null && gender.length() > 10) {
            return "Пол не может быть длиннее 10 символов.";
        }
        return null;
    }

    public String validateHeightCm(Integer height) {
        if (height != null) {
            try {
                if (height <= 0 || height > 250) {
                    return "Рост должен быть между 1 и 250 см.";
                }
            } catch (NumberFormatException e) {
                return "Рост должен быть числом.";
            }
        }
        else {
            return "Введите рост";
        }
        return null;
    }

    public String validateWeightKg(BigDecimal weight) {
        if (weight != null) {
            try {
                if (weight.compareTo(BigDecimal.ZERO) <= 0 || weight.compareTo(new BigDecimal("300.00")) > 0) {
                    return "Вес должен быть между 0.01 и 300 кг.";
                }
            } catch (NumberFormatException e) {
                return "Вес должен быть числом.";
            }
        }
        else {
            return "Введите вес";
        }
        return null;
    }

    public String validateActivityLevel(String activityLevel) {
        if (activityLevel != null && activityLevel.length() > 50) {
            return "Образ жизни не может быть длиннее 50 символов.";
        }
        return null;
    }
    public String validateDesiredWeight(BigDecimal desiredWeight) {
        if (desiredWeight!= null && (desiredWeight.compareTo(BigDecimal.ZERO) <= 0 ||desiredWeight.compareTo(new BigDecimal("300.00")) > 0)) {
            return "Желаемый вес должен быть между 0.01 и 300кг";
        }
        return null;
    }

    public String validateCaloriesNum(Integer caloriesNum) {
        if(caloriesNum==null || caloriesNum==0)
            return "Выберите количество калорий для потребления";
        if(caloriesNum<=700)
            return "Количество калорий должно быть больше 700";
        return null; // пока нет валидации
    }

    public String validatePurpose(String purpose) {
        if (purpose != null && purpose.length() > 30) {
            return "Цель не может быть длиннее 30 символов.";
        }
        else if (purpose!=null && !purpose.matches("^[a-zA-Zа-яА-Я\\s]+$")) { // Added whitespace
            return "Имя должно содержать только буквы и пробелы.";
        }
        return null;
    }

    public void deleteOldCoverImage(UserInfo userInfo) {
        if(userInfo.getImageUrl()!=null && !userInfo.getImageUrl().isEmpty() && !userInfo.getImageUrl().equals("/images/defaultprofile.png")) {
            String oldCoverImage = userInfo.getImageUrl().substring(7);
            if (!oldCoverImage.isEmpty()) {
                Path oldFilePath = Paths.get(uploadPath, oldCoverImage);
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    // Логируем ошибку, но не прерываем процесс
                    System.err.println("Failed to delete old cover image: " + e.getMessage());
                }
            }
        }
    }

    @Value("${upload.directory}") // Читаем путь из application.properties
    private String uploadPath;
    public void saveCoverImage(UserInfo userInfo, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // 1. Генерируем уникальное имя файла
            deleteOldCoverImage(userInfo);
            String uuidFile = UUID.randomUUID().toString();
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = uuidFile + fileExtension;

            // 2. Создаем путь для сохранения файла
            Path filePath = Paths.get(uploadPath, fileName);

            // 3. Сохраняем файл
            try {
                Files.write(filePath, file.getBytes());
            } catch (IOException e) {
                throw new IOException("Failed to save cover image: " + e.getMessage());
            }
            fileName="/images/"+fileName;
            userInfo.setImageUrl(fileName);
        }
    }
    
}
