package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.Ingredient;
import group2.swd392_onlineingredientsystem.repository.IIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService{
    @Autowired
    private IIngredientRepository ingredientRepository;
    @Override
    public Page<Ingredient> searchIngredients(String searchQuery, Integer categoryId, Pageable pagable) {
        return ingredientRepository.searchIngredients(searchQuery, categoryId, pagable);
    }
}
