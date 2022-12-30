package com.example.numismatist.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {

    public List<Integer> pagesList(Page page) {
        List<Integer> pagesList = new ArrayList<>();
        pagesList.add(1);

        if (page.getNumber() - 2 > 1) {
            pagesList.add(-1);
        }

        for (int i = page.getNumber() - 1; i <= page.getNumber() + 3; i++) {

            if ((i > 1) & (i < page.getTotalPages())) pagesList.add(i);
        }

        if (page.getNumber() + 2 < page.getTotalPages() - 2){
            pagesList.add(-1);
        }

        if (page.getTotalPages() != 1) pagesList.add(page.getTotalPages());

        return pagesList;
    }
}
