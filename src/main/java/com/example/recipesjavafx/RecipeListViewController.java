package com.example.recipesjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeListViewController {
    @FXML
    private ListView<Recipe> recipeListView;
    private ArrayList<Recipe> recipes = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        initializeListView();
        RecipeDataBase recipeDataBase = new RecipeDataBase();
    }

    private void getRecipesFromDB() throws SQLException {
        RecipeDataBase recipeDataBase = new RecipeDataBase();
    }

    private void initializeListView() {
        ObservableList<Recipe> recipesObservable = FXCollections.observableArrayList();
        Recipe recipe = new Recipe(null, null, null, "hAloo,", "0", "hallo");
        recipesObservable.add(recipe);
        recipesObservable.setAll(this.recipes);
        recipeListView.setItems(recipesObservable);
        recipeListView.setCellFactory(new RecipeCellFactory());
        }
}
