import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "io.qameta.allure.cucumber3jvm.AllureCucumber3Jvm"}, tags = "not @ignore")
public class RunCucumberTest {
}
