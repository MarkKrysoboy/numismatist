package com.example.numismatist.controllers;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.enteties.Series;
import com.example.numismatist.enteties.User;
import com.example.numismatist.repositories.CoinRepo;
import com.example.numismatist.repositories.SeriesRepo;
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
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    CoinsUsersService coinsUsersService;

    @Autowired
    CoinService coinService;

    private final CoinRepo coinRepo;

    private final SeriesRepo seriesRepo;

    public MainController(CoinRepo coinRepo, SeriesRepo seriesRepo) {
        this.coinRepo = coinRepo;
        this.seriesRepo = seriesRepo;
    }

    @ModelAttribute("currentUser")
    public User extractCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("/")
    public String greeting(Model model) {

        return "greeting";
    }

    @GetMapping("main")
    public String main(Model model,
                       @ModelAttribute("currentUser") User currentUser,
                       @RequestParam(required = false) String series,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 9) Pageable pageable) {
        getPage(model, series, pageable);
        return "main";
    }

    private void getPage(Model model, String series, Pageable pageable) {
        Page<Coin> page;
        if (series != null) {
            page = coinRepo.findBySeries(seriesRepo.findBySeriesName(series), pageable);
            model.addAttribute("parameter", "&series=" + series);
        } else {
            page = coinRepo.findAll(pageable);
        }
        model.addAttribute("page", page);
        model.addAttribute("pagesList", coinService.pagesList(page));
    }

    @GetMapping("/{user}")
    public String userCoins(Model model) {
        // Todo findUserCoins
        Iterable<Coin> coins = coinRepo.findAll();
        model.addAttribute("coins", coins);
        return "main";
    }

//    @GetMapping("/main/series")
//    public String seriesCoins(Model model,
//                              @RequestParam(required = false) String series,
//                              @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 9) Pageable pageable) {
//        Page<Coin> page = coinRepo.findBySeries(seriesRepo.findBySeriesName(series), pageable);
//        model.addAttribute("page", page);
//        model.addAttribute("parameter", "&series=" + series);
//        model.addAttribute("pagesList", coinService.pagesList(page));
//        return "main";
//    }

    @GetMapping("main/{catalogNumber}")
    public String coinCatalogNumber(Model model, @PathVariable String catalogNumber) {
        model.addAttribute("coin", coinRepo.findByCatalogNumber(catalogNumber));
        return "coin";
    }

//    @PostMapping("main")
//    public String filterCoins(
//            Model model,
//            @RequestParam(required = false) String series,
//            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 9) Pageable pageable
//    ) {
//        getPage(model, series, pageable);
//        return "main";
//    }


}
