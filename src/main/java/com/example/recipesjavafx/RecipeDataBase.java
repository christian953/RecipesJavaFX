package com.example.recipesjavafx;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecipeDataBase {
    private Connection connection;

    public RecipeDataBase() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" +  System.getProperty("user.dir") + "\\" +"recipeDataBase.db ");
        System.out.println(System.getProperty("user.dir"));
        //File file = new File(recipeDataBase)
    }

}
