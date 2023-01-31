package steps.web;

import config.ConfigProperties;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.Parser;

import java.io.FileNotFoundException;

public class DriverManager {
    private Logger logger = LoggerFactory.getLogger(DriverManager.class);
    WebDriver driver;
    ConfigProperties configProperties = new ConfigProperties();

    public WebDriver getDriver(String driverType) throws FileNotFoundException {
        String headless = configProperties.getProperties("headless");
        if (driverType.toLowerCase().equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            if (headless.equals("true")) {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                driver = new ChromeDriver(chromeOptions);
            } else {
                driver = new ChromeDriver();
            }
        } else if (driverType.toLowerCase().equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            if (headless.equals("true")) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);
                driver = new FirefoxDriver();
            } else {
                driver = new FirefoxDriver();
            }
        } else {
            logger.error("Unsupported Browser Type");
        }
        return driver;
    }
}
