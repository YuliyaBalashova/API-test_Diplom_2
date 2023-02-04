import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static User user;
    // Получение заказов авторизованного пользователя
    @Test
    public void getOrdersAuthUserTest() {
        // создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        // получение заказов
        Utils.doGet(Constants.GET_ORDERS, token.substring(7))
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true), "orders", notNullValue());
    }

    // Получение заказов неавторизованного пользователя
    @Test
    public void getOrdersNotAuthUserTest() {
        // получение заказов
        Utils.doGet(Constants.GET_ORDERS)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"You should be authorised\"}"));
    }

    @After
    public void deleteUser() {
        Utils.deleteUser(user);
    }
}