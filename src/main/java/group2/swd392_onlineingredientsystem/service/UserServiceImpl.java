package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.Role;
import group2.swd392_onlineingredientsystem.model.User;
import group2.swd392_onlineingredientsystem.repository.RoleRepository;
import group2.swd392_onlineingredientsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepo.deleteById(id);
    }

    @Override
    public User register(User user) throws Exception {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new Exception("Email already registered");
        }

        Role role = roleRepo.findById(2).orElseThrow(() -> new Exception("Role not found"));
        user.setRole(role);
        user.setPassword(encoder.encode(user.getPassword()));
//        user.setBalance(100000000);
        user.setMoney(5000000);
        return userRepo.save(user);
    }

    @Override
    public Optional<User> login(String username, String rawPassword) {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent() && encoder.matches(rawPassword, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
