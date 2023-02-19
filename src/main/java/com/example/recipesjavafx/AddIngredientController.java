package com.example.recipesjavafx;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddIngredientController {
    public TextField ingredientNameInput;

    public void addIngredient(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        RecipeDataBase recipeDataBase = new RecipeDataBase();
        recipeDataBase.addIngredient(ingredientNameInput.getText());
        Stage stage = (Stage) ingredientNameInput.getScene().getWindow();
        stage.close();
    }
}
