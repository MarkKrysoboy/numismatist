package com.example.numismatist.services;

import com.example.numismatist.enteties.*;
import com.example.numismatist.repositories.*;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Data
public class LoadFromBankPageService {


    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    CoinAttributeService coinAttributeService;

    @Autowired
    SeriesRepo seriesRepo;

    @Autowired
    NominalRepo nominalRepo;
    @Autowired
    MaterialRepo materialRepo;

    @Autowired
    CoinageRepo coinageRepo;

    @Autowired
    CoinRepo coinRepo;

    public Document getJsoupDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public String parseName(Document doc) {
        Elements elements = doc.getElementsByTag("h1");
        if (!elements.isEmpty()) {
            String name = elements.first().text();
            return name;
        } else {
            return null;
        }
    }

    public Series parseSeries(Document doc) {
        Elements elements = doc.getElementsByTag("a");
        Element elementSeries = elements.stream().filter(element -> element.text().contains("Серия:")).findFirst().orElse(null);
        if (elementSeries != null) {
            String elementText = elementSeries.text();
            String seriesName = elementText.substring(elementText.indexOf("Серия:") + 7);
            String linkToBankPage = elementSeries.attr("href").substring(2);
            int start = linkToBankPage.indexOf("serie_id=") + 9;
            int end = linkToBankPage.indexOf("&", start);
            Integer idInBank = Integer.parseInt(linkToBankPage.substring(start, end));
            coinAttributeService.addSeries(seriesName, linkToBankPage, idInBank);
            return seriesRepo.findBySeriesName(seriesName);
        } else {
            return null;
        }
    }

    public String getCoinDescription(Document doc) {
        Elements elements = doc.select("body > main ");
        return elements.first().text();
    }

