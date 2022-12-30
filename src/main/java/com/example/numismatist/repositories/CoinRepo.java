package com.example.numismatist.repositories;

import com.example.numismatist.enteties.Coin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CoinRepo extends CrudRepository<Coin, Integer> {
    Page findByCatalogNumber(String catalogNumber, Pageable pageable);
    Coin findByCatalogNumber(String catalogNumber);
    Page findAll(Pageable pageable);
}
