package com.example.numismatist.services;

import com.example.numismatist.enteties.Coin;
import com.example.numismatist.enteties.Material;
import com.example.numismatist.enteties.Nominal;
import com.example.numismatist.enteties.Series;
import com.example.numismatist.repositories.CoinRepo;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Data
public class ReadXlsxService {

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private CoinAttributeService coinAttributeService;

    @Value("${upload.path}")
    private String uploadPath;
    public File multipartFileToFile(MultipartFile multipart, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    public List<Coin> readXlsxToList(MultipartFile file) {
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
                int amount = getCirculation(String.format("https://cbr.ru/cash_circulation/memorable_coins/coins_base/ShowCoins/?cat_num=%s", coin.getCatalogNumber()));
                coin.setCirculation(amount);

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

    public String deleteCharacter(String string) {
        List<Character> numbers = Arrays.asList('0', '1','2', '3', '4', '5', '6', '7', '8', '9');
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < string.length(); i++) {

            if (!numbers.contains(string.charAt(i))){
               stringBuilder.deleteCharAt(i);
            }
        }
        return stringBuilder.toString();
    }

    public int getCirculation(String url) {
        Document doc;
        String amount = "";
        try {
            doc = Jsoup.connect(url).get();
            String body = doc.text();
            System.out.println(body);
            String regexp = "шт\\W\\s[\\d+\\s{1}]+\\b";
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(body);
            int i = 0;
            while (matcher.find(i)) {
                amount = i == 0 ? matcher.group().substring(4) : amount + " / " + matcher.group().substring(4);
                i = matcher.end();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(deleteCharacter(amount));
    }

}
