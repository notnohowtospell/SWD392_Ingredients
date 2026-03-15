package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.CartItem;
import group2.swd392_onlineingredientsystem.model.Ingredient;
import group2.swd392_onlineingredientsystem.model.Order;
import group2.swd392_onlineingredientsystem.model.User;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ICartService {
    List<CartItem> getCart(HttpSession session);

    void addToCart(HttpSession session, Ingredient ingredient, int quantity);

    void updateQuantity(HttpSession session, int ingredientId, int quantity);

    void removeFromCart(HttpSession session, int ingredientId);
    BigDecimal calculateTotal(HttpSession session);

    Order createOrderFromCart(HttpSession session, User user);

    void clearCart(HttpSession session);

}

