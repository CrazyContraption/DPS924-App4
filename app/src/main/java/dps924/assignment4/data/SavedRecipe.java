package dps924.assignment4.data;

import androidx.room.Entity;

@Entity(tableName = "save")
public class SavedRecipe extends Recipe {

    public String notes;

    public SavedRecipe(String notes) {
        super();
        this.notes = notes;
    }

    public SavedRecipe(Recipe recipe) {
        super(recipe);
        id = 0;
        summary = recipe.summary;
        likes = recipe.likes;
        rating = recipe.rating;
        pricing = recipe.pricing;
        servings = recipe.servings;
        weightWatcherPoints = recipe.weightWatcherPoints;
        prepTime = recipe.prepTime;
        cookTime = recipe.cookTime;
        creditsText = recipe.creditsText;
        sourceName = recipe.sourceName;
        license = recipe.license;
        sourceUrl = recipe.sourceUrl;
        ingredients = recipe.ingredients;
        vegetarian = recipe.vegetarian;
        vegan = recipe.vegan;
        glutenFree = recipe.glutenFree;
        dairyFree = recipe.dairyFree;
        sustainable = recipe.sustainable;
        instructions = recipe.instructions;
    }

    public SavedRecipe(Recipe recipe, String notes) {
        super(recipe);
        this.notes = notes;
    }
}
