// package com.example.kalorik.kalorik_app.servingwebcontent;

// import com.example.kalorik.kalorik_app.domain.Categories;
// import com.example.kalorik.kalorik_app.services.CategoryService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Controller
// @RequestMapping("/categories")
// public class CategoryController {
    
//     private final CategoryService categoryService;

//     public CategoryController(CategoryService categoryService) {
//         this.categoryService = categoryService;
//     }

//     @GetMapping
//     public String getAllCategories(Model model) {
//         model.addAttribute("categories", categoryService.getAllCategories());
//         return "categories/list";
//     }

//     @GetMapping("/{id}")
//     public String getCategoryById(@PathVariable Long id, Model model) {
//         Optional<Categories> category = categoryService.getCategoryById(id);
//         if (!category.isPresent()) {
//             return "redirect:/error";
//         }
//         model.addAttribute("category", category.get());
//         return "categories/view";
//     }

//     @GetMapping("/new")
//     public String showCreateForm(Model model) {
//         model.addAttribute("category", new Categories());
//         return "categories/form";
//     }

//     @PostMapping
//     public String createCategory(@ModelAttribute Categories category) {
//         categoryService.saveCategory(category);
//         return "redirect:/categories";
//     }

//     @GetMapping("/{id}/edit")
//     public String showEditForm(@PathVariable Long id, Model model) {
//         Optional<Categories> category = categoryService.getCategoryById(id);
//         if (!category.isPresent()) {
//             return "redirect:/error";
//         }
//         model.addAttribute("category", category.get());
//         return "categories/form";
//     }

//     // @PostMapping("/{id}")
//     // public String updateCategory(@PathVariable Long id, @ModelAttribute Categories category) {
//     //     try {
//     //         categoryService.updateCategory(id, category);
//     //     } catch (CategoryService.ResourceNotFoundException e) {
//     //         return "redirect:/error";
//     //     }
//     //     return "redirect:/categories";
//     // }

//     // @PostMapping("/{id}/delete")
//     // public String deleteCategory(@PathVariable Long id) {
//     //     categoryService.deleteCategory(id);
//     //     return "redirect:/categories";
//     // }
// }
