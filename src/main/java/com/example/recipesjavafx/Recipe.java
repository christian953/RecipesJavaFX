package com.example.recipesjavafx;

import java.util.ArrayList;

public record Recipe(
                     String name,
                     String approxTime,
                     ArrayList<Step> steps,
                     ArrayList<Ingredient> ingredients,
                     ArrayList<Utensil> utensils
) {
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void addUtensil(Utensil utensil) {
        utensils.add(utensil);
    }

    public void addStep(Step step) {
        steps.add(step);
    }
}