    public int findUppercase(String str, int start) {
        int index = -1;
        for (int i = start; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                index = i;
                break;
            }
        }
        return index;
    }

    public String getText(String str, String text) {
        try {
            if (str.indexOf(text) > -1) {
                int start = str.indexOf(text) + text.length() + 1;
                int end = findUppercase(str, start) - 1;
                String outText = str.substring(start, end);
                System.out.println(outText);
                return outText;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Date getReleaseDate(String str) {
        String textDate = getText(str, "Дата выпуска");
        LocalDate date = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = LocalDate.parse(textDate, formatter);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        } catch (DateTimeParseException ex) {
            System.out.println(ex.getMessage());
        }
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getCatalogNumber(String str) {
        String catalogNumber = getText(str, "Каталожный номер");
        return catalogNumber;
    }

    public Nominal getNominal(String str) {
        String nominal = getText(str, "Номинал");
        coinAttributeService.addNominal(nominal);
        return nominalRepo.findByNominal(nominal);
    }

    public Material getMaterial(String str) {
        String material = null;
        if (str.contains("Сплав")) {
            String temp = str.substring(str.indexOf("Сплав") + 6, str.indexOf("Масса общая"));
            material = temp.length() > 0 ? temp : null;
        }
        if (str.contains("Металл, проба")) {
            material = getText(str, "Металл, проба");
        }
        if (str.contains("Материал")) {
            material = getText(str, "Материал");
        }
        coinAttributeService.addMaterial(material);
        return materialRepo.findByMaterial(material);
    }

    public String getWeight(String str) {
        String weight = getText(str, "Масса общая, г");
        return weight;
    }

    public String getDiameter(String str) {
        String diameter = getText(str, "Диаметр, мм");
        return diameter;
    }

    public String getLength(String str) {
        String length = getText(str, "Длина, мм");
        return length;
    }

    public String getWidth(String str) {
        String width = getText(str, "Ширина, мм");
        return width;
    }

    public String getThickness(String str) {
        String thickness = getText(str, "Толщина, мм");
        return thickness;
    }

    public String getCirculation(String str) {
        String circulation = getText(str, "Тираж, шт.");
        return circulation;
    }


    public String getObverse(String str) {
        String obverse = str.substring(str.indexOf("Аверс") + 6, str.indexOf("Реверс"));
        return obverse.length() > 0 ? obverse : null;
    }

    public String getReverse(String str) {
        String reverse = str.substring(str.indexOf("Реверс") + 7, str.indexOf("Авторы"));
        return reverse.length() > 0 ? reverse : null;
    }

    public String getAuthors(String str) {
        String authors = str.substring(str.indexOf("Авторы") + 7, str.indexOf("Чеканка:"));
        return authors.length() > 0 ? authors : null;
    }

    public Coinage getCoinage(String str) {
        String coinage = str.substring(str.indexOf("Чеканка:") + 9, str.indexOf("Оформление гурта:"));
        coinAttributeService.addCoinage(coinage);
        return coinageRepo.findByCoinage(coinage);
    }

    public String getDesignHerd(String str) {
        String designHerd = str.substring(str.indexOf("Оформление гурта:") + 18, str.indexOf("Историко-тематическая справка"));
        return designHerd.length() > 0 ? designHerd : null;
    }

    public String getHistoricalReference(String str) {
        String historicalReference = str.substring(str.indexOf("Историко-тематическая справка") + 30, str.indexOf("Страница была полезной?"));
        return historicalReference.length() > 0 ? historicalReference : null;
    }

    public String getPureMetal(String str) {
        String pureMetal = getText(str, "Содержание химически чистого металла не менее, г");
        return pureMetal;
    }

    public String getQuality(String str) {
        int start = str.indexOf("Качество") + 9;
        String quality = str.substring(start, str.indexOf(" ", start));
        return quality.length() > 0 ? quality : null;
    }

    public void saveImages(Document doc, String catalogNumber) {
        Elements elements = doc.getElementsByTag("img");
        String obverseRegex = ".*\\d{4}(-)\\d{4}(-)*\\d*(.)[a-z]+";
        String reverseRegex = ".*\\d{4}(-)\\d{4}(-)*\\d*(r.)[a-z]+";
        String obverseImgUrl = null;
        String reverseImgUrl = null;
        for (Element url : elements) {
            String urlSrc = url.attr("src");
            if (Pattern.matches(obverseRegex, urlSrc)) obverseImgUrl = urlSrc;
            if (Pattern.matches(reverseRegex, urlSrc)) reverseImgUrl = urlSrc;
        }
        try {
            byte[] obverseByte = downloadFile(String.format("https://cbr.ru" + obverseImgUrl));
            byte[] reverseByte = downloadFile(String.format("https://cbr.ru" + reverseImgUrl));
            saveFile(obverseByte, catalogNumber);
            saveFile(reverseByte, catalogNumber + "r");
        } catch (Exception e) {

        }
    }

    public boolean isInvestment(String url) {
        return (Pattern.matches(".*\\d{4}(-)\\d{4}(-)\\d{2}", url));
    }

    private byte[] downloadFile(String Url) {
        try (InputStream inputStream = new URL(Url).openStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveFile(byte[] bytes, String name) {
        try (FileOutputStream fos = new FileOutputStream(uploadPath + "/" + name + ".jpg")) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Coin> addCoinFromBankPage(String url) throws ParseException {
        List<Coin> coinList = new ArrayList<>();
        Coin coin = new Coin();
        Document doc = getJsoupDocument(url);
        String str = getCoinDescription(doc);
        String catalogNumber = getCatalogNumber(str);
        if (coinRepo.findByCatalogNumber(catalogNumber) == null) {
            coin.setName(parseName(doc));
            coin.setSeries(parseSeries(doc));
            coin.setNominal(getNominal(str));
            coin.setMaterial(getMaterial(str));
            coin.setReleaseDate(getReleaseDate(str));
            coin.setCatalogNumber(catalogNumber);
            addAdditionParameters(url, coin, doc, str, catalogNumber);
            coinList.add(coin);
            coinRepo.save(coin);
        }
        return coinList;
    }

    public void addAdditionParameters(String url, Coin coin, Document doc, String str, String catalogNumber) {
        coin.setCirculation(getCirculation(str));
        coin.setWeight(getWeight(str));
        coin.setDiameter(getDiameter(str));
        coin.setLength(getLength(str));
        coin.setWidth(getWidth(str));
        coin.setThickness(getThickness(str));
        coin.setPureMetal(getPureMetal(str));
        coin.setQuality(getQuality(str));
        coin.setAuthors(getAuthors(str));
        coin.setCoinage(getCoinage(str));
        coin.setDesignHerd(getDesignHerd(str));
        coin.setHistoricalReference(getHistoricalReference(str));
        coin.setObverse(getObverse(str));
        coin.setReverse(getReverse(str));
        coin.setInvestment(isInvestment(url));
        coin.setLinkToBankPage(String.format("https://cbr.ru/cash_circulation/memorable_coins/coins_base/ShowCoins/?cat_num=%s", catalogNumber));
        saveImages(doc, catalogNumber);
    }

    public List<Coin> addCoinsFromBankSite(String year) throws ParseException {
        List<Coin> coinsList = new ArrayList<>();
        String urlYear = String.format("https://cbr.ru/cash_circulation/memorable_coins/coins_base/?" +
                "UniDbQuery.Posted=True&UniDbQuery.SearchPhrase=&UniDbQuery.year=%s&" +
                "UniDbQuery.serie_id=0&UniDbQuery.nominal=-1&UniDbQuery.metal_id=0&UniDbQuery.tab=1&" +
                "UniDbQuery.page=1&UniDbQuery.sort=99&UniDbQuery.sort_direction=down", year);
        Document doc = getJsoupDocument(urlYear);
        Elements elements = doc.getElementsByTag("a");
        for (Element yearsLink : elements) {
            String coinLink = yearsLink.attr("href");
            if (Pattern.matches(".*\\d{4}(-)\\d{4}((-)\\d{2})*", coinLink)) {
                String urlCoin = "https://cbr.ru/cash_circulation/memorable_coins/coins_base/" + coinLink;
                coinsList.addAll(addCoinFromBankPage(urlCoin));
            }
        }
        return coinsList;
    }
}
