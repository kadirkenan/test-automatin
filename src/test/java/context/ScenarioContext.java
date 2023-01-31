package context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    public static Map<String, String> scenarioContext = new HashMap<String, String>();

    public void setContext(String key, String value) {
        scenarioContext.put(key, value);
    }

    public String getContext(String key) {
        return scenarioContext.get(key);
    }

    public boolean isContains(String key) {
        return scenarioContext.containsKey(key);
    }
}
