package web.springBootSecurityProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.springBootSecurityProject.models.Role;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.services.RoleService;
import web.springBootSecurityProject.services.UserService;
import web.springBootSecurityProject.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private RoleService roleService;
    private UserService userService;
    private UserValidator validator;

    @Autowired
    public AdminController(RoleService roleService, UserService userService, UserValidator validator) {
        this.roleService = roleService;
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") int id, @ModelAttribute("user") User updateUser) {
        userService.update(id, updateUser);
        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "/admin/add";
    }

    @PostMapping("/add")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                                      @RequestParam("selectedRole") String selectedRole) {
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/admin/add";
        }

        Role userRole;

        if (selectedRole.equals("ADMIN")) {
            userRole = roleService.findRoleByName("ROLE_ADMIN").orElseGet(() -> {
                Role newUserRole = new Role("ROLE_ADMIN");
                roleService.save(newUserRole);
                return newUserRole;
            });
        } else {
            userRole  = roleService.findRoleByName("ROLE_USER").orElseGet(() -> {
                Role newUserRole = new Role("ROLE_USER");
                roleService.save(newUserRole);
                return newUserRole;
            });
        }

        user.getRoles().add(userRole);

        userService.register(user);

        return "redirect:/admin";
    }

}
