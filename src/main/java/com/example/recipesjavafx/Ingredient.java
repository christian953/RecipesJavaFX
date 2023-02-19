package com.example.recipesjavafx;

public record Ingredient(String name, String amount) {

    public String toString(){
        return amount + " " + name;
    }
}
