package dps924.assignment4.ui.create;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import dps924.assignment4.CreateView;
import dps924.assignment4.MainActivity;
import dps924.assignment4.R;
import dps924.assignment4.RecipeView;
import dps924.assignment4.data.SavedRecipe;
import dps924.assignment4.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {

    private CreateViewModel notificationsViewModel;
    private FragmentCreateBinding binding;
    private static View currentView;

    private static void setText(int id, boolean setting, Resources resources) {
        ((CheckBox) currentView.findViewById(id)).setChecked(setting);
        ((CheckBox) currentView.findViewById(id)).setText(setting ? resources.getString(R.string.yes) : resources.getString(R.string.no));
    }

    private static void setText(int id, int num, String fallback) {
        ((EditText) currentView.findViewById(id)).setText("" + num);
    }

    private static void setText(int id, String text, String fallback) {
        ((EditText) currentView.findViewById(id)).setText(text == null ? fallback : text);
    }

    private static void setText(int id, Spanned html, String fallback) {
        ((EditText) currentView.findViewById(id)).setText(html == null ? fallback : html);
    }

    private static int getIntFrom(int id) {
        return Integer.parseInt(String.valueOf(((EditText) currentView.findViewById(id)).getText()));
    }

    private static double getDoubleFrom(int id) {
        String temp = String.valueOf(((EditText) currentView.findViewById(id)).getText());
        if (temp.charAt(0) != '0'
            && temp.charAt(0) != '1'
            && temp.charAt(0) != '2'
            && temp.charAt(0) != '3'
            && temp.charAt(0) != '4'
            && temp.charAt(0) != '5'
            && temp.charAt(0) != '6'
            && temp.charAt(0) != '7'
            && temp.charAt(0) != '8'
            && temp.charAt(0) != '9')
                temp = temp.substring(1);
        return Double.parseDouble(temp);
    }

    private static String getTextFrom(int id) {
        return String.valueOf(((EditText) currentView.findViewById(id)).getText());
    }

    private static Boolean getBoolFrom(int id) {
        return ((CheckBox) currentView.findViewById(id)).isChecked();
    }

    public static void updateFragment(SavedRecipe recipe) {
        CreateView.recipeData = recipe;

        Resources resources = MainActivity.AppContext.getResources();
        if (recipe != null) {
            if (recipe.image != null && recipe.image.length() > 0)
                MainActivity.DataManager.getImageAsync(recipe.image, "_0");

            currentView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

            setText(R.id.recipe_title, recipe.title, "Unknown Recipe");
            setText(R.id.recipe_summary, Html.fromHtml(recipe.summary == null ? "" : recipe.summary, Html.FROM_HTML_MODE_COMPACT), resources.getString(R.string.unknown));

            setText(R.id.recipe_price, "$" + recipe.pricing, resources.getString(R.string.unknown));
            setText(R.id.recipe_servings, "" + recipe.servings, resources.getString(R.string.unknown));

            setText(R.id.recipe_wwpoints, recipe.weightWatcherPoints, resources.getString(R.string.unknown));

            setText(R.id.recipe_preptime, recipe.prepTime, resources.getString(R.string.unknown));
            setText(R.id.recipe_cooktime, recipe.cookTime, resources.getString(R.string.unknown));

            setText(R.id.recipe_credits, recipe.sourceName + ", " + recipe.creditsText, resources.getString(R.string.unknown));

            // TODO: Check for ingredients being defined, and re-enable
            // currentView.findViewById(R.id.button_view_ingredients).setVisibility(View.VISIBLE);

            setText(R.id.recipe_vegetarian, recipe.vegetarian, resources);
            setText(R.id.recipe_vegan, recipe.vegan, resources);
            setText(R.id.recipe_gluten, recipe.glutenFree, resources);
            setText(R.id.recipe_dairy, recipe.dairyFree, resources);

            setText(R.id.recipe_sustainable, recipe.sustainable, resources);
        } else if (currentView != null) {
            currentView.findViewById(R.id.button_save2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CreateView.recipeData == null)
                        if (CreateView.savedId >= 0)
                            return;
                        else
                            CreateView.recipeData = new SavedRecipe("");

                    CreateView.recipeData.title = getTextFrom(R.id.recipe_title);
                    CreateView.recipeData.summary = getTextFrom(R.id.recipe_summary);
                    CreateView.recipeData.pricing = getDoubleFrom(R.id.recipe_price);
                    CreateView.recipeData.servings = getIntFrom(R.id.recipe_servings);
                    CreateView.recipeData.weightWatcherPoints = getIntFrom(R.id.recipe_wwpoints);
                    CreateView.recipeData.prepTime = getIntFrom(R.id.recipe_preptime);
                    CreateView.recipeData.cookTime = getIntFrom(R.id.recipe_cooktime);
                    CreateView.recipeData.creditsText = getTextFrom(R.id.recipe_credits);
                    CreateView.recipeData.vegetarian = getBoolFrom(R.id.recipe_vegetarian);
                    CreateView.recipeData.vegan = getBoolFrom(R.id.recipe_vegan);
                    CreateView.recipeData.glutenFree = getBoolFrom(R.id.recipe_gluten);
                    CreateView.recipeData.dairyFree = getBoolFrom(R.id.recipe_dairy);
                    CreateView.recipeData.sustainable = getBoolFrom(R.id.recipe_sustainable);
                    if (CreateView.savedId >= 0)
                        MainActivity.DataManager.updateRecipe(CreateView.recipeData);
                    else
                        MainActivity.DataManager.saveRecipe(CreateView.recipeData);

                    Snackbar.make(view, MainActivity.AppContext.getResources().getString(R.string.recipe_saved), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            // TODO: Check for ingredients being defined
            currentView.findViewById(R.id.button_view_ingredients).setVisibility(View.GONE);

            setText(R.id.recipe_price, 0, "0");
            setText(R.id.recipe_servings, 0, "0");
            setText(R.id.recipe_wwpoints, 0, "0");
            setText(R.id.recipe_preptime, 0, "0");
            setText(R.id.recipe_cooktime, 0, "0");
            setText(R.id.recipe_vegetarian, false, resources);
            setText(R.id.recipe_vegan, false, resources);
            setText(R.id.recipe_gluten, false, resources);
            setText(R.id.recipe_dairy, false, resources);
            setText(R.id.recipe_sustainable, false, resources);
            if (CreateView.savedId < 0) {
                setText(R.id.recipe_title, "", "");
                setText(R.id.recipe_summary, "", "");
                setText(R.id.recipe_credits, "", "");
                currentView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);
            } else {
                setText(R.id.recipe_title, resources.getString(R.string.unknown), "");
                setText(R.id.recipe_summary, Html.fromHtml(resources.getString(R.string.unknown), Html.FROM_HTML_MODE_COMPACT), null);
                setText(R.id.recipe_credits, resources.getString(R.string.unknown), "");
                currentView.findViewById(R.id.loading_spinner).setVisibility(View.VISIBLE);
            }
        }
    }

    public static void updateFragment(Bitmap bmp) {
        try {
            ImageView image = currentView.findViewById(R.id.recipe_image);
            if (bmp != null)
                image.setImageBitmap(bmp);
            else {
                image.setImageResource(0);
                image.setImageDrawable(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        currentView = binding.getRoot();
        return currentView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeView.isOnPrimary = true;
        if (currentView != null)
            updateFragment(RecipeView.recipe);

        /*binding.buttonViewIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreateFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}