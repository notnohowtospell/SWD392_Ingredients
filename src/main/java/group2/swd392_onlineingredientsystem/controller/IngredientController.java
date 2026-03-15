package group2.swd392_onlineingredientsystem.controller;

import group2.swd392_onlineingredientsystem.model.Category;
import group2.swd392_onlineingredientsystem.model.Ingredient;
import group2.swd392_onlineingredientsystem.repository.ICategoryRepository;
import group2.swd392_onlineingredientsystem.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private ICategoryRepository categoryRepository;

    /**
     * Hiển thị danh sách nguyên liệu với chức năng tìm kiếm, sắp xếp, phân trang
     */
    @GetMapping
    public String listIngredients(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "dir", defaultValue = "asc") String dir,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, dir.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Ingredient> ingredientPage = ingredientService.searchAndFilter(keyword, categoryId, pageable);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("ingredientPage", ingredientPage);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "ingredient/list";
    }

    /**
     * Hiển thị form tạo mới nguyên liệu
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("ingredient", new Ingredient());
        model.addAttribute("categories", categoryRepository.findAll());
        return "ingredient/create";
    }

    /**
     * Xử lý lưu nguyên liệu mới
     */
    @PostMapping("/create")
    public String create(@ModelAttribute Ingredient ingredient,
                         @RequestParam("category.categoryId") Integer categoryId,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        ingredient.setCategory(category);

        String projectDir = System.getProperty("user.dir"); // Thư mục gốc project
        String uploadDir = projectDir + "/src/main/resources/static/images/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                File dest = new File(uploadDir, fileName);
                imageFile.transferTo(dest);
                ingredient.setImage("/images/" + fileName);
            }
            ingredientService.save(ingredient);
            redirectAttributes.addFlashAttribute("success", "Tạo nguyên liệu thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh!");
        }
        return "redirect:/ingredients";
    }

    /**
     * Hiển thị form chỉnh sửa nguyên liệu
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Optional<Ingredient> ingredientOpt = ingredientService.findById(id);
        if (ingredientOpt.isEmpty()) {
            return "redirect:/ingredients";
        }
        model.addAttribute("ingredient", ingredientOpt.get());
        model.addAttribute("categories", categoryRepository.findAll());
        return "ingredient/edit";
    }

    /**
     * Xử lý cập nhật nguyên liệu
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       @ModelAttribute Ingredient ingredient,
                       @RequestParam("category.categoryId") Integer categoryId,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        ingredient.setCategory(category);
        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + "/src/main/resources/static/images/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                File dest = new File(uploadDir, fileName);
                imageFile.transferTo(dest);
                ingredient.setImage("/images/" + fileName);
            } else {
                Ingredient old = ingredientService.findById(id).orElse(null);
                if (old != null) ingredient.setImage(old.getImage());
            }
            ingredientService.update(id, ingredient);
            redirectAttributes.addFlashAttribute("success", "Cập nhật nguyên liệu thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh!");
        }
        return "redirect:/ingredients";
    }

    /**
     * Hiển thị chi tiết nguyên liệu
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Optional<Ingredient> ingredientOpt = ingredientService.findById(id);
        if (ingredientOpt.isEmpty()) {
            return "redirect:/ingredients";
        }
        model.addAttribute("ingredient", ingredientOpt.get());
        return "ingredient/detail";
    }

    /**
     * Xóa nguyên liệu
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            ingredientService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa nguyên liệu thành công!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa nguyên liệu này vì đã có đơn hàng chi tiết tham chiếu!");
        }
        return "redirect:/ingredients";
    }
}