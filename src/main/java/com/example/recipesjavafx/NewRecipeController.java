package com.example.recipesjavafx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class NewRecipeController {

    private RecipeDataBase recipeDataBase;
    public ChoiceBox<String> ingredientChoiceBox;
    public ChoiceBox<String> utensilChoiceBox;
    public ListView<Ingredient> ingredientsListView;
    public ListView<Utensil> utensilsListView;
    public TextField ingredientInputField;
    public ListView<Step> stepListView;
    public TextField recipeNameInputTextArea;
    public TextField timeInput;
    private int numberOfSteps = 0;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        this.recipeDataBase = new RecipeDataBase();
        initializeListViews();
        initializeChoiceBoxes(recipeDataBase);
    }

    private void initializeChoiceBoxes(RecipeDataBase recipeDataBase) throws SQLException {
        utensilChoiceBox.getItems().addAll(recipeDataBase.getAllUtensils());
        ingredientChoiceBox.getItems().addAll(recipeDataBase.getAllIngredients());
    }

    private void initializeListViews() {
        ingredientsListView.setCellFactory(new IngredientCellFactory());
        utensilsListView.setCellFactory(new UtensilCellFactory());
        stepListView.setCellFactory(new StepCellFactory());
    }

    @FXML
    void submitRecipe(Event actionEvent) throws SQLException, ClassNotFoundException, IOException {
        RecipeDataBase recipeDataBase = new RecipeDataBase();
        ArrayList<Ingredient> ingredients = new ArrayList<>(getIngredients());
        ArrayList<Utensil> utensils = new ArrayList<>(getUtensils());
        ArrayList<Step> steps = new ArrayList<>(getSteps());
        String name = recipeNameInputTextArea.getText();
        String time = timeInput.getText();
        Recipe recipeToAdd = new Recipe(name,time, steps, ingredients, utensils );
        recipeDataBase.addRecipe(recipeToAdd);
        recipeDataBase.close();
        Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        returnToMainScene(primaryStage);
    }

    private void returnToMainScene(Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(NewRecipeController.class.getResource("recipe_list_view_scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }


    private ObservableList<Step> getSteps() {
        return stepListView.getItems();
    }

    private ObservableList<Utensil> getUtensils() {
        return utensilsListView.getItems();
    }

    private ObservableList<Ingredient> getIngredients() {
        return ingredientsListView.getItems();
    }

    @FXML
    void addIngredient(){
        Ingredient ingredientToAdd = new Ingredient(ingredientChoiceBox.getValue(), ingredientInputField.getText());
        for(Ingredient ingredient: ingredientsListView.getItems()){
            if(Objects.equals(ingredientToAdd.name(), ingredient.name()) || ingredientToAdd.name() == null){
                return;
            }
        }
        ingredientsListView.getItems().add(ingredientToAdd);
    }

    @FXML
    void addUtensil(){
        Utensil utensilToAdd = new Utensil(utensilChoiceBox.getValue());
        for(Utensil utensil: utensilsListView.getItems()) {
            if (Objects.equals(utensilToAdd.name(), utensil.name()) || utensil.name() == null) {
                return;
            }
        }
        utensilsListView.getItems().add(utensilToAdd);
    }

    @FXML
    void addStep(){
        Step stepToAdd = new Step(numberOfSteps, "", "");
        numberOfSteps += 1;
        stepListView.getItems().add(stepToAdd);
    }

    public void showNewIngredientStage(ActionEvent actionEvent) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(NewRecipeController.class.getResource("add_ingredient_view.fxml"));
        final Scene scene = new Scene(fxmlLoader.load());
        Stage newIngredientStage = new Stage();
        newIngredientStage.setScene(scene);
        newIngredientStage.show();
    }
    public void showNewUtensilStage(ActionEvent actionEvent) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(NewRecipeController.class.getResource("add_utensil_view.fxml"));
        final Scene scene = new Scene(fxmlLoader.load());
        Stage newUtensilStage = new Stage();
        newUtensilStage.setScene(scene);
        newUtensilStage.show();
    }
    @FXML
    public void updateSpinners() throws SQLException {
        this.utensilChoiceBox.getItems().setAll(recipeDataBase.getAllUtensils());
        this.ingredientChoiceBox.getItems().setAll(recipeDataBase.getAllIngredients());
    }

    @FXML
    public void deleteIngredientFromListView(KeyEvent event){
        KeyCode keyCode = event.getCode();
        if(keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.DELETE) {
            ListView<Ingredient> listview = (ListView<Ingredient>) event.getSource();
            listview.getItems().remove(listview.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void deleteUtensilFromListView(KeyEvent event){
        KeyCode keyCode = event.getCode();
        if(keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.DELETE) {
            ListView<Utensil> listView = (ListView<Utensil>) event.getSource();
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void deleteStepFromListView(KeyEvent event){
        KeyCode keyCode = event.getCode();
        if(keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.DELETE) {
            ListView<Step> listView = (ListView<Step>) event.getSource();
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        }
    }
}