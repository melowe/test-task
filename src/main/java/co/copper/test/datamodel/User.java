package co.copper.test.datamodel;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String firstName;

    private String lastName;

    private String email;

    private String password;


    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("firstName",firstName)
                .add("lastName",lastName)
                .add("email",email)
                .add("password",password)
                .build();
    }

}
