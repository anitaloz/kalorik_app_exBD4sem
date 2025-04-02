package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.Food;
import com.example.kalorik.kalorik_app.domain.Meal_food_items;
import com.example.kalorik.kalorik_app.domain.Meals;
import com.example.kalorik.kalorik_app.repositories.FoodRepo;
import com.example.kalorik.kalorik_app.repositories.MealProductInfo;
import com.example.kalorik.kalorik_app.repositories.Meal_food_itemsRepo;
import com.example.kalorik.kalorik_app.repositories.MealsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Изменили импорт
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private FoodRepo foodRepo;

    @Autowired
    private MealsRepo mealsRepo;

    @Autowired
    private Meal_food_itemsRepo foodItemRepo;

    @GetMapping("/")
    public String caloriePage(Model model) {
        Random random = new Random();
        int randomCalories = random.nextInt(501) + 1200; // 501 = 1700 - 1200 + 1
        Double c=foodRepo.sumValueByNameNative("calories");
        model.addAttribute("calorieCount", randomCalories);
        model.addAttribute("currentCalories", c);

        return "main"; // Имя вашего Mustache-шаблона
    }

    @GetMapping("/getProducts") // Измененный URL
    public String getProducts(Model model, @RequestParam(name = "filter", required = false) String filter) {
        Iterable<Food> products;
        if (filter != null && !filter.isEmpty()) {
            products = foodRepo.findByNameContainingIgnoreCase(filter);
        } else {
            products = foodRepo.findAll();
        }
        model.addAttribute("products", products);
        return "getProducts";  // Возвращаем только список продуктов
    }

    @GetMapping("/getMealProducts")
    public ResponseEntity<Map<String, List<MealProductInfo>>> getMealProducts(
            @RequestParam("mealDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mealDate,
            @RequestParam("mealTitle") String mealTitle) {

        List<MealProductInfo> mealProducts = foodRepo.findByMealDateAndMealTitle(mealTitle, mealDate);

        // Создаем карту (Map) для JSON ответа
        Map<String, List<MealProductInfo>> response = new HashMap<>();
        response.put("mealProducts", mealProducts);
        System.out.println();
        return ResponseEntity.ok(response); // Возвращаем JSON
    }


    @PostMapping("/add")
    public RedirectView add(
            @RequestParam String name,
            @RequestParam Float calories, // Float
            @RequestParam Float bel, // Float
            @RequestParam Float fats, // Float
            @RequestParam Float ch, // Float
            @RequestParam Float servingSize, // Float и servingSize
            @RequestParam String servingUnit,
            Model model) { // Изменили тип Model

        Food f = new Food(name, calories, bel, fats, ch, servingSize, servingUnit);
        foodRepo.save(f);
        return new RedirectView("/");
    }



    //@RequestParam выдергивает из с запросов либо из формы если передаем постом либо с url она выдергивает значеня
    @PostMapping("/addProductToMeal")
    public ResponseEntity<?> addProductToMeal(@RequestBody AddProductRequest request, Model model) {
        try {
            // 1. Получаем продукт из базы данных по ID
            Food food = foodRepo.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + request.getProductId() + " не найден"));

            // 2. Создаем новую сущность Meal
            Meals meal = new Meals();
            meal.setMeal_date(new Date());
            meal.setMeal_title(request.getMealTitle());

            Meal_food_items.MealFoodItemId mealFoodItemId = new Meal_food_items.MealFoodItemId(meal.getId(), request.getProductId());

            // 5. Создаем Meal_food_items и устанавливаем значения
            Meal_food_items item = new Meal_food_items();
            item.setId(mealFoodItemId); // Устанавливаем составной ключ!
            item.setMeal(meal);
            item.setFood(food);
            item.setUnit(request.getUnit());
            item.setQuantity(request.getQuantity());

            // 3. Сохраняем сущность Meal в базу данных
            mealsRepo.save(meal);
            foodItemRepo.save(item);
            String s="Продукт добавлен";
            model.addAttribute("status", s);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка добавления продукта");
        }
    }

    static class AddProductRequest {
        private Long productId;
        private String mealTitle;
        private Float quantity;
        private String unit;
        private String productSize;

        // Геттеры и сеттеры
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getMealTitle() { return mealTitle; }
        public void setMealTitle(String mealType) { this.mealTitle = mealType; }
        public Float getQuantity() { return quantity; }
        public void setQuantity(Float quantity) { this.quantity = quantity; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getProductSize() { return productSize; }
        public void setProductSize(String productSize) { this.productSize = productSize; }
    }


}

//    @PostMapping("/")  // Изменили mapping на "/filter"
//    public String filter(@RequestParam String filter, Model model) { // Изменили тип Model
//        Iterable<Food> products;
//        if (filter != null && !filter.isEmpty()) {
//            products = foodRepo.findByName(filter);
//        } else {
//            products = foodRepo.findAll();
//        }
//        model.addAttribute("products", products); // Используем addAttribute
//        return "main";
//    }


