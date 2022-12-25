package com.example.numismatist.services;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.enteties.Material;
import com.example.numismatist.enteties.Nominal;
import com.example.numismatist.enteties.Series;
import com.example.numismatist.repositories.CoinRepo;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Data
public class ReadXlsxService {

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private CoinAttributeService coinAttributeService;
    private MultipartFile file;
    private List<Coin> coins;
    private String str;

    public File multipartFileToFile(MultipartFile multipart, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    public List<Coin> readXlsxToList() {
        coins = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(multipartFileToFile(file, file.getName()))) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                Coin coin = new Coin();
                if (row.getCell(0) == null) break;
                coin.setCatalogNumber(row.getCell(0).toString().trim());
                coin.setReleaseDate(row.getCell(1).getDateCellValue());
                coin.setName(row.getCell(2).toString().trim().replaceAll("[&<>; a-z/]", " "));
                if (row.getCell(3) != null) {
                    String seriesFromFile = row.getCell(3).toString().trim();
                    Series series = coinAttributeService.addSeries(seriesFromFile);
                    coin.setSeries(series);
                }
                if (row.getCell(4) != null) {
                    String nominalFromFile = row.getCell(4).toString().trim();
                    Nominal nominal = coinAttributeService.addNominal(nominalFromFile);
                    coin.setNominal(nominal);
                }
                if (row.getCell(5) != null) {
                    String materialFromFile = row.getCell(5).toString().trim();
                    Material material = coinAttributeService.addMaterial(materialFromFile);
                    coin.setMaterial(material);
                }

                coins.add(coin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coins;
    }

    public void addCoinsFromList(List<Coin> coins) {
        for (Coin coin : coins) {
            Coin coinTemp = coinRepo.findByCatalogNumber(coin.getCatalogNumber());
            if (coinTemp == null) coinRepo.save(coin);
        }
    }

}
