package pojo;

import java.util.ArrayList;

// класс для сериализации тела запроса Создание заказа
public class IngredientsForOrder {
private ArrayList<String> ingredients;

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsForOrder(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
