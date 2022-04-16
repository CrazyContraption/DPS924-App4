package dps924.assignment4.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeBrowserResults {
    public int offset;
    public int number;
    public ArrayList<RecipeBasic> results;
    public int totalResults;

    /*public RecipeBrowserResults(ArrayList<RecipeBasic> recipes) {
        results = recipes;
    }*/

    public RecipeBrowserResults(ArrayList<SavedRecipe> recipes) {
        results = new ArrayList<>();
        for (int index = 0; index < recipes.size(); index++) {
            results.add(recipes.get(index));
        }
    }

    public RecipeBrowserResults(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("offset"))
            offset = jsonObject.getInt("offset");
        if (jsonObject.has("number"))
            number = jsonObject.getInt("number");
        if (jsonObject.has("results")) {
            results = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject item = jsonArray.getJSONObject(index);
                results.add(new RecipeBasic(item));
            }
        }
        if (jsonObject.has("totalResults"))
            totalResults = jsonObject.getInt("totalResults");
    }
}
