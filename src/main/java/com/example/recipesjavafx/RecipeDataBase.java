package com.example.recipesjavafx;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDataBase {
    private Connection connection;

    public RecipeDataBase() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String path = System.getProperty("user.dir") + "\\" + "recipeDataBase.sqlite";
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        createTables();
    }

    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        String createRecipeTableString = "CREATE TABLE IF NOT EXISTS recipe (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "approxTime TIME)";
        String createIngredientTableString = "CREATE TABLE IF NOT EXISTS ingredient (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)";
        String createUtensilTableString = "CREATE TABLE IF NOT EXISTS utensil (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)";
        String createStepTableString = "CREATE TABLE IF NOT EXISTS step (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "text TEXT," +
                "stepNumber INTEGER," +
                "recipe_id INTEGER," +
                "time TIME," +
                "FOREIGN KEY (recipe_id) REFERENCES recipe(id))";
        String createJunctionTableRecipeIngredient = "CREATE TABLE IF NOT EXISTS recipe_ingredient (" +
                "recipe_id INTEGER NOT NULL," +
                "ingredient_id INTEGER NOT NULL," +
                "amount TEXT," +
                "PRIMARY KEY (recipe_id, ingredient_id)," +
                "FOREIGN KEY (recipe_id) REFERENCES recipe(id)," +
                "FOREIGN KEY (ingredient_id) REFERENCES ingredient(id))";
        String createJunctionTableRecipeUtensil = "CREATE TABLE IF NOT EXISTS recipe_utensil (" +
                "recipe_id INTEGER NOT NULL," +
                "utensil_id INTEGER NOT NULL," +
                "PRIMARY KEY(recipe_id, utensil_id)," +
                "FOREIGN KEY (recipe_id) REFERENCES recipe(id)," +
                "FOREIGN KEY (utensil_id) REFERENCES utensil(id))";
        statement.execute(createIngredientTableString);
        statement.execute(createRecipeTableString);
        statement.execute(createUtensilTableString);
        statement.execute(createJunctionTableRecipeIngredient);
        statement.execute(createStepTableString);
        statement.execute(createJunctionTableRecipeUtensil);
        statement.close();
    }

    public void addRecipe(Recipe recipe) throws SQLException {
        Statement statement = connection.createStatement();
        String insertRecipeString = "INSERT INTO recipe(name, approxTime) VALUES ('" +
                recipe.name() + "', '" + recipe.approxTime() + "')";
        statement.executeUpdate(insertRecipeString);

        String getRecipeIdString = "SELECT last_insert_rowid()";
        int recipeId = statement.executeQuery(getRecipeIdString).getInt(1);

        for (Ingredient ingredient : recipe.ingredients()) {
            String ingredientName = ingredient.name().toLowerCase().trim();
            PreparedStatement ingredientExistsStatement = connection.prepareStatement("SELECT * FROM ingredient WHERE LOWER(name) = ?");
            ingredientExistsStatement.setString(1, ingredientName);
            ResultSet ingredientExistsResult = ingredientExistsStatement.executeQuery();
            if (!ingredientExistsResult.next()) {
                String insertIngredientString = "INSERT INTO ingredient(name) VALUES (?)";
                PreparedStatement insertIngredientStatement = connection.prepareStatement(insertIngredientString);
                insertIngredientStatement.setString(1, ingredientName);
                insertIngredientStatement.executeUpdate();
            }

            PreparedStatement getIngredientIdStatement = connection.prepareStatement("SELECT id FROM ingredient WHERE LOWER(name) = ?");
            getIngredientIdStatement.setString(1, ingredientName);
            ResultSet getIngredientIdResult = getIngredientIdStatement.executeQuery();
            int ingredientId = getIngredientIdResult.getInt("id");

            String insertRecipeIngredientString = "INSERT INTO recipe_ingredient(recipe_id, ingredient_id, amount) VALUES (" +
                    recipeId + ", " + ingredientId + ", '" + ingredient.amount() + "')";
            statement.executeUpdate(insertRecipeIngredientString);
        }

        for (Utensil utensil : recipe.utensils()) {
            String utensilName = utensil.name().toLowerCase().trim();
            PreparedStatement utensilExistsStatement = connection.prepareStatement("SELECT * FROM utensil WHERE LOWER(name) = ?");
            utensilExistsStatement.setString(1, utensilName);
            ResultSet utensilExistsResult = utensilExistsStatement.executeQuery();
            if (!utensilExistsResult.next()) {
                String insertUtensilString = "INSERT INTO utensil(name) VALUES (?)";
                PreparedStatement insertUtensilStatement = connection.prepareStatement(insertUtensilString);
                insertUtensilStatement.setString(1, utensilName);
                insertUtensilStatement.executeUpdate();
            }

            PreparedStatement getUtensilIdStatement = connection.prepareStatement("SELECT id FROM utensil WHERE LOWER(name) = ?");
            getUtensilIdStatement.setString(1, utensilName);
            ResultSet getUtensilIdResult = getUtensilIdStatement.executeQuery();
            int utensilId = getUtensilIdResult.getInt("id");

            String insertRecipeUtensilString = "INSERT INTO recipe_utensil(recipe_id, utensil_id) VALUES (" +
                    recipeId + ", " + utensilId + ")";
            statement.executeUpdate(insertRecipeUtensilString);
        }

        for (Step step : recipe.steps()) {
            String insertStepString = "INSERT INTO step(text, recipe_id, time) VALUES ('" +
                    step.text() + "', " + recipeId + ", '" + step.time() + "')";
            statement.executeUpdate(insertStepString);
        }

        for (Step step : recipe.steps()) {
            String insertStepString = "INSERT INTO step(text, recipe_id, time) VALUES ('" +
                    step.text() + "', " + recipeId + ", '" + step.time() + "')";
            statement.executeUpdate(insertStepString);
        }
        statement.close();
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe");

        while (resultSet.next()) {
            int recipeId = resultSet.getInt("id");
            String recipeName = resultSet.getString("name");
            String recipeApproxTime = resultSet.getString("approxTime");

            Recipe recipe = new Recipe(recipeName, recipeApproxTime, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

            PreparedStatement recipeIngredientStatement = connection.prepareStatement("SELECT ingredient.name, recipe_ingredient.amount FROM ingredient JOIN recipe_ingredient ON ingredient.id = recipe_ingredient.ingredient_id WHERE recipe_ingredient.recipe_id = ?");
            recipeIngredientStatement.setInt(1, recipeId);
            ResultSet recipeIngredientResultSet = recipeIngredientStatement.executeQuery();
            while (recipeIngredientResultSet.next()) {
                String ingredientName = recipeIngredientResultSet.getString("name");
                String ingredientAmount = recipeIngredientResultSet.getString("amount");
                Ingredient ingredient = new Ingredient(ingredientName, ingredientAmount);
                recipe.addIngredient(ingredient);
            }

            PreparedStatement recipeUtensilStatement = connection.prepareStatement("SELECT utensil.name FROM utensil JOIN recipe_utensil ON utensil.id = recipe_utensil.utensil_id WHERE recipe_utensil.recipe_id = ?");
            recipeUtensilStatement.setInt(1, recipeId);
            ResultSet recipeUtensilResultSet = recipeUtensilStatement.executeQuery();
            while (recipeUtensilResultSet.next()) {
                String utensilName = recipeUtensilResultSet.getString("name");
                Utensil utensil = new Utensil(utensilName);
                recipe.addUtensil(utensil);
            }

            PreparedStatement stepStatement = connection.prepareStatement("SELECT * FROM step WHERE recipe_id = ?");
            stepStatement.setInt(1, recipeId);
            ResultSet stepResultSet = stepStatement.executeQuery();
            while (stepResultSet.next()) {
                String stepText = stepResultSet.getString("text");
                int stepNumber = stepResultSet.getInt("stepNumber");
                String stepTime = stepResultSet.getString("time");
                Step step = new Step(stepNumber, stepText, stepTime);
                recipe.addStep(step);
            }

            recipes.add(recipe);
        }

        statement.close();
        return recipes;
    }
    public String[] getAllIngredients() throws SQLException {
        List<String> ingredients = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM ingredient");

        while (resultSet.next()) {
            ingredients.add(resultSet.getString("name"));
        }

        statement.close();
        resultSet.close();

        return ingredients.toArray(new String[0]);
    }


    public String[] getAllUtensils() throws SQLException {
        List<String> utensils = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM utensil");

        while (resultSet.next()) {
            utensils.add(resultSet.getString("name"));
        }

        statement.close();
        resultSet.close();

        return utensils.toArray(new String[0]);
    }
    public void addIngredient(String ingredientName) throws SQLException {
        String ingredientNameTrimmed = ingredientName.toLowerCase().trim();
        PreparedStatement ingredientExistsStatement = connection.prepareStatement("SELECT * FROM ingredient WHERE LOWER(name) = ?");
        ingredientExistsStatement.setString(1, ingredientNameTrimmed);
        ResultSet ingredientExistsResult = ingredientExistsStatement.executeQuery();
        if (!ingredientExistsResult.next()) {
            String insertIngredientString = "INSERT INTO ingredient(name) VALUES (?)";
            PreparedStatement insertIngredientStatement = connection.prepareStatement(insertIngredientString);
            insertIngredientStatement.setString(1, ingredientNameTrimmed);
            insertIngredientStatement.executeUpdate();
            insertIngredientStatement.close();
        }
    }

    public void addUtensil(String utensilName) throws SQLException {
        String utensilNameTrimmed = utensilName.toLowerCase().trim();
        PreparedStatement utensilExistsStatement = connection.prepareStatement("SELECT * FROM utensil WHERE LOWER(name) = ?");
        utensilExistsStatement.setString(1, utensilNameTrimmed);
        ResultSet utensilExistsResult = utensilExistsStatement.executeQuery();
        if (!utensilExistsResult.next()) {
            String insertUtensilString = "INSERT INTO utensil(name) VALUES (?)";
            PreparedStatement insertUtensilStatement = connection.prepareStatement(insertUtensilString);
            insertUtensilStatement.setString(1, utensilNameTrimmed);
            insertUtensilStatement.executeUpdate();
            insertUtensilStatement.close();
        }
    }



    public void close() throws SQLException {
        connection.close();
    }

    public Recipe getRecipeByName(String recipeName) throws SQLException {
        Recipe recipe = null;

        PreparedStatement statement = connection.prepareStatement("SELECT id, name, approxTime FROM recipe WHERE name = ?");
        statement.setString(1, recipeName);
        ResultSet recipeResult = statement.executeQuery();

        if (recipeResult.next()) {
            int id = recipeResult.getInt("id");
            String name = recipeResult.getString("name");
            String approxTime = recipeResult.getString("approxTime");

            PreparedStatement getIngredientsStatement = connection.prepareStatement("SELECT ingredient.name, recipe_ingredient.amount FROM ingredient JOIN recipe_ingredient ON ingredient.id = recipe_ingredient.ingredient_id WHERE recipe_ingredient.recipe_id = ?");
            getIngredientsStatement.setInt(1, id);
            ResultSet ingredientsResult = getIngredientsStatement.executeQuery();
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while (ingredientsResult.next()) {
                String ingredientName = ingredientsResult.getString("name");
                String amount = ingredientsResult.getString("amount");
                ingredients.add(new Ingredient(ingredientName, amount));
            }

            PreparedStatement getUtensilsStatement = connection.prepareStatement("SELECT utensil.name FROM utensil JOIN recipe_utensil ON utensil.id = recipe_utensil.utensil_id WHERE recipe_utensil.recipe_id = ?");
            getUtensilsStatement.setInt(1, id);
            ResultSet utensilsResult = getUtensilsStatement.executeQuery();
            ArrayList<Utensil> utensils = new ArrayList<>();
            while (utensilsResult.next()) {
                String utensilName = utensilsResult.getString("name");
                utensils.add(new Utensil(utensilName));
            }

            PreparedStatement getStepsStatement = connection.prepareStatement("SELECT text, stepNumber, time FROM step WHERE recipe_id = ? ORDER BY stepNumber ASC");
            getStepsStatement.setInt(1, id);
            ResultSet stepsResult = getStepsStatement.executeQuery();
            ArrayList<Step> steps = new ArrayList<>();
            while (stepsResult.next()) {
                String text = stepsResult.getString("text");
                int stepNumber = stepsResult.getInt("stepNumber");
                String time = stepsResult.getString("time");
                steps.add(new Step(stepNumber, text, time));
            }

            recipe = new Recipe(name, approxTime, steps, ingredients, utensils);
        }

        return recipe;
    }

}
