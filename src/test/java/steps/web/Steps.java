package steps.web;


import context.ScenarioContext;
import io.cucumber.core.api.Scenario;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.Parser;
import user.User;
import utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Steps {
    DriverManager driverManager = new DriverManager();
    public static WebDriver driver = null;
    public static Utils utils;
    Parser parser = new Parser();
    ScenarioContext scenarioContext = new ScenarioContext();
    User user = new User();
    Actions actions;
    private static Logger logger = LoggerFactory.getLogger(Steps.class);

    @Given("I open {string} browser")
    public void openBrowser(String driverType) throws IOException {
        driver = driverManager.getDriver(driverType);
        utils = new Utils(driver);
    }

    @And("I open {string} page")
    public void openPage(String pageKey) throws FileNotFoundException {
        parser.setVariableKey(pageKey);
        driver.get(parser.getVariable("pages", "urlKey"));
    }

    @And("I fill:")
    public void fillDataMap(Map<String, String> dataMap) throws IOException {
        for (Map.Entry<String, String> item : dataMap.entrySet()) {
            if (scenarioContext.isContains(item.getValue()))
                utils.fillItem(item.getKey(), scenarioContext.getContext(item.getValue()));
            else
                utils.fillItem(item.getKey(), item.getValue());
        }
    }

    @And("I click (\\w+(?: \\w+)*)")
    public void clickElement(String elementKey) throws IOException {
        utils.getElement(elementKey).click();
    }

    @And("I click {string}")
    public void clickText(String text) {
        driver.findElement(By.xpath("//*[text()='" + text + "']")).click();
    }

    @And("I wait for {int} seconds")
    public void waitNSeconds(int seconds) throws InterruptedException {
        long SECOND_AS_MILLIS = 1000L;
        Thread.sleep(seconds * SECOND_AS_MILLIS);
    }

    @And("I see {string} text")
    public void seeText(String text) {
        if (driver.findElements(By.xpath("//*[text()='" + text + "']")).size() != 0)
            logger.info("See text method runned with : " + text);
        else
            logger.error("See text method not found the " + text);
    }

    @And("I see {string} element")
    public void seeElement(String elementKey) throws IOException {
        if (utils.getElement(elementKey) != null)
            logger.info("See element method runned with : " + elementKey);
        else
            logger.error("See element method not found the " + elementKey);
    }

    @And("I select {string} as {string}")
    public void selectList(String elementKey, String elementValue) throws IOException {
        Select select = new Select(utils.getElement(elementKey));
        select.selectByValue(elementValue);
    }

    @And("I select {string} radio button as the {string}")
    public void selectRadioButton(String elementKey, String elementValue) throws IOException {
        List<WebElement> webElements = utils.findRadioCheckboxElement(elementKey);
        for (WebElement element : webElements) {
            if (element.getAttribute("value").equals(elementValue))
                element.click();
            else
                logger.error("Not found radio button");
        }
    }

    @And("I login with {string}")
    public void loginWithUserKey(String userKey) throws FileNotFoundException {
        user.getUser(userKey);
        //Login steps
    }

    @And("I double click {string}")
    public void doubleClickElement(String elementKey) throws IOException {
        actions = new Actions(driver);
        actions.doubleClick(utils.getElement(elementKey));
    }

    @And("I right click {string}")
    public void rightClickElement(String elementKey) throws IOException {
        actions = new Actions(driver);
        actions.contextClick(utils.getElement(elementKey));
    }

    @And("I execute {string} script")
    public void executeScript(String scriptName) throws FileNotFoundException {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeAsyncScript(parser.getVariable("scripts", scriptName));
    }

    @And("I accept alert")
    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    @And("I decline alert")
    public void declineAlert() {
        driver.switchTo().alert().dismiss();
    }


    @And("I verify the {string} table:")
    public void verifyTable(String elementKey, DataTable dataTable) throws IOException {
        WebElement tableElement = utils.getElement(elementKey);
        List<WebElement> tableHeads = tableElement.findElements(By.xpath("thead/tr/th"));
        for (int i = 0; i < tableHeads.size(); i++) {
            Assert.assertEquals(dataTable.row(0).get(i).toString(), tableHeads.get(i).getText());
        }
        List<WebElement> tableRows = tableElement.findElements(By.xpath("tbody/tr"));
        for (int i = 0; i < tableRows.size(); i++) {
            List<WebElement> tableCells = tableRows.get(i).findElements(By.xpath("td"));
            for (int j = 0; j < tableCells.size(); j++) {
                Assert.assertEquals(tableCells.get(j).getText(), dataTable.row(i + 1).get(j));
            }
        }
    }

    @And("I save \"([^\"]*)\" text as \"([^\"]*)\"")
    public void saveElement(String elementKey, String variableKey) throws IOException {
        scenarioContext.setContext(variableKey, utils.getText(elementKey));
    }

    @After
    public void tearDown(Scenario scenario) {
        String[] paths = scenario.getUri().split("/");
        if (scenario.isFailed()) {
            takeScreenShot(scenario);
        }
        try {
            driver.close();
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
    }

    private void takeScreenShot(Scenario scenario) {
        final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }
}
