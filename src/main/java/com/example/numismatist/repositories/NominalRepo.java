package com.example.numismatist.repositories;

import com.example.numismatist.enteties.Nominal;
import com.example.numismatist.enteties.Series;
import org.springframework.data.repository.CrudRepository;

public interface NominalRepo extends CrudRepository<Nominal, Integer> {
    Nominal findByNominal(String nominalFromFile);
}
