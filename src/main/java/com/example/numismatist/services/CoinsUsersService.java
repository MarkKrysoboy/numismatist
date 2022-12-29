package com.example.numismatist.services;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.enteties.CoinsUsers;
import com.example.numismatist.enteties.Role;
import com.example.numismatist.enteties.User;
import com.example.numismatist.repositories.CoinRepo;
import com.example.numismatist.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoinsUsersService {

    @Autowired
    CoinRepo coinRepo;

    public Map<Coin, Integer> coinAddCount(User user) {
        Map<Coin, Integer> coinCount = null;
        Iterable<Coin> coins = coinRepo.findAll();
        for (Coin coinn : coins) {

            if (coinn.getCounts().contains(user)) {
                coinCount.put(coinn, user.getCounts().stream().filter(coin -> coin.equals(coinn)).findFirst()
                        .get().getCount());
            } else {
                coinCount.put(coinn, 0);
            }
        }
        return coinCount;
    }
}
