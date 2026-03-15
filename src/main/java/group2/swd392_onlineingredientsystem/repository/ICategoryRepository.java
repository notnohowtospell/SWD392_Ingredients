package group2.swd392_onlineingredientsystem.repository;

import group2.swd392_onlineingredientsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
} 