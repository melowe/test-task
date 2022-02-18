package co.copper.test.services;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import co.copper.test.datamodel.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.*;
import org.asynchttpclient.AsyncHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.copper.test.storage.TestJavaRepository;


@Service
public class TestJavaService {

    private static final Logger log = LoggerFactory.getLogger(TestJavaService.class);
    private final TestJavaRepository testRepo;
    private final AsyncHttpClient httpClient;

    @Autowired
    public TestJavaService(TestJavaRepository testRepo, AsyncHttpClient httpClient) {
        this.testRepo = testRepo;
        this.httpClient = httpClient;
    }

    private static String toJson(List<User> users) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        users.stream()
                .map(User::toJson)
                .forEach(jsonArrayBuilder::add);
        try (StringWriter sw = new StringWriter()) {
            JsonWriter jsonWriter = Json.createWriter(sw);
            jsonWriter.write(
                    Json.createObjectBuilder().
                            add("users", jsonArrayBuilder)
                            .build());
            return sw.toString();
        } catch (IOException ioException) {
            throw new UncheckedIOException(ioException);
        }
    }

    private static User from(JsonObject json) {
        JsonObject credentials = json.getJsonObject("name");
        String firstName = credentials.getString("first");
        String lastName = credentials.getString("last");

        String email = json.getString("email");
        String password = json.getJsonObject("login").getString("password");

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email).password(password)
                .build();
    }

    public CompletableFuture<String> getOk() {
        log.debug(testRepo.getById(1L).get(0).getVal());
        return this.httpClient.prepareGet("https://postman-echo.com/get").execute().toCompletableFuture()
                .handle((res, t) -> res.getResponseBody());
    }

    private String loadData() {
        try {
            return this.httpClient.prepareGet("https://randomuser.me/api?results=20")
                    .execute().toCompletableFuture()
                    .handle((res, t) -> res.getResponseBody())
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> queryAndSave() {

        try (StringReader reader = new StringReader(loadData())) {
            JsonObject json = Json.createReader(reader).readObject();
            JsonArray jsonResults = json.getJsonArray("results");
            List<User> users = jsonResults.stream()
                    .map(JsonValue::asJsonObject)
                    .map(TestJavaService::from)
                    .collect(Collectors.toList());

            users.forEach(testRepo::saveUser);
            return CompletableFuture.completedFuture(toJson(users));
        }

    }

}
