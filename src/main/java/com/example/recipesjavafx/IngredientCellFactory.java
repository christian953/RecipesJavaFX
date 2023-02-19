package com.example.recipesjavafx;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class IngredientCellFactory implements Callback<ListView<Ingredient>, ListCell<Ingredient>> {
    @Override
    public ListCell<Ingredient> call(ListView<Ingredient> ingredientListView) {
        return new ListCell<Ingredient>() {
            @Override
            protected void updateItem(Ingredient ingredient, boolean empty) {
                super.updateItem(ingredient, empty);
                if (ingredient != null) {
                    setText(ingredient.amount() + " " + ingredient.name());
                } else {
                    setText("");
                }
            }
        };
    }
}
