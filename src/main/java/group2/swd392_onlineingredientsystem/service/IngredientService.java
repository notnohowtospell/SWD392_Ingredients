package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    Ingredient save(Ingredient ingredient);
    Ingredient update(Integer id, Ingredient ingredient);
    void delete(Integer id);
    Optional<Ingredient> findById(Integer id);
    List<Ingredient> findAll();
    Page<Ingredient> searchAndFilter(String keyword, Integer categoryId, Pageable pageable);
}

