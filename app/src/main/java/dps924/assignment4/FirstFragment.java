package dps924.assignment4;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import dps924.assignment4.data.Recipe;
import dps924.assignment4.databinding.FragmentRecipeBinding;

public class FirstFragment extends Fragment {

    private FragmentRecipeBinding binding;
    private static View currentView;

    private static void setText(int id, int num, String fallback) { ((TextView) currentView.findViewById(id)).setText("" + (num == 0 ? fallback : num)); }
    private static void setText(int id, String text, String fallback) { ((TextView) currentView.findViewById(id)).setText(text == null ? fallback : text); }
    private static void setText(int id, Spanned html, String fallback) { ((TextView) currentView.findViewById(id)).setText(html == null ? fallback : html); }

    public static void updateFragment(Recipe recipe) {
        Resources resources = MainActivity.AppContext.getResources();
        if (recipe != null) {
            if (recipe.image != null && recipe.image.length() > 0)
                MainActivity.DataManager.getImageAsync(recipe.image, "=0");

            currentView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

            setText(R.id.recipe_title, recipe.title, "Unknown Recipe");
            setText(R.id.recipe_summary, Html.fromHtml(recipe.summary == null ? "" : recipe.summary, Html.FROM_HTML_MODE_COMPACT), resources.getString(R.string.unknown));

            setText(R.id.recipe_likes, "" + recipe.likes, resources.getString(R.string.unknown));
            setText(R.id.recipe_rating, "" + recipe.rating, resources.getString(R.string.unknown));
            setText(R.id.recipe_price, "$" + recipe.pricing, resources.getString(R.string.unknown));
            setText(R.id.recipe_servings, "" + recipe.servings, resources.getString(R.string.unknown));

            setText(R.id.recipe_wwpoints, recipe.weightWatcherPoints, resources.getString(R.string.unknown));

            setText(R.id.recipe_preptime, recipe.prepTime, resources.getString(R.string.unknown));
            setText(R.id.recipe_cooktime, recipe.cookTime, resources.getString(R.string.unknown));

            setText(R.id.recipe_credits, recipe.sourceName + ", " + recipe.creditsText, resources.getString(R.string.unknown));

            // TODO: Check for ingredients being defined
            currentView.findViewById(R.id.button_view_ingredients).setVisibility(View.VISIBLE);

            setText(R.id.recipe_vegetarian, recipe.vegetarian ? resources.getString(R.string.yes) : resources.getString(R.string.no), resources.getString(R.string.unknown));
            setText(R.id.recipe_vegan, recipe.vegan ? resources.getString(R.string.yes) : resources.getString(R.string.no), resources.getString(R.string.unknown));
            setText(R.id.recipe_gluten, recipe.glutenFree ? resources.getString(R.string.yes) : resources.getString(R.string.no), resources.getString(R.string.unknown));
            setText(R.id.recipe_dairy, recipe.dairyFree ? resources.getString(R.string.yes) : resources.getString(R.string.no), resources.getString(R.string.unknown));

            setText(R.id.recipe_sustainable, recipe.sustainable ? resources.getString(R.string.yes) : resources.getString(R.string.no), resources.getString(R.string.unknown));
        } else {
            currentView.findViewById(R.id.loading_spinner).setVisibility(View.VISIBLE);

            setText(R.id.recipe_title, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_summary, Html.fromHtml(resources.getString(R.string.unknown), Html.FROM_HTML_MODE_COMPACT), "");

            setText(R.id.recipe_likes, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_rating, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_price, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_servings, resources.getString(R.string.unknown), "");

            setText(R.id.recipe_wwpoints, resources.getString(R.string.unknown), "");

            setText(R.id.recipe_preptime, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_cooktime, resources.getString(R.string.unknown), "");

            setText(R.id.recipe_credits, resources.getString(R.string.unknown), "");

            // TODO: Check for ingredients being defined
            currentView.findViewById(R.id.button_view_ingredients).setVisibility(View.GONE);

            setText(R.id.recipe_vegetarian, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_vegan, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_gluten, resources.getString(R.string.unknown), "");
            setText(R.id.recipe_dairy, resources.getString(R.string.unknown), "");

            setText(R.id.recipe_sustainable, resources.getString(R.string.unknown), "");
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

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        currentView = binding.getRoot();
        return currentView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeView.isOnPrimary = true;
        if (currentView != null)
            updateFragment(RecipeView.recipe);

        binding.buttonViewIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}