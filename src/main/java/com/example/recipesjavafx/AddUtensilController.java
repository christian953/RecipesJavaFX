package com.example.recipesjavafx;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddUtensilController {
    public TextField utensilNameInput;

    public void addUtensil(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        RecipeDataBase recipeDataBase = new RecipeDataBase();
        recipeDataBase.addUtensil(utensilNameInput.getText());
        Stage stage = (Stage) utensilNameInput.getScene().getWindow();
        stage.close();
    }
}
