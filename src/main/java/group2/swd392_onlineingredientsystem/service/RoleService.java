package group2.swd392_onlineingredientsystem.service;

import group2.swd392_onlineingredientsystem.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(Integer id);
    List<Role> getAllRoles();
}