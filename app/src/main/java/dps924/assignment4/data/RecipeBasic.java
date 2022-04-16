package dps924.assignment4.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeBasic extends RecipeBase {

    public String image;
    public String imageType;

    RecipeBasic() {
        super();
    }

    RecipeBasic(int id, String title) {
        super(id, title);
    }

    RecipeBasic(int id, String title, String Image, String ImageType) {
        this(id, title);
        image = Image;
        imageType = ImageType;
    }

    RecipeBasic(RecipeBase recipe) {
        this(recipe.id, recipe.title);
    }

    RecipeBasic(RecipeBasic recipe) {
        super(recipe);
        image = recipe.image;
        imageType = recipe.imageType;
    }

    public RecipeBasic(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (jsonObject.has("image"))
            image = jsonObject.getString("image");
        if (jsonObject.has("imageType"))
            imageType = jsonObject.getString("imageType");
    }
}