package com.example.numismatist.services;

import com.example.numismatist.enteties.Coinage;
import com.example.numismatist.enteties.Material;
import com.example.numismatist.enteties.Nominal;
import com.example.numismatist.enteties.Series;
import com.example.numismatist.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CoinAttributeService {

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private SeriesRepo seriesRepo;

    @Autowired
    private NominalRepo nominalRepo;

    @Autowired
    private MaterialRepo materialRepo;

    @Autowired
    private CoinageRepo coinageRepo;

    public void addSeries(String seriesFromFile, String linkToBankPage, Integer idInBank) {
        if (seriesFromFile != null) {
            Series seriesFromDb = seriesRepo.findBySeriesName(seriesFromFile);
            if (seriesFromDb == null) {
                Series series = new Series();
                series.setSeriesName(seriesFromFile);
                series.setLinkToBankPage(linkToBankPage);
                series.setIdInBank(idInBank);
                seriesRepo.save(series);
            }
        }
    }


    public void addNominal(String nominalFromFile) {
        if (nominalFromFile != null) {
            Nominal nominalFromDb = nominalRepo.findByNominal(nominalFromFile);
            if (nominalFromDb == null) {
                Nominal nominal = new Nominal();
                nominal.setNominal(nominalFromFile);
                nominalRepo.save(nominal);
            }
        }
    }

    public void addMaterial(String materialFromFile) {
        if (materialFromFile != null) {
            Material materialFromDb = materialRepo.findByMaterial(materialFromFile);
            if (materialFromDb == null) {
                Material material = new Material();
                material.setMaterial(materialFromFile);
                materialRepo.save(material);
            }
        }
    }

    public void addCoinage(String coinageName) {
        if (coinageName != null) {
            Coinage coinageFromDb = coinageRepo.findByCoinage(coinageName);
            if (coinageFromDb == null) {
                Coinage coinage = new Coinage();
                coinage.setCoinage(coinageName);
                coinageRepo.save(coinage);
            }
        }
    }

}
