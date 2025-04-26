package com.example.demo.service;

import com.example.demo.model.ScrapModel;
import com.example.demo.repository.ScrapRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScrapService {
    @Autowired
    private ScrapRepository scrapRepository;

    private WebDriver driver;

    public List<String> getInfo(String query) {
        List<String> results = new ArrayList<>();
        try {
            List<WebElement> elements = getElements();

            System.out.println("Scraping elements...");
            for (WebElement element : elements) {
                String title = element.findElement(By.tagName("h3")).getText();
                String link = element.getAttribute("href");
                String image = "";
                try {
                    image = element.findElement(By.tagName("img")).getAttribute("src");
                } catch (Exception e) {

                }

                if (title.toLowerCase().contains(query.toLowerCase())) {
                    results.add(title);

                    ScrapModel scrapModel = new ScrapModel();
                    scrapModel.setTitle(title);
                    scrapModel.setSource(driver.getCurrentUrl());
                    scrapModel.setLink(link);
                    scrapModel.setImage(image);

                    boolean exists = scrapRepository.existsByTitle(title);
                    if(!exists) {
                        scrapRepository.save(scrapModel);
                    }

                    System.out.println("Scrap model successfully saved.");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        finally {
            driver.close();
        }

        System.out.println("Results: " + results);
        return results;
    }


    public List<WebElement> getElements() {
        List<WebElement> elementList = new ArrayList<>();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(ChromeDriverService.createDefaultService(), options);
        try {
            driver.get("https://www.onet.pl");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cmp-intro_acceptAll")));
            WebElement modal = driver.findElement(By.className("cmp-intro_acceptAll"));
            modal.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a h3")));

            System.out.println("before");
            List<WebElement> elements = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("a:has(h3)"))
            );
            System.out.println("after");

            elementList.addAll(elements);
        } catch (Exception e) {

        } finally {

        }
        return elementList;
    }

    public List<ScrapModel> getAll() {
        List data = new ArrayList<>();

        try {
            data = scrapRepository.findAll();
        } catch (Exception e) {
            System.err.println(e);
        }
        return data;
    }

    public String removeInDb(String title) {
        Optional<ScrapModel> scrapModelOpt = scrapRepository.findByTitle(title);

        if (scrapModelOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ScrapModel not found with title: " + title);
        }

        scrapRepository.delete(scrapModelOpt.get());
        return "Usunięto pomyślnie";
    }
}
