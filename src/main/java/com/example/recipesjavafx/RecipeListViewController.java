package com.example.recipesjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeListViewController {
    @FXML
    private ListView<Recipe> recipeListView;
    private final ArrayList<Recipe> recipes = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        initializeListView();
        RecipeDataBase recipeDataBase = new RecipeDataBase();
    }

    private void getRecipesFromDB() throws SQLException, ClassNotFoundException {
        RecipeDataBase recipeDataBase = new RecipeDataBase();
    }

    private void initializeListView() throws SQLException, ClassNotFoundException {
        RecipeDataBase recipeDataBase = new RecipeDataBase();
        ArrayList<Step> steps = new ArrayList<Step>();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<Utensil> utensils = new ArrayList<>();
        ObservableList<Recipe> recipesObservable = FXCollections.observableArrayList();
        recipesObservable.setAll(recipeDataBase.getAllRecipes());
        recipeListView.setItems(recipesObservable);
        recipeListView.setCellFactory(new RecipeCellFactory());
        }

    public void addNewRecipe(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        final FXMLLoader fxmlLoader = new FXMLLoader(RecipeListViewController.class.getResource("new_recipe_view.fxml"));
        final Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
    }

    public void openDetailView(Event event) throws IOException, SQLException, ClassNotFoundException {
        ListView<Recipe> listView = (ListView<Recipe>) event.getSource();
        Recipe selectedRecipe = listView.getSelectionModel().getSelectedItem();
        final FXMLLoader fxmlLoader = new FXMLLoader(RecipeListViewController.class.getResource("recipe_detail_view.fxml"));
        Stage detailStage = new Stage();
        RecipeDetailViewController controller = new RecipeDetailViewController(selectedRecipe);
        fxmlLoader.setController(controller);
        detailStage.setScene(new Scene(fxmlLoader.load()));
        detailStage.setTitle(selectedRecipe.name());
        detailStage.show();
    }

}
