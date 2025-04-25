package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.*;
import com.example.kalorik.kalorik_app.dopclass.DeleteProductRequest;
import com.example.kalorik.kalorik_app.dopclass.MealProductInfo;
import com.example.kalorik.kalorik_app.repositories.*;
import com.example.kalorik.kalorik_app.services.FoodService;
import com.example.kalorik.kalorik_app.services.MealFoodItemService;
import com.example.kalorik.kalorik_app.services.MealsService;
import com.example.kalorik.kalorik_app.services.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Изменили импорт
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
//import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    MealsService mealsService;
    @Autowired
    MealFoodItemService itemService;
    @Autowired
    FoodService foodService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false); // Не создаем новую сессию, если ее нет

        if (request.getParameter("error") != null) {
            model.addAttribute("error", true); // Передаем флаг error в шаблон
        }

        if (session != null && session.getAttribute("loginError") != null) {
            model.addAttribute("loginError", session.getAttribute("loginError")); // Передаем сообщение об ошибке
            session.removeAttribute("loginError"); // Очищаем сессию, чтобы сообщение не отображалось постоянно
        }

        return "login.html";
    }

    @GetMapping("/")
    public String Log(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/main";
        }
        return "log.html";
    }


    @GetMapping("/main")
    String getMain(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userRepo.findByUsername(auth.getName());
        if (userInfoService.getUserInfoByUsr(u)==null){
            return "redirect:/addInfo";
        }
        double sumCalories=0.0;
        UserInfo ui=userInfoService.getUserInfoByUsr(u);
        Date d=new Date();
        List<MealFoodItems> breakfastItems=new ArrayList<MealFoodItems>();
        List<Meals> breakfast=mealsService.findMealsByMealDateAndMealTitleAndUser(d, "breakfast", u);
        for(Meals m:breakfast)
        {
            List<MealFoodItems> br=itemService.findMeal_food_itemsByMeal(m);
            breakfastItems.addAll(br);
        }
        List<MealFoodItems> lunchItems=new ArrayList<MealFoodItems>();
        List<Meals> lunch=mealsService.findMealsByMealDateAndMealTitleAndUser(d, "lunch", u);
        for(Meals m:lunch)
        {
            List<MealFoodItems> br=itemService.findMeal_food_itemsByMeal(m);
            lunchItems.addAll(br);
        }
        List<MealFoodItems> dinnerItems=new ArrayList<MealFoodItems>();
        List<Meals> dinner=mealsService.findMealsByMealDateAndMealTitleAndUser(d, "dinner", u);
        for(Meals m:dinner)
        {
            List<MealFoodItems> br=itemService.findMeal_food_itemsByMeal(m);
            dinnerItems.addAll(br);
        }
        double sumbel=0.0;
        double sumch=0.0;
        double sumfats=0.0;
        if(!breakfastItems.isEmpty()) {
            for (MealFoodItems m : breakfastItems) {
                sumCalories += m.getFood().getCalories() * m.getQuantity() / m.getFood().getServingSize();
                sumbel+=m.getFood().getBel()*m.getQuantity()/m.getFood().getServingSize();
                sumfats+=m.getFood().getFats()*m.getQuantity()/m.getFood().getServingSize();
                sumch+=m.getFood().getCh()*m.getQuantity()/m.getFood().getServingSize();
            }
        }
        if(!lunchItems.isEmpty()) {
            for (MealFoodItems m : lunchItems) {
                sumCalories += m.getFood().getCalories() * m.getQuantity() / m.getFood().getServingSize();
                sumbel+=m.getFood().getBel()*m.getQuantity()/m.getFood().getServingSize();
                sumfats+=m.getFood().getFats()*m.getQuantity()/m.getFood().getServingSize();
                sumch+=m.getFood().getCh()*m.getQuantity()/m.getFood().getServingSize();
            }
        }
        if(!dinnerItems.isEmpty()) {
            for (MealFoodItems m : dinnerItems) {
                sumCalories += m.getFood().getCalories() * m.getQuantity() / m.getFood().getServingSize();
                sumbel+=m.getFood().getBel()*m.getQuantity()/m.getFood().getServingSize();
                sumfats+=m.getFood().getFats()*m.getQuantity()/m.getFood().getServingSize();
                sumch+=m.getFood().getCh()*m.getQuantity()/m.getFood().getServingSize();
            }
        }
        double belNum=ui.getWeightKg().doubleValue()*1.5;
        double chNum=ui.getWeightKg().doubleValue()*2;
        double fatsNum=ui.getWeightKg().doubleValue()*0.8;
        model.addAttribute("caloriesNum", ui.getCaloriesnum());
        model.addAttribute("breakfastItems", breakfastItems);
        model.addAttribute("lunchItems", lunchItems);
        model.addAttribute("dinnerItems", dinnerItems);
        model.addAttribute("sumCalories", sumCalories);
        model.addAttribute("sumFats", sumfats);
        model.addAttribute("sumBel", sumbel);
        model.addAttribute("sumCh", sumch);
        model.addAttribute("chNum", chNum);
        model.addAttribute("belNum", belNum);
        model.addAttribute("fatsNum", fatsNum);
        return "main.html";
    }


    @GetMapping("/addInfo")
    String getAddInfo(Model model)
    {
        return "addInfo.html";
    }

    @PostMapping("/addInfo")
    String postAddInfo(@RequestParam String firstName,
                       @RequestParam String lastName,
                       @RequestParam(value = "dateOfBirth", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateOfBirth,
                       @RequestParam (defaultValue = "")String gender,
                       @RequestParam (defaultValue = "0") Integer heightCm,
                       @RequestParam (defaultValue = "0") BigDecimal weightKg,
                       @RequestParam String activityLevel,
                       @RequestParam (defaultValue = "0") Integer caloriesNum,
                       @RequestParam String purpose,
                       RedirectAttributes redirectAttributes){
        String firstNameError = userInfoService.validateFirstName(firstName);
        String lastNameError = userInfoService.validateLastName(lastName);
        //String dateOfBirthError = validateDateOfBirth(dateOfBirth);
        String genderError = userInfoService.validateGender(gender);
        String heightCmError = userInfoService.validateHeightCm(heightCm);
        String weightKgError = userInfoService.validateWeightKg(weightKg);
        String activityLevelError = userInfoService.validateActivityLevel(activityLevel);
        String caloriesNumError = userInfoService.validateCaloriesNum(caloriesNum);
        String purposeError = userInfoService.validatePurpose(purpose);


        if (firstNameError != null || lastNameError != null  ||
                genderError != null || heightCmError != null || weightKgError != null ||
                activityLevelError != null || caloriesNumError != null || purposeError != null) {

            redirectAttributes.addFlashAttribute("firstNameError", firstNameError);
            redirectAttributes.addFlashAttribute("lastNameError", lastNameError);
            //redirectAttributes.addFlashAttribute("dateOfBirthError", dateOfBirthError);
            redirectAttributes.addFlashAttribute("genderError", genderError);
            redirectAttributes.addFlashAttribute("heightCmError", heightCmError);
            redirectAttributes.addFlashAttribute("weightKgError", weightKgError);
            redirectAttributes.addFlashAttribute("activityLevelError", activityLevelError);
            redirectAttributes.addFlashAttribute("caloriesNumError", caloriesNumError);
            redirectAttributes.addFlashAttribute("purposeError", purposeError);

            // Сохраняем значения полей, чтобы вернуть их в форму
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("dateOfBirth", dateOfBirth);
            redirectAttributes.addFlashAttribute("gender", gender);
            redirectAttributes.addFlashAttribute("heightCm", heightCm);
            redirectAttributes.addFlashAttribute("weightKg", weightKg);
            redirectAttributes.addFlashAttribute("activityLevel", activityLevel);
            redirectAttributes.addFlashAttribute("caloriesNum", caloriesNum);
            redirectAttributes.addFlashAttribute("purpose", purpose);

            return "redirect:/addInfo"; // Возвращаемся на форму
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User usr= userRepo.findByUsername(auth.getName());
        UserInfo ui=new UserInfo(firstName, lastName, dateOfBirth, gender, heightCm, weightKg, activityLevel, caloriesNum, purpose, usr);
        userInfoService.save(ui);
        return "redirect:/main";
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userRepo.findByUsername(auth.getName());
        List<MealProductInfo> mealProducts = foodService.findByMealDateAndMealTitle(mealTitle,mealDate, u.getId());
        for(MealProductInfo m: mealProducts)
        {
            System.out.println(m.getId());
        }
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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User u=userRepo.findByUsername(auth.getName());
            // 1. Получаем продукт из базы данных по ID
            Food food = foodRepo.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + request.getProductId() + " не найден"));

            // 2. Создаем новую сущность Meal
            Meals meal = new Meals();
            meal.setMealDate(new Date());
            meal.setMealTitle(request.getMealTitle());
            meal.setUser(u);
            if(!(mealsService.findMealsByMealDateAndMealTitleAndUser(meal.getMealDate(), meal.getMealTitle(),meal.getUser()).isEmpty()))
            {
                meal=mealsService.findMealsByMealDateAndMealTitleAndUser(meal.getMealDate(), meal.getMealTitle(),meal.getUser()).getFirst();
            }
            else {
                mealsRepo.save(meal);
            }
            MealFoodItems.MealFoodItemId mealFoodItemId = new MealFoodItems.MealFoodItemId(meal.getId(), request.getProductId());
            List<MealFoodItems> items=itemService.findMeal_food_itemsByMeal(meal);
            if(!items.isEmpty())
            {
                for(MealFoodItems i:items)
                {
                    if(i.getFood().equals(food))
                    {
                        i.setQuantity(i.getQuantity()+request.getQuantity());
                        itemService.save(i);
                    }
                }
            }
            else {
                // 5. Создаем Meal_food_items и устанавливаем значения
                MealFoodItems item = new MealFoodItems();
                item.setId(mealFoodItemId); // Устанавливаем составной ключ!
                item.setMeal(meal);
                item.setFood(food);
                item.setUnit(request.getUnit());
                item.setQuantity(request.getQuantity());
                foodItemRepo.save(item);
            }
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

    @PostMapping("/deleteProductFromMeal")
    public ResponseEntity<?> deleteProductFromMeal(@RequestBody DeleteProductRequest request) {
        try {
            //  Извлечение mealTitle и productId из request
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User u=userRepo.findByUsername(auth.getName());
            String mealTitle = request.getMealTitle();
            Long productId = request.getProductId();
            Date mealDate = request.getMealDate();
            Meals meal=mealsService.findMealsByMealDateAndMealTitleAndUser(mealDate, mealTitle, u).getFirst();
            Food food=foodService.findFoodById(productId);
            // Логика удаления продукта из приема пищи
            itemService.deleteMealFoodItemsByMealAndFood(meal, food);
            return ResponseEntity.ok().build(); // Успешное удаление

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка удаления продукта");
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


