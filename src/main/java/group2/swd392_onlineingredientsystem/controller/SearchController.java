package group2.swd392_onlineingredientsystem.controller;

import group2.swd392_onlineingredientsystem.model.Category;
import group2.swd392_onlineingredientsystem.model.Ingredient;
import group2.swd392_onlineingredientsystem.repository.ICategoryRepository;
import group2.swd392_onlineingredientsystem.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private ICategoryRepository categoryRepository;

    @GetMapping
    public String searchIngredient(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "sort", defaultValue = "name") String sortBy,
                                   @RequestParam(value = "dir", defaultValue = "asc") String direction,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                   @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                   Model model){
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, 10, sort);

        String keywordParam = (keyword != null && !keyword.trim().isEmpty()) ? keyword : null;

        Page<Ingredient> ingredientPage = searchService.searchIngredients(keywordParam, categoryId, pageable);
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("ingredientPage", ingredientPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categories);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", direction);
        return "search/list";
    }
}
