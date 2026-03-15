package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.Ingredient;
import group2.swd392_onlineingredientsystem.repository.IIngredientRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IIngredientRepository ingredientRepository;

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient update(Integer id, Ingredient ingredient) {
        ingredient.setIngredientId(id);
        return ingredientRepository.save(ingredient);
    }

    @Override
    public void delete(Integer id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public Optional<Ingredient> findById(Integer id) {
        return ingredientRepository.findById(id);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Page<Ingredient> searchAndFilter(String keyword, Integer categoryId, Pageable pageable) {
        Specification<Ingredient> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("categoryId"), categoryId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return ingredientRepository.findAll(spec, pageable);
    }
} 