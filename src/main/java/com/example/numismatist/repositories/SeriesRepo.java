package com.example.numismatist.repositories;

import com.example.numismatist.enteties.Series;
import org.springframework.data.repository.CrudRepository;

public interface SeriesRepo extends CrudRepository<Series, Integer> {
    Series findBySeriesName(String seriesFromFile);
}
