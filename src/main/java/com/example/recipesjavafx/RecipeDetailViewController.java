package com.example.recipesjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.SQLException;

public class RecipeDetailViewController {
    public Label recipeNameDisplay;
    public Label timeDisplay;
    public ListView<Ingredient> ingredientsListView;
    public ListView<Utensil> utensilsListView;
    public ListView<Step> stepListView;
    private Recipe recipe;

    public RecipeDetailViewController(Recipe recipe){
        this.recipe = recipe;
    }

    @FXML
    public void initData(Recipe selectedRecipe) {
        System.out.println(recipe);
        this.recipe = selectedRecipe;
    }

    public void initialize() throws SQLException, ClassNotFoundException {
        recipeNameDisplay.setText(recipe.name());
        timeDisplay.setText(recipe.approxTime());
        initializeListViews();
    }

    private void initializeListViews() {
        ingredientsListView.setCellFactory(new IngredientCellFactory());
        utensilsListView.setCellFactory(new UtensilCellFactory());
        stepListView.setCellFactory(new StepCellFactory());
        ingredientsListView.getItems().addAll(recipe.ingredients());
        utensilsListView.getItems().addAll(recipe.utensils());
        stepListView.getItems().addAll(recipe.steps());
    }

}
