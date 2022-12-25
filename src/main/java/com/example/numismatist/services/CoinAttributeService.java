package com.example.numismatist.services;

import com.example.numismatist.enteties.Material;
import com.example.numismatist.enteties.Nominal;
import com.example.numismatist.enteties.Series;
import com.example.numismatist.repositories.CoinRepo;
import com.example.numismatist.repositories.MaterialRepo;
import com.example.numismatist.repositories.NominalRepo;
import com.example.numismatist.repositories.SeriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CoinAttributeService{

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private SeriesRepo seriesRepo ;

    @Autowired
    private NominalRepo nominalRepo;

    @Autowired
    private MaterialRepo materialRepo;

    public Series addSeries(String seriesFromFile) {
        Series seriesFromDb = seriesRepo.findBySeriesName(seriesFromFile);
        if (seriesFromDb != null) {
            return seriesFromDb;
        }
        Series series = new Series();
        series.setSeriesName(seriesFromFile);
        seriesRepo.save(series);
        return series;
    }

    public Nominal addNominal(String nominalFromFile) {
        Nominal nominalFromDb = nominalRepo.findByNominal(nominalFromFile);
        if (nominalFromDb != null) {
            return nominalFromDb;
        }
        Nominal nominal = new Nominal();
        nominal.setNominal(nominalFromFile);
        nominalRepo.save(nominal);
        return nominal;
    }

    public Material addMaterial(String materialFromFile) {
        Material materialFromDb = materialRepo.findByMaterial(materialFromFile);
        if (materialFromDb != null) {
            return materialFromDb;
        }
        Material material = new Material();
        material.setMaterial(materialFromFile);
        materialRepo.save(material);
        return material;
    }


}
