package group2.swd392_onlineingredientsystem.controller;

import group2.swd392_onlineingredientsystem.model.User;
import group2.swd392_onlineingredientsystem.security.CustomUserDetails;
import group2.swd392_onlineingredientsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; // HTML login page
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            redirectAttributes.addFlashAttribute("message", "✅ Đăng nhập thành công!");
            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", "❌ Sai tên đăng nhập hoặc mật khẩu");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user,
                                  RedirectAttributes redirectAttributes) {
        try {
            userService.register(user);
            redirectAttributes.addFlashAttribute("message", "✅ Đăng ký thành công!");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Đăng ký thất bại: " + e.getMessage());
            return "redirect:/register";
        }
    }
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        // ✅ Gán role vào session để Thymeleaf sử dụng
        session.setAttribute("role", currentUser.getRole().getRoleName());

        // Gán thêm user và balance nếu cần
        model.addAttribute("user", currentUser);
//        model.addAttribute("balance", currentUser.getBalance());
        model.addAttribute("balance", currentUser.getMoney());
        return "dashboard";
    }


}
