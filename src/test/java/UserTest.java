import io.restassured.RestAssured;
import org.junit.*;
import pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private  static User user;

    @Test
    public void creatingUserTest() {
        //создаем нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);

        //отправляем запрос на создание пользователя
        Utils.createUser(user);
    }

    @Test
    public void creatingIdenticalUserTest() {
        //создаем нового пользователя
        String randomString = Utils.getRandomString(8);
        String randomEmail = Utils.getRandomString(8) + "@" + Utils.getRandomString(6) + "." + Utils.getRandomString(3);
        user = new User(randomEmail, randomString, randomString);

        //отправляем запрос на создание пользователя
        Utils.createUser(user);

        //отправляем повторный запрос на создание курьера
        Utils.doPost(Constants.CREATE_USER, user)
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"User already exists\"}"));
    }

    @Test
    public void creatingUserWithEmptyEmailTest() {
        //создаем нового пользователя
        String randomString = Utils.getRandomString(8);
        user = new User("", randomString, randomString);
        //создание пользователя с пустым полем email
        Utils.doPost(Constants.CREATE_USER, user)
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"Email, password and name are required fields\"}"));
        user = null;
    }

    @Test
    public void creatingUserWithEmptyPasswordTest() {
        //создаем нового пользователя
        String randomString = Utils.getRandomString(8);
        user = new User(randomString, "", randomString);
        //создание пользователя с пустым полем password
        Utils.doPost(Constants.CREATE_USER, user)
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"Email, password and name are required fields\"}"));
        user = null;
    }

    @Test
    public void creatingUserWithEmptyNameTest() {
        //создаем нового пользователя
        String randomString = Utils.getRandomString(8);
        user = new User(randomString, randomString, "");
        //создание пользователя с пустым полем name
        Utils.doPost(Constants.CREATE_USER, user)
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo("{\"success\":false,\"message\":\"Email, password and name are required fields\"}"));
        user = null;
    }

    @After
    public void deleteUser() {
        Utils.deleteUser(user);
    }
}
