package com.example.recipesjavafx;

public record Recipe(Step[] steps, Ingredient[] ingredients, Utensil[] utensils, String description, String approxTime, String name) {
}
