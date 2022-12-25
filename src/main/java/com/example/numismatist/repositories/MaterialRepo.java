package com.example.numismatist.repositories;

import com.example.numismatist.enteties.Material;
import com.example.numismatist.enteties.Series;
import org.springframework.data.repository.CrudRepository;

public interface MaterialRepo extends CrudRepository<Material, Integer> {
    Material findByMaterial(String materialFromFile);
}
