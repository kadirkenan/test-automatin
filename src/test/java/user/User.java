package user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class User {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;

    public void getUser(String userKey) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        Object object = parser.parse(new FileReader("src/test/resources/config/users.json"));
        JsonObject jsonObject = (JsonObject) object;
        JsonObject returnObject = (JsonObject) jsonObject.get(userKey);
        setUsername(returnObject.get("username").toString());
        setPassword(returnObject.get("passowrd").toString());
    }
}
