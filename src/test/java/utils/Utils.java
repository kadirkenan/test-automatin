package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.Parser;

import java.io.IOException;
import java.util.List;

public class Utils {
    WebDriver driver;
    Parser parser = new Parser();
    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public Utils(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getElement(String elementKey) throws IOException {
        By selector = bySelector(parser.getConfigPath("pages", "elements", elementKey));
        return driver.findElement(selector);
    }

    public void fillItem(String key, String value) throws IOException {
        clearInput(key);
        getElement(key).sendKeys(value);
    }

    public void clearInput(String elementKey) throws IOException {
        getElement(elementKey).clear();
    }

    public List findRadioCheckboxElement(String elementKey) throws IOException {
        By selector = bySelector(parser.getConfigPath("pages", "elements", elementKey));
        return driver.findElements(selector);
    }

    public String getText(String elementKey) throws IOException {
        return getElement(elementKey).getText();
    }

    public By bySelector(String selector) {
        if (selector.matches("^#[\\w-]+$")) {
            return By.id(selector.substring(1));
        } else if (selector.charAt(0) == '/' || selector.charAt(0) == '(' || selector.startsWith("./")) {
            return By.xpath(selector);
        } else {
            return By.cssSelector(selector);
        }
    }
}
