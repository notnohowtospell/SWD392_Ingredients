package group2.swd392_onlineingredientsystem.repository;

import group2.swd392_onlineingredientsystem.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IIngredientRepository extends JpaRepository<Ingredient, Integer>, JpaSpecificationExecutor<Ingredient> {
    // Có thể thêm custom query nếu cần
//    @Query("SELECT i FROM Ingredient i " +
//            "WHERE (:searchQuery IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) " +
//            "AND (:searchQuery IS NULL OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) " +
//            "AND (:categoryId IS NULL OR i.category.categoryId = :categoryId)")
    @Query(value = """
    SELECT * FROM ingredients
    WHERE (:searchQuery IS NULL OR name COLLATE SQL_Latin1_General_CP1_CI_AS LIKE '%' + :searchQuery + '%')
      AND (:searchQuery IS NULL OR description COLLATE SQL_Latin1_General_CP1_CI_AS LIKE '%' + :searchQuery + '%')
      AND (:categoryId IS NULL OR categoryid = :categoryId)
    """,
            countQuery = """
    SELECT COUNT(*) FROM ingredients
    WHERE (:searchQuery IS NULL OR name COLLATE SQL_Latin1_General_CP1_CI_AS LIKE '%' + :searchQuery + '%')
      AND (:searchQuery IS NULL OR description COLLATE SQL_Latin1_General_CP1_CI_AS LIKE '%' + :searchQuery + '%')
      AND (:categoryId IS NULL OR categoryid = :categoryId)
    """,
            nativeQuery = true)
    Page<Ingredient> searchIngredients(@Param("searchQuery") String searchQuery,
                                       @Param("categoryId") Integer categoryId,
                                       Pageable pageable);
} 