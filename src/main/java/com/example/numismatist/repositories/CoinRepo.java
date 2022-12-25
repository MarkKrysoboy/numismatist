package com.example.numismatist.repositories;

import com.example.numismatist.enteties.Coin;
import org.springframework.data.repository.CrudRepository;

public interface CoinRepo extends CrudRepository<Coin, Integer> {
    Coin findByCatalogNumber(String catalogNumber);
}
