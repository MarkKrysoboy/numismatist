package com.example.numismatist.controllers;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.repositories.CoinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @Autowired
    private CoinRepo coinRepo;

    @GetMapping("/")
    public String greeting(Model model) {

        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Coin> coins = coinRepo.findAll();
        model.addAttribute("coins", coins);
        return "main";
    }

}
