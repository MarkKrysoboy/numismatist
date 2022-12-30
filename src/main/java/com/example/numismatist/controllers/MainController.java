package com.example.numismatist.controllers;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.enteties.User;
import com.example.numismatist.repositories.CoinRepo;
import com.example.numismatist.services.CoinService;
import com.example.numismatist.services.CoinsUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @Autowired
    CoinsUsersService coinsUsersService;

    @Autowired
    CoinService coinService;

    private final CoinRepo coinRepo;

    public MainController(CoinRepo coinRepo) {
        this.coinRepo = coinRepo;
    }

    @ModelAttribute("currentUser")
    public User extractCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("/")
    public String greeting(Model model) {

        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model,
                       @ModelAttribute("currentUser") User currentUser,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 9) Pageable pageable) {
        Page<Coin> page = coinRepo.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("pagesList", coinService.pagesList(page));
        return "main";
    }

    @GetMapping("/main/{user}")
    public String userCoins(Model model) {
        // Todo findUserCoins
        Iterable<Coin> coins = coinRepo.findAll();
        model.addAttribute("coins", coins);
        return "main";
    }

}
