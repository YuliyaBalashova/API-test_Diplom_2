package pojo;

import java.util.List;
// класс для десериализации тела ответа по GET-запросу Получение данных об ингредиентах
public class Ingredient {
    private String success;
    private List<DataIngredient> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<DataIngredient> getData() {
        return data;
    }

    public void setData(List<DataIngredient> data) {
        this.data = data;
    }
}
