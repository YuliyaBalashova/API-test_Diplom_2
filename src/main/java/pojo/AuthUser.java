package pojo;
// класс для сериализации тела запроса Авторизация пользователя
public class AuthUser {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthUser(){
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password +
                '}';
    }
}
