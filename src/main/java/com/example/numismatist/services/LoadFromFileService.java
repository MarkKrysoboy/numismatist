package com.example.numismatist.services;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.repositories.*;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class LoadFromFileService {

    @Autowired
    CoinRepo coinRepo;

    @Autowired
    private CoinAttributeService coinAttributeService;

    @Autowired
    SeriesRepo seriesRepo;

    @Autowired
    NominalRepo nominalRepo;

    @Autowired
    MaterialRepo materialRepo;

    @Autowired
    CoinageRepo coinageRepo;

    @Autowired
    LoadFromBankPageService lfbp;

    @Value("${upload.path}")
    private String uploadPath;


    public File multipartFileToFile(MultipartFile multipart, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    public List<Coin> addCoinFromFile(MultipartFile file) {
        List<Coin> coins = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(multipartFileToFile(file, file.getName()))) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                Coin coin = new Coin();
                if (row.getCell(0) == null) break;
                String catalogNumber = row.getCell(0).toString().trim();
                coin.setCatalogNumber(catalogNumber);
                coin.setReleaseDate(row.getCell(1).getDateCellValue());
                coin.setName(row.getCell(2).toString().trim().replaceAll("[&<>; a-z/]", " "));
                if (row.getCell(3) != null) {
                    String seriesFromFile = row.getCell(3).toString().trim();
                    coinAttributeService.addSeries(seriesFromFile, null, null);
                    coin.setSeries(seriesRepo.findBySeriesName(seriesFromFile));
                }
                if (row.getCell(4) != null) {
                    String nominalFromFile = row.getCell(4).toString().trim();
                    coinAttributeService.addNominal(nominalFromFile);
                    coin.setNominal(nominalRepo.findByNominal(nominalFromFile));
                }
                if (row.getCell(5) != null) {
                    String materialFromFile = row.getCell(5).toString().trim();
                    coinAttributeService.addMaterial(materialFromFile);
                    coin.setMaterial(materialRepo.findByMaterial(materialFromFile));
                }
                String url = String.format("https://cbr.ru/cash_circulation/memorable_coins/coins_base/ShowCoins/?cat_num=%s", catalogNumber);
                Document doc = lfbp.getJsoupDocument(url);
                String str = lfbp.getCoinDescription(doc);
                lfbp.addAdditionParameters("", coin, doc, str, catalogNumber);
                coins.add(coin);
                coinRepo.save(coin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return coins;
    }
}
