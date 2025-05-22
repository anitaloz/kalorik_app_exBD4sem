package com.example.kalorik.kalorik_app.servingwebcontent;

import com.example.kalorik.kalorik_app.domain.*;
import com.example.kalorik.kalorik_app.dopclass.DeleteProductRequest;
import com.example.kalorik.kalorik_app.dopclass.MealProductInfo;
import com.example.kalorik.kalorik_app.repositories.*;
import com.example.kalorik.kalorik_app.services.*;
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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.abs;

@Controller
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private FoodRepo foodRepo;
    @Autowired
    private ServingUnitRepo servingUnitRepo;

    @Autowired
    private MealsRepo mealsRepo;
 @Autowired
    private CategoryService categoryService;
    @Autowired
    private Meal_food_itemsRepo foodItemRepo;
    @Autowired
    private RecipeService recipeService;
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
    @Autowired
    private BodyService bodyService;

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

    @PostMapping("/main")
    String postMain(@RequestParam LocalDate shDt,  @RequestParam BigDecimal currentWeight)
    {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userRepo.findByUsername(auth.getName());
        Date d=BodyService.convertLocalDateToDate(shDt);
        System.out.println(shDt);
        List<Body> bodyList=bodyService.findBodiesByUserAndDt(u, d);
        Body b;
        if(bodyList.isEmpty()) {
            b = new Body();
            b.setDt(d);
            b.setWeight(currentWeight);
            b.setHeight(userInfoService.getUserInfoByUsr(u).getHeightCm());
            b.setUser(u);
        }
        else{
            b=bodyList.getFirst();
            b.setWeight(currentWeight);
        }
        bodyService.save(b);
        UserInfo ui=userInfoService.getUserInfoByUsr(u);
        ui.setWeightKg(currentWeight);
        userInfoService.save(ui);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(d);

        // Создаем URL для редиректа с параметром inputDate
        String redirectUrl = "redirect:/main?inputDate=" + formattedDate;

        // Возвращаем URL для редиректа
        return redirectUrl;
    }


    @GetMapping("/main")
    String getMain(@RequestParam(value = "inputDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date inputDate, Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userRepo.findByUsername(auth.getName());
        if (userInfoService.getUserInfoByUsr(u)==null){
            return "redirect:/addInfo";
        }
        double sumCalories=0.0;
        UserInfo ui=userInfoService.getUserInfoByUsr(u);
        Date d=new Date();
        if(inputDate!=null)
            d=inputDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date nw = cal.getTime();
        String formattedDate2;
        SimpleDateFormat sdfShort = new SimpleDateFormat("E");
        Date nw1=new Date();
        if(d.equals(nw) || d.equals(nw1))
        {
            formattedDate2="Сегодня";
        }
        else {
            Calendar cal1 = Calendar.getInstance(); // Получаем текущую дату и время
            cal1.add(Calendar.DATE, -1); // Вычитаем 1 день
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            Date yesterday = cal1.getTime();
            if (d.equals(yesterday)) {
                formattedDate2 = "Вчера";
            } else {
                Calendar cal2 = Calendar.getInstance(); // Получаем текущую дату и время
                cal2.add(Calendar.DATE, -2); // Вычитаем 1 день
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);
                Date befyest = cal2.getTime();
                if (d.equals(befyest)) {
                    formattedDate2 = "Позавчера";
                } else {
                    Calendar cal3 = Calendar.getInstance(); // Получаем текущую дату и время
                    cal3.add(Calendar.DATE, +1); // Вычитаем 1 день
                    cal3.set(Calendar.HOUR_OF_DAY, 0);
                    cal3.set(Calendar.MINUTE, 0);
                    cal3.set(Calendar.SECOND, 0);
                    cal3.set(Calendar.MILLISECOND, 0);
                    Date tomorrow = cal3.getTime();
                    if (d.equals(tomorrow)) {
                        formattedDate2 = "Завтра";
                    } else formattedDate2 = sdfShort.format(d) + ", " + dateFormat2.format(d);
                }
            }
        }
        formattedDate2=formattedDate2.substring(0, 1).toUpperCase() + formattedDate2.substring(1);
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
        int belNum=(int)(ui.getWeightKg().doubleValue()*1.5)+1;
        int chNum=(int)(ui.getWeightKg().doubleValue()*2)+1;
        int fatsNum=(int)(ui.getWeightKg().doubleValue()*0.8)+1;
        List<Meals> brf=mealsService.findMealsByMealDateAndMealTitleAndUser(d, "breakfast", u);
        List<String> brfmfiSTRING=new ArrayList<String>();
        int brfl=0;//пустой
        if(!brf.isEmpty())
        {
            List<MealFoodItems> brfmfi=itemService.findMeal_food_itemsByMeal(brf.getFirst());
            if(!brfmfi.isEmpty())
                brfl=1;
            for(MealFoodItems m:brfmfi) {
                brfmfiSTRING.add(m.getFood().getName());
            }
        }
        List<Meals> lnch=mealsService.findMealsByMealDateAndMealTitleAndUser(d, "lunch", u);
        List<String> lnchSTRING=new ArrayList<String>();
        int lnchfl=0;//пустой
        if(!lnch.isEmpty())
        {
            List<MealFoodItems> lnchmfi=itemService.findMeal_food_itemsByMeal(lnch.getFirst());
            if(!lnchmfi.isEmpty())
                lnchfl=1;
            for(MealFoodItems m:lnchmfi) {
                lnchSTRING.add(m.getFood().getName());
            }
        }
        List<Meals> dn=mealsService.findMealsByMealDateAndMealTitleAndUser(d, "dinner", u);
        List<String> dnSTRING=new ArrayList<String>();
        int dnfl=0;//пустой
        if(!dn.isEmpty())
        {
            List<MealFoodItems> dnmfi=itemService.findMeal_food_itemsByMeal(dn.getFirst());
            if(!dnmfi.isEmpty())
                dnfl=1;
            for(MealFoodItems m:dnmfi) {
                dnSTRING.add(m.getFood().getName());
            }
        }
        BigDecimal w=userInfoService.getUserInfoByUsr(u).getWeightKg().setScale(1, RoundingMode.HALF_UP);;
        String avatar=userInfoService.getUserInfoByUsr(u).getImageUrl();

        List<Recipes> allrec=new ArrayList<Recipes>();
        if(brfl!=0 && lnchfl==0 && dnfl==0)
        {
            Long id= 2L;
            allrec=recipeService.findByCategoryId(id);
            id= 3L;
            List<Recipes> din=recipeService.findByCategoryId(id);
            allrec.addAll(din);
        }
        if(brfl==0 && lnchfl!=0 && dnfl==0)
        {
            Long id= 1L;
            allrec=recipeService.findByCategoryId(id);
            id= 3L;
            List<Recipes> din=recipeService.findByCategoryId(id);
            allrec.addAll(din);
        }
        if(brfl==0 && lnchfl==0 && dnfl!=0)
        {
            Long id= 2L;
            allrec=recipeService.findByCategoryId(id);
            id= 1L;
            List<Recipes> din=recipeService.findByCategoryId(id);
            allrec.addAll(din);
        }
        if(brfl!=0 && lnchfl!=0 && dnfl==0)
        {
            allrec=recipeService.findByCategoryId(3L);
        }
        if(brfl!=0 && lnchfl==0 && dnfl!=0)
        {
            allrec= recipeService.findByCategoryId(2L);
        }
        if(brfl==0 && lnchfl!=0 && dnfl!=0)
        {
            allrec=recipeService.findByCategoryId(1L);
        }
        if(brfl==0 && lnchfl==0 && dnfl==0)
        {
            Long id= 2L;
            allrec=recipeService.findByCategoryId(id);
            id= 3L;
            List<Recipes> din=recipeService.findByCategoryId(id);
            allrec.addAll(din);
            id= 1L;
            din=recipeService.findByCategoryId(id);
            allrec.addAll(din);
        }
        allrec=RecipeService.calFilter(allrec, ui.getCaloriesnum()-(int)sumCalories);
        model.addAttribute("caloriesNum", ui.getCaloriesnum());
        model.addAttribute("breakfastItems", breakfastItems);
        model.addAttribute("lunchItems", lunchItems);
        model.addAttribute("dinnerItems", dinnerItems);
        model.addAttribute("sumCalories", (int)sumCalories);
        model.addAttribute("sumFats", (int)sumfats);
        model.addAttribute("sumBel", (int)sumbel);
        model.addAttribute("sumCh", (int)sumch);
        model.addAttribute("chNum", chNum);
        model.addAttribute("belNum", belNum);
        model.addAttribute("fatsNum", fatsNum);
        model.addAttribute("inputDate", formattedDate);
        model.addAttribute("SHOWDate", formattedDate2);
        model.addAttribute("brfmfi", brfmfiSTRING);
        model.addAttribute("lnchmfi", lnchSTRING);
        model.addAttribute("dnmfi", dnSTRING);
        model.addAttribute("currentWeight", w);
        model.addAttribute("avatar", avatar);
        model.addAttribute("rec", allrec);

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
        ui.setImageUrl("/images/defaultprofile.png");
        userInfoService.save(ui);
        Date d=new Date();
        Body b=new Body();
        b.setDt(d);
        b.setWeight(ui.getWeightKg());
        b.setHeight(ui.getHeightCm());
        b.setUser(usr);
        bodyService.save(b);
        return "redirect:/main";
    }

    @GetMapping("/getProducts") // Измененный URL
    public String getProducts(Model model, @RequestParam(name = "filter", required = false) String filter) {
        Iterable<Food> products;
        List<Food> l;
        if (filter != null && !filter.isEmpty()) {
            products = foodRepo.findByNameContainingIgnoreCase(filter);
            l=foodRepo.findByNameContainingIgnoreCase(filter);
        } else {
            products = foodRepo.findAll();
            l=foodRepo.findAll();
        }
        Iterable<ServingUnits> su=servingUnitRepo.findAll();
        model.addAttribute("products", products);
        model.addAttribute("servingUnits", su);
        boolean fl=!l.isEmpty();
        model.addAttribute("fl", fl);
        return "getProducts";  // Возвращаем только список продуктов
    }

    @GetMapping("/getMealProducts")
    public ResponseEntity<Map<String, List<MealProductInfo>>> getMealProducts(
            @RequestParam("mealDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mealDate,
            @RequestParam("mealTitle") String mealTitle) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u=userRepo.findByUsername(auth.getName());
        List<MealProductInfo> mealProducts = foodService.findByMealDateAndMealTitle(mealTitle, mealDate, u.getId());
        Map<String, List<MealProductInfo>> response = new HashMap<>();
        response.put("mealProducts", mealProducts);
        return ResponseEntity.ok(response); // Возвращаем JSON
    }


    @PostMapping("/add")
    public RedirectView add(
            @RequestParam String name,
            @RequestParam Double calories, // Float
            @RequestParam Double bel, // Float
            @RequestParam Double fats, // Float
            @RequestParam Double ch, // Float
            @RequestParam Double servingSize, // Float и servingSize
            @RequestParam Long servingUnit,
            Model model) { // Изменили тип Model

        ServingUnits su=servingUnitRepo.findServingUnitsById(servingUnit);
        Food f = new Food(name, calories, bel, fats, ch, servingSize, su);
        foodRepo.save(f);
        return new RedirectView("/");
    }

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
            meal.setMealDate(request.getInputDate());
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
                int fl=0;
                for(MealFoodItems i:items)
                {
                    if(i.getFood().equals(food))
                    {
                        fl=1;
                        i.setQuantity(i.getQuantity()+request.getQuantity());
                        itemService.save(i);
                    }
                }
                if(fl==0)
                {
                    MealFoodItems item = new MealFoodItems();
                    item.setId(mealFoodItemId); // Устанавливаем составной ключ!
                    item.setMeal(meal);
                    item.setFood(food);
                    item.setQuantity(request.getQuantity());
                    itemService.save(item);
                }

            }
            else {
                // 5. Создаем Meal_food_items и устанавливаем значения
                MealFoodItems item = new MealFoodItems();
                item.setId(mealFoodItemId); // Устанавливаем составной ключ!
                item.setMeal(meal);
                item.setFood(food);
                item.setQuantity(request.getQuantity());
                itemService.save(item);
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
            List<MealFoodItems> lmfi= itemService.findMeal_food_itemsByMeal(meal);
            if(lmfi.isEmpty())
            {
                mealsService.delete(meal);
            }
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
        private Date inputDate;
        // Геттеры и сеттеры
        public Date getInputDate(){return inputDate; }
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
