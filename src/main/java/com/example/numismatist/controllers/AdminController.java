package com.example.numismatist.controllers;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.enteties.Coinage;
import com.example.numismatist.enteties.User;
import com.example.numismatist.repositories.CoinRepo;
import com.example.numismatist.services.LoadFromFileService;
import com.example.numismatist.services.LoadFromBankPageService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping(value = "/main")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    LoadFromBankPageService lfbp;

    @Autowired
    LoadFromFileService lff;

    final CoinRepo coinRepo;

    public AdminController(CoinRepo coinRepo) {
        this.coinRepo = coinRepo;
    }

    @ModelAttribute("currentUser")
    public User extractCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("updating")
    public String updateDb(Model model) {
        Long countCoin = coinRepo.count();
        model.addAttribute("countCoins", countCoin);
        return "updating";
    }

    @PostMapping("updating")
    public String handleFileUpload(@RequestParam(value = "file", required = false) MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            model.addAttribute("fileName", file.getOriginalFilename());
            try {
                model.addAttribute("coinlist", lff.addCoinFromFile(file));
                model.addAttribute("message", "Added next coins:");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "File upload is failed: " + e.getMessage());
            }
        } else {
            model.addAttribute("message", "File upload is failed: File is empty");
        }
        return "updating";
    }

//    @PostMapping("updating")
//    public String downloadData(@RequestParam String url, Model model) throws ParseException {
//        model.addAttribute("coinlist", lfbp.addCoinFromBankPage(url));
//        return "updating";
//    }

}
