package group2.swd392_onlineingredientsystem.controller;
import group2.swd392_onlineingredientsystem.model.CartItem;
import group2.swd392_onlineingredientsystem.model.Ingredient;
import group2.swd392_onlineingredientsystem.model.Order;
import group2.swd392_onlineingredientsystem.model.User;
import group2.swd392_onlineingredientsystem.repository.IIngredientRepository;
import group2.swd392_onlineingredientsystem.repository.IOrderRepository;
import group2.swd392_onlineingredientsystem.repository.IUserRepository;
import group2.swd392_onlineingredientsystem.repository.UserRepository;
import group2.swd392_onlineingredientsystem.service.ICartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;
    private final IIngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final IOrderRepository orderRepository;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("totalAmount", cartService.calculateTotal(session));

        return "cart"; // cart.html
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam int ingredientId,
                            @RequestParam int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        if (ingredient != null) {
            cartService.addToCart(session, ingredient, quantity);
            redirectAttributes.addFlashAttribute("message", "✔️ " + ingredient.getName() + " added to cart");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy nguyên liệu.");
        }
        return "redirect:/cart";
    }
    @PostMapping("/addAjax")
    @ResponseBody
    public ResponseEntity<String> addToCartWithoutRedirecting(@RequestParam int ingredientId,
                                                              @RequestParam int quantity,
                                                              HttpSession session) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        if (ingredient != null) {
            cartService.addToCart(session, ingredient, quantity);
            return ResponseEntity.ok("Added to cart");
        }
        return ResponseEntity.badRequest().body("Ingredient not found");
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam int ingredientId,
                             @RequestParam int quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(session, ingredientId, quantity);
        redirectAttributes.addFlashAttribute("message", "Updated");
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam int ingredientId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(session, ingredientId);
        redirectAttributes.addFlashAttribute("message", "Deleted");
        return "redirect:/cart";
    }
    @PostMapping("/purchase")
    public String purchase(HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to purchase.");
            return "redirect:/cart";
        }

        BigDecimal total = cartService.calculateTotal(session);
        //BigDecimal balance = BigDecimal.valueOf(user.get().getBalance());
        BigDecimal balance = BigDecimal.valueOf(user.get().getMoney());

        if (balance.compareTo(total) < 0) {
            redirectAttributes.addFlashAttribute("error", "Insufficient balance.");
            return "redirect:/cart";
        }

        try {
            List<CartItem> cartItems = cartService.getCart(session);
            for (CartItem item : cartItems) {

                Ingredient ingredient = ingredientRepository.findById(item.getIngredient().getIngredientId()).orElse(null);
                if (ingredient == null) continue;

                int newQuantity = ingredient.getQuantity() - item.getQuantity();
                if (newQuantity < 0) {
                    redirectAttributes.addFlashAttribute("error", "Not enough stock for: " + ingredient.getName());
                    return "redirect:/cart";
                }

                ingredient.setQuantity(newQuantity);
                ingredientRepository.save(ingredient);
            }

            // 2. Create and save order
            Order order = cartService.createOrderFromCart(session, user.get());
            orderRepository.save(order);

            // 3. Deduct balance
            BigDecimal newBalance = balance.subtract(total);
            //user.get().setBalance(newBalance.doubleValue());
            user.get().setMoney(newBalance.doubleValue());
            userRepository.save(user.get());

            // 4. Clear cart
            cartService.clearCart(session);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }

        redirectAttributes.addFlashAttribute("message", "✅ Purchase successful!");
        return "redirect:/cart";
    }


}
