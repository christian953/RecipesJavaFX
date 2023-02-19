package com.example.recipesjavafx;

import com.example.recipesjavafx.Recipe;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class RecipeCellFactory implements Callback<ListView<Recipe>, ListCell<Recipe>> {

    @Override
    public ListCell<Recipe> call(ListView<Recipe> listView) {
        return new ListCell<Recipe>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty) {
                super.updateItem(recipe, empty);
                if (recipe != null) {
                    setText(recipe.name());
                } else {
                    setText("");
                }
            }
        };
    }
}
