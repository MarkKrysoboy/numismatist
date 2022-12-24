package com.example.numismatist.controllers;

import com.example.numismatist.enteties.User;
import com.example.numismatist.repositories.CoinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    final
    CoinRepo coinRepo;

    public AdminController(CoinRepo coinRepo) {
        this.coinRepo = coinRepo;
    }

    @ModelAttribute("currentUser")
    public User extractCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("/updating_database")
    public String updateBase(Model model) {
        Long countCoin = coinRepo.count();
        model.addAttribute("countCoins", countCoin);

        return "updating_database";
    }
}
