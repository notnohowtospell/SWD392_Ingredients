package group2.swd392_onlineingredientsystem;
import group2.swd392_onlineingredientsystem.service.UserService;


import group2.swd392_onlineingredientsystem.model.User;
import group2.swd392_onlineingredientsystem.repository.UserRepository;
import group2.swd392_onlineingredientsystem.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;




}
