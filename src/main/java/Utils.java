import java.util.Random;
import java.util.stream.Collectors;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.AuthUser;
import pojo.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class Utils {

    private Utils() {
        throw new IllegalAccessError("Utility class");
    }

    // GET без токена (Response) (3)
    public static Response doGetResp(String endpoint) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .get(endpoint);
    }

    // GET c токеном (1)
    public static ValidatableResponse doGet(String endpoint, String token) {
        return given()
                .log().all()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .get(endpoint)
                .then()
                .log().all();
    }

    // POST тело - строка, с токеном (2)
    public static ValidatableResponse doPost(String endpoint, String body, String token) {
        return given()
                .log().all()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all();
    }

    // POST тело - объект, без токена (18)
    public static ValidatableResponse doPost(String endpoint, Object body) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all();
    }

    // POST тело - строка, без токена (2)
    public static ValidatableResponse doPost(String endpoint, String body) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all();
    }

    // PATCH с токеном (Response) (4)
    public static Response doPatchResp (String endpoint, String body, String token) {
        return given()
                .log().all()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .patch(endpoint);
    }

    // PATCH без токена (3)
    public static ValidatableResponse doPatch(String endpoint, String body) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .log().all();
    }

    @Step("Создаем пользователя")
    public static void createUser(User user) {
        //отправляем запрос на создание пользователя
        doPost(Constants.CREATE_USER, user)
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true), "user", notNullValue(), "accessToken", notNullValue(), "refreshToken", notNullValue());
    }

    @Step("Удаляем пользователя")
    public static void deleteUser(User user) {
        if (user != null) {
            AuthUser authUser = new AuthUser(user.getEmail(), user.getPassword());
            String token = Utils.doPost(Constants.USER_AUTH, authUser)
                    .extract().body().jsonPath().get("accessToken");
            given()
                    .log().all()
                    .auth().oauth2(token.substring(7))
                    .header("Content-type", "application/json")
                    .delete(Constants.DELETE_USER)
                    .then()
                    .log().all();
        }
    }

    /**
     * Получение рандомной строки
     * @param size - длина строки
     * @return - рандомная строка
     */
    public static String getRandomString(long size) {
        String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return new Random().ints(size, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
