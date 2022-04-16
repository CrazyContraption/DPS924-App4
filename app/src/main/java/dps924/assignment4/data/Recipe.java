package dps924.assignment4.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipe extends RecipeBasic {
    public String summary;
    public int likes;
    public double rating;
    public double pricing;
    public int servings;
    public int weightWatcherPoints;

    public int prepTime;
    public int cookTime;

    public String creditsText;
    public String sourceName;
    public String license;
    public String sourceUrl;

    public ArrayList<String> ingredients;

    public boolean vegetarian;
    public boolean vegan;
    public boolean glutenFree;
    public boolean dairyFree;
    public boolean sustainable;

    public ArrayList<String> instructions;

    public Recipe() {
        super();
    }

    public Recipe(RecipeBasic recipe) {
        super(recipe);
    }

    public Recipe(SavedRecipe recipe) {
        super(recipe);
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

    public Recipe(JSONObject jsonObject) throws JSONException {
        super(jsonObject);

        if (jsonObject.has("summary"))
            summary = jsonObject.getString("summary");
        if (jsonObject.has("aggregateLikes"))
            likes = jsonObject.getInt("aggregateLikes");
        if (jsonObject.has("spoonacularScore"))
            rating = jsonObject.getDouble("spoonacularScore");
        if (jsonObject.has("pricePerServing"))
            pricing = jsonObject.getDouble("pricePerServing");
        if (jsonObject.has("servings"))
            servings = jsonObject.getInt("servings");
        if (jsonObject.has("weightWatcherSmartPoints"))
            weightWatcherPoints = jsonObject.getInt("weightWatcherSmartPoints");
        if (jsonObject.has("preparationMinutes"))
            prepTime = jsonObject.getInt("preparationMinutes");
        if (jsonObject.has("cookingMinutes"))
            cookTime = jsonObject.getInt("cookingMinutes");
        if (jsonObject.has("creditsText"))
            creditsText = jsonObject.getString("creditsText");
        if (jsonObject.has("sourceName"))
            sourceName = jsonObject.getString("sourceName");
        if (jsonObject.has("license"))
            license = jsonObject.getString("license");
        if (jsonObject.has("sourceUrl"))
            sourceUrl = jsonObject.getString("sourceUrl");

        ingredients = new ArrayList<>();
        if (jsonObject.has("extendedIngredients")) {
            JSONArray jsonArray = jsonObject.getJSONArray("extendedIngredients");
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject item = jsonArray.getJSONObject(index);
                if (item.has("original"))
                    ingredients.add(item.getString("original"));
            }
        }

        if (jsonObject.has("vegetarian"))
            vegetarian = jsonObject.getBoolean("vegetarian");
        if (jsonObject.has("vegan"))
            vegan = jsonObject.getBoolean("vegan");
        if (jsonObject.has("glutenFree"))
            glutenFree = jsonObject.getBoolean("glutenFree");
        if (jsonObject.has("dairyFree"))
            dairyFree = jsonObject.getBoolean("dairyFree");
        if (jsonObject.has("sustainable"))
            sustainable = jsonObject.getBoolean("sustainable");

        instructions = new ArrayList<>();
        if (jsonObject.has("analyzedInstructions")) {
            JSONArray jsonArray = jsonObject.getJSONArray("analyzedInstructions");
            if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("steps")) {
                jsonArray = jsonArray.getJSONObject(0).getJSONArray("steps");
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject item = jsonArray.getJSONObject(index);
                    if (item.has("step"))
                        instructions.add(item.getString("step"));
                }
            }
        }
    }
}