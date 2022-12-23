package com.example.numismatist.controllers;

import com.example.numismatist.enteties.Role;
import com.example.numismatist.enteties.User;
import com.example.numismatist.services.UserService;
import com.example.numismatist.enteties.Role;
import com.example.numismatist.enteties.User;
import com.example.numismatist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/user")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping
//    public String userList(Model model) {
//        model.addAttribute("users", userService.findAll());
//
//        return "user_list";
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("{user}")
//    public String userEditForm(@PathVariable User user, Model model) {
//        model.addAttribute("user", user);
//        model.addAttribute("roles", Role.values());
//        return "user_edit";
//    }
//

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{user}")
    public String userSave(@RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam(name = "id") User user) {

        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    //
//    @GetMapping("profile")
//    public String getProfile(Model model, @AuthenticationPrincipal User user) {
//        model.addAttribute("username", user.getUsername());
//        model.addAttribute("email", user.getEmail());
//
//        return "profile";
//    }
//
//    @PostMapping("profile")
//    public String updateProfile(
//            @AuthenticationPrincipal User user,
//            @RequestParam String password,
//            @RequestParam String email) {
//        userService.updateProfile(user, password, email);
//        return "redirect:/user/profile";
//    }
//
    @ModelAttribute("currentUser")
    public User extractCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    //
//    @GetMapping("subscribe/{user}")
//    public String subscribe(@PathVariable User user, @ModelAttribute("currentUser") User currentUser) {
//
//        userService.subscribe(currentUser, user);
//        return "redirect:/user_messages/" + user.getId();
//    }
//
//    @GetMapping("unsubscribe/{user}")
//    public String unsubscribe(@PathVariable User user, @ModelAttribute("currentUser") User currentUser) {
//
//        userService.unsubscribe(currentUser, user);
//        return "redirect:/user_messages/" + user.getId();
//    }
//
//    @GetMapping("{type}/{user}/list")
//    public String userList(Model model, @PathVariable User user, @PathVariable String type) {
//        model.addAttribute("userChannel", user);
//        model.addAttribute("type", type);
//
//        if ("subscriptions".equals(type)) {
//            model.addAttribute("users", user.getSubscriptions());
//        } else {
//            model.addAttribute("users", user.getSubscribers());
//        }
//
//        return "subscriptions";
//    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "user_list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user_edit";
    }

}
