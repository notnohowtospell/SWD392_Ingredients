package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface SearchService {
    Page<Ingredient> searchIngredients(String searchQuery,
                                       Integer categoryId,
                                       Pageable pageable);
}
