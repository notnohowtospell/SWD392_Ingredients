package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.User;
import group2.swd392_onlineingredientsystem.repository.RoleRepository;
import group2.swd392_onlineingredientsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import group2.swd392_onlineingredientsystem.model.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer id);
    void save(User user);
    void delete(Integer id);
    User register(User user) throws Exception;
    Optional<User> login(String username, String rawPassword);
    Optional<User> findByUsername(String username);
}