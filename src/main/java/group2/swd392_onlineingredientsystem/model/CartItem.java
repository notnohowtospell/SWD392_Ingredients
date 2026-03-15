package group2.swd392_onlineingredientsystem.model;
    import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CartItem {
        private Ingredient ingredient;
        private int quantity;

        public BigDecimal getTotalPrice() {
            return ingredient.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

