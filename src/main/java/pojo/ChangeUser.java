package pojo;
// класс для десериализации тела ответа по PATCH-запросу Обновление данных о пользователе
public class ChangeUser {
    private boolean success;
    private UserDate user;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserDate getUser() {
        return user;
    }

    public void setUser(UserDate user) {
        this.user = user;
    }
}