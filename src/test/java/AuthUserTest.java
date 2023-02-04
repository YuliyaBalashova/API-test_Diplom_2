import io.restassured.RestAssured;
import org.junit.*;
import pojo.AuthUser;
import pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class AuthUserTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private  static User user;

    //авторизация под существующим пользователем
    @Test
    public void authUserTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        //авторизация пользователя
        AuthUser authUser = new AuthUser(randomEmail, randomString);
        Utils.doPost(Constants.USER_AUTH, authUser)
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true), "accessToken", notNullValue(), "refreshToken", notNullValue(), "user", notNullValue());

    }

    //авторизация пользователя c неверным email
    @Test
    public void authUserWithWrongEmailTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        //авторизация пользователя
        String wrongEmail = Utils.getRandomString(5) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        AuthUser authUser = new AuthUser(wrongEmail, randomString);
        Utils.doPost(Constants.USER_AUTH, authUser)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"email or password are incorrect\"}"));
    }

    //авторизация пользователя c неверным password
    @Test
    public void authUserWithWrongPasswordTest() {
        //создание нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);
        Utils.doPost(Constants.CREATE_USER, user);

        //авторизация пользователя
        String wrongPassword = Utils.getRandomString(6);
        AuthUser authUser = new AuthUser(randomEmail, wrongPassword);
        Utils.doPost(Constants.USER_AUTH, authUser)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"email or password are incorrect\"}"));
    }

    @After
    public void deleteUser() {
        Utils.deleteUser(user);
    }
}
