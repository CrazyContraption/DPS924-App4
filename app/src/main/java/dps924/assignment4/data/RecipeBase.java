package dps924.assignment4.data;

import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeBase {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;

    public RecipeBase() {
        id = -1;
    }

    public RecipeBase(int Id, String Title) {
        id = Id;
        title = Title;
    }

    public RecipeBase(RecipeBase recipe) {
        id = recipe.id;
        title = recipe.title;
    }

    public RecipeBase(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id"))
            id = jsonObject.getInt("id") * -1;
        if (jsonObject.has("title"))
            title = jsonObject.getString("title");
    }
}