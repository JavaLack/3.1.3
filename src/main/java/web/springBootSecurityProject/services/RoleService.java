package web.springBootSecurityProject.services;

import web.springBootSecurityProject.models.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findRoleByName(String name);
    void save(Role role);
}
