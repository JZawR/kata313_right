package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin")
    public String userList(Model model, Authentication authentication) {
        User autorityUser = (User) authentication.getPrincipal();
        List<User> users = userService.listUsers();

        model.addAttribute("new_user", new User());
        model.addAttribute("edit_user", new User());
        model.addAttribute("users", users);
        model.addAttribute("autorityUser", autorityUser);
        model.addAttribute("userRoles", roleService.getRoleList());
        return "admin";
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/newUser")
    public String getAddUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("userRoles", roleService.getRoleList());
        return "newUser";
    }

    @PostMapping(value = "/add")
    public String addUser(@ModelAttribute("new_user") User user,
                          @RequestParam("rol")Long[] roles) {
        Set<Role> userRoles = roleService.getRolesInIds(roles);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/editUser")
    public String getEditUserForm(@ModelAttribute("edit_user") User user, Model model) {
        model.addAttribute("userHiddenPassword", user.getPassword());
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("userRoles", roleService.getRoleList());
        return "editUser";
    }

    @PostMapping(value = "/editUser")
    public String editUser(User user,
                           @RequestParam("userHiddenPassword") String userHiddenPassword,
                           @RequestParam("rol") Long[] roles) {
        Set<Role> userRoles = roleService.getRolesInIds(roles);
        user.setRoles(userRoles);
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(userHiddenPassword);
        }
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("id") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String getUser(Authentication authentication, Model model) {
        User autorityUser = (User) authentication.getPrincipal();
        model.addAttribute("autorityUser", autorityUser);
        return "user";
    }
}