package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService implements ICartService {

    private static final String CART_SESSION_KEY = "cart";

    @Override
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    @Override
    public void addToCart(HttpSession session, Ingredient ingredient, int quantity) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getIngredient().getIngredientId().equals(ingredient.getIngredientId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cart.add(new CartItem(ingredient, quantity));
    }

    @Override
    public void updateQuantity(HttpSession session, int ingredientId, int quantity) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getIngredient().getIngredientId() == ingredientId) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    @Override
    public void removeFromCart(HttpSession session, int ingredientId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getIngredient().getIngredientId() == ingredientId);
    }
    @Override
    public BigDecimal calculateTotal(HttpSession session) {
        List<CartItem> cart = getCart(session);
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart) {
            total = total.add(item.getTotalPrice()); // đã là BigDecimal
        }

        return total;
    }
    @Override
    public Order createOrderFromCart(HttpSession session, User user) {
        BigDecimal totalAmount = calculateTotal(session);
        List<CartItem> cart = getCart(session);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartItem cartItem : cart) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setIngredient(cartItem.getIngredient());
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(cartItem.getIngredient().getPrice());

            orderDetails.add(detail);
        }

        order.setOrderDetails(orderDetails);
        return order;
    }


    @Override
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}

