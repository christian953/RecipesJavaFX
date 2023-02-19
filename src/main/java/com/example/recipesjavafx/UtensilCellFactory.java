package com.example.recipesjavafx;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class UtensilCellFactory implements Callback<ListView<Utensil>, ListCell<Utensil>> {

    @Override
    public ListCell<Utensil> call(ListView<Utensil> utensilListView) {
        return new ListCell<Utensil>() {
            @Override
            protected void updateItem(Utensil utensil, boolean empty) {
                super.updateItem(utensil, empty);
                if (utensil != null) {
                    setText(utensil.name());
                } else {
                    setText("");
                }
            }
            };
    }
}
