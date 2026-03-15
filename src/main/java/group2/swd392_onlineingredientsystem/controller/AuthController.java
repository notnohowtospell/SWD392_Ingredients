package group2.swd392_onlineingredientsystem.controller;

import group2.swd392_onlineingredientsystem.model.User;
import group2.swd392_onlineingredientsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        try {
            //user.setBalance(100000000);
            user.setMoney(100000000);
            userService.register(user);
            return "✅ Register successful!";
        } catch (Exception e) {
            return "❌ Register failed: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "✅ Login successful!";
        } catch (AuthenticationException e) {
            return "❌ Login failed: " + e.getMessage();
        }
    }

}
