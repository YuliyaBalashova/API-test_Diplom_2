import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.ChangeUser;
import pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static User user;

    // Изменение email пользователя с авторизацией
    @Test
    @DisplayName("Changing the user's email with authorization") // имя теста
    public void changeUserEmailTest() {
        // создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        // создание нового email
        String randomEmailNew = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        // изменение email
        Response response = Utils.doPatchResp(Constants.USER_CHANGE, "{\"email\":\"" + randomEmailNew + "\"}", token.substring(7));
        // проверка кода ответа
        response.then().statusCode(SC_OK);
        // десериализация тела ответа в класс ChangeUser
        ChangeUser changeUser = response.body().as(ChangeUser.class);
        // проверка тела ответа
        Assert.assertTrue(changeUser.isSuccess());
        Assert.assertEquals(changeUser.getUser().getEmail(), randomEmailNew.toLowerCase());
        Assert.assertEquals(changeUser.getUser().getName(), randomString);

        user = new User(randomEmailNew, randomString, randomString); //для метода удаления пользователя
    }

    // Изменение password пользователя с авторизацией
    @Test
    @DisplayName("Changing the user's password with authorization") // имя теста
    public void changeUserPasswordTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        // создание нового password
        String randomPasswordNew = Utils.getRandomString(7);
        //изменение password
        Response response = Utils.doPatchResp(Constants.USER_CHANGE, "{\"password\":\"" + randomPasswordNew + "\"}", token.substring(7));
        // проверка кода ответа
        response.then().statusCode(SC_OK);
        // десериализация тела ответа в класс ChangeUser
        ChangeUser changeUser = response.body().as(ChangeUser.class);
        // проверка тела ответа
        Assert.assertTrue(changeUser.isSuccess());
        Assert.assertEquals(changeUser.getUser().getEmail(), randomEmail.toLowerCase());
        Assert.assertEquals(changeUser.getUser().getName(), randomString);

        user = new User(randomEmail, randomPasswordNew, randomString); //для метода удаления пользователя
    }

    // Изменение name пользователя с авторизацией
    @Test
    @DisplayName("Changing the user's name with authorization") // имя теста
    public void changeUserNameTest() {
        // создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        // получение token
        String token = Utils.doPost(Constants.CREATE_USER, user)
                .extract().body().jsonPath().get("accessToken");
        // создание нового name
        String randomNameNew = Utils.getRandomString(7);
        //изменение name
        Response response = Utils.doPatchResp(Constants.USER_CHANGE, "{\"name\":\"" + randomNameNew + "\"}", token.substring(7));
        // проверка кода ответа
        response.then().statusCode(SC_OK);
        // десериализация тела ответа в класс ChangeUser
        ChangeUser changeUser = response.body().as(ChangeUser.class);
        // проверка тела ответа
        Assert.assertTrue(changeUser.isSuccess());
        Assert.assertEquals(changeUser.getUser().getEmail(), randomEmail.toLowerCase());
        Assert.assertEquals(changeUser.getUser().getName(), randomNameNew);

        user = new User(randomEmail, randomString, randomNameNew); //для метода удаления пользователя
    }

    //Изменение email на уже использующийся с авторизацией
    @Test
    @DisplayName("Changing the user's email to an existing email with authorization") // имя теста
    public void changeUserEmailUsingTest() {
        //создание нового пользователя1
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        //создание нового пользователя2
        String randomEmailNew = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmailNew, randomString, randomString);
        String token = Utils.doPost(Constants.CREATE_USER, user)   //получение token
                .extract().body().jsonPath().get("accessToken");

        //изменение email пользователя2 на email пользователя1
        Response response = Utils.doPatchResp(Constants.USER_CHANGE, "{\"email\":\"" + randomEmail + "\"}", token.substring(7));
        response
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"User with such email already exists\"}"));
    }

    //Изменение email пользователя без авторизации
    @Test
    @DisplayName("Changing the user's email without authorization") // имя теста
    public void changeUserEmailDefaultTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        String randomEmailNew = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);  //создание нового email

        //изменение email
        Utils.doPatch(Constants.USER_CHANGE, "{\"email\":\"" + randomEmailNew + "\"}")
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"You should be authorised\"}"));
    }

    //Изменение password пользователя без авторизации
    @Test
    @DisplayName("Changing the user's password without authorization") // имя теста
    public void changeUserPasswordDefaultTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        String randomPasswordNew = Utils.getRandomString(7);  //создание нового password

        //изменение password
        Utils.doPatch(Constants.USER_CHANGE, "{\"password\":\"" + randomPasswordNew + "\"}")
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"You should be authorised\"}"));
    }

    //Изменение name пользователя без авторизации
    @Test
    @DisplayName("Changing the user's name without authorization") // имя теста
    public void changeUserNameDefaultTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        String randomNameNew = Utils.getRandomString(7);  //создание нового name

        //изменение password
        Utils.doPatch(Constants.USER_CHANGE, "{\"name\":\"" + randomNameNew + "\"}")
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"You should be authorised\"}"));
    }

    @After
    public void deleteUser() {
        Utils.deleteUser(user);
    }
}