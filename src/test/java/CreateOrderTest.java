import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Ingredient;
import pojo.IngredientsForOrder;
import pojo.User;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static User user;
    private static IngredientsForOrder ingredientsForOrder;

    // Создание заказа с ингредиантами авторизованным пользователем
    @Test
    @DisplayName("Creating an order with ingredients by a user with authorization") // имя теста
    public void createOrderWithIngredientAuthUserTest() {
        // создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        // получение хэша ингредиентов
        Response response = Utils.doGetResp(Constants.GET_INGREDIENT);
        // десериализация тела ответа в класс ChangeUser
        Ingredient ingredient = response.body().as(Ingredient.class);
        //создание заказа
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getData().get(1).getId());
        ingredients.add(ingredient.getData().get(2).getId());
        ingredientsForOrder = new IngredientsForOrder(ingredients);
        Utils.doPost(Constants.CREATE_ORDER, ingredientsForOrder, token.substring(7))
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true), "name", notNullValue(), "order", notNullValue());
    }

    // Создание заказа без ингредиантов авторизованным пользователем
    @Test
    @DisplayName("Creating an order without ingredients by a user with authorization") // имя теста
    public void createOrderWithoutIngredientAuthUserTest() {
        // создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        //создание заказа без ингредиентов
        given()
                .log().all()
                .auth().oauth2(token.substring(7))
                .header("Content-type", "application/json")
                .when()
                .post(Constants.CREATE_ORDER)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"Ingredient ids must be provided\"}"));
    }

    // Создание заказа с несуществующими ингредиантами авторизованным пользователем
    @Test
    @DisplayName("Creating an order with non-existent ingredients by a user with authorization") // имя теста
    public void createOrderWithNotIsIngredientAuthUserTest() {
        // создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        //создание заказа
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(randomString);
        ingredients.add(randomString);
        ingredientsForOrder = new IngredientsForOrder(ingredients);
        Utils.doPost(Constants.CREATE_ORDER, ingredientsForOrder, token.substring(7))
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    // Создание заказа с ингредиантами неавторизованным пользователем
    @Test
    @DisplayName("Creating an order with ingredients by a user without authorization") // имя теста
    public void createOrderWithIngredientNotAuthUserTest() {
        // получение хэша ингредиентов
        Response response = Utils.doGetResp(Constants.GET_INGREDIENT);
        // десериализация тела ответа в класс ChangeUser
        Ingredient ingredient = response.body().as(Ingredient.class);
        //создание заказа
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getData().get(4).getId());
        ingredients.add(ingredient.getData().get(5).getId());
        ingredientsForOrder = new IngredientsForOrder(ingredients);
        Utils.doPost(Constants.CREATE_ORDER, ingredientsForOrder)
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true), "name", notNullValue(), "order", notNullValue());
        user = null;
    }

    // Создание заказа без ингредиантов неавторизованным пользователем
    @Test
    @DisplayName("Creating an order without ingredients by a user without authorization") // имя теста
    public void createOrderWithoutIngredientNotAuthUserTest() {
        //создание заказа
        given()
                .log().all()
                .header("Content-type", "application/json")
                .when()
                .post(Constants.CREATE_ORDER)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"Ingredient ids must be provided\"}"));
        user = null;
    }

    // Создание заказа с несуществующими ингредиантами неавторизованным пользователем
    @Test
    @DisplayName("Creating an order with non-existent ingredients by a user without authorization") // имя теста
    public void createOrderWithNotIsIngredientNotAuthUserTest() {
        String randomString = Utils.getRandomString(8);
        //создание заказа
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(randomString);
        ingredients.add(randomString);
        ingredientsForOrder = new IngredientsForOrder(ingredients);
        Utils.doPost(Constants.CREATE_ORDER, ingredientsForOrder)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
        user = null;
    }

    @After
    public void deleteUser() {
        Utils.deleteUser(user);
    }
}