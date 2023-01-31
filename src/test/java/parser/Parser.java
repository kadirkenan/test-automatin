package parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private static Logger logger = LoggerFactory.getLogger(Parser.class);
    JsonParser parser = new JsonParser();
    JsonObject jsonObject;
    private static String variableKey;

    public String getVariableKey() {
        return variableKey;
    }

    public void setVariableKey(String variableKey) {
        System.out.println("Variable Key : " + variableKey);
        this.variableKey = variableKey;
    }

    public Object getJsonObject(String configFile) throws FileNotFoundException {
        return parser.parse((new FileReader("src/test/resources/config/" + configFile + ".json")));
    }

    public String getVariable(String configFileName, String configName) throws FileNotFoundException {
        jsonObject = (JsonObject) getJsonObject(configFileName);
        JsonObject returnObject = (JsonObject) jsonObject.get(variableKey);
        System.out.println("Object : " + returnObject.get(configName).toString());
        return evaluateQuotedString(returnObject.get(configName).toString());
    }

    public String getConfigPath(String configFileName, String configPathName, String configName) throws IOException {
        jsonObject = (JsonObject) getJsonObject(configFileName);
        JsonObject returnObject = (JsonObject) jsonObject.get(variableKey);
        JsonObject newObject = (JsonObject) returnObject.get(configPathName);
        return evaluateQuotedString(newObject.get(configName).toString());
    }

    public String evaluateQuotedString(String string) {
        return string.substring(1, string.length() - 1);
    }
}
