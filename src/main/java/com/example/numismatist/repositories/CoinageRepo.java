package com.example.numismatist.repositories;

import com.example.numismatist.enteties.Coinage;
import com.example.numismatist.enteties.Material;
import org.springframework.data.repository.CrudRepository;

public interface CoinageRepo extends CrudRepository<Coinage, Integer> {
    Coinage findByCoinage(String coinageName);
}
