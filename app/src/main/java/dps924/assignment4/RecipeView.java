package dps924.assignment4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import dps924.assignment4.data.Recipe;
import dps924.assignment4.data.SavedRecipe;
import dps924.assignment4.databinding.RecipeViewBinding;

public class RecipeView extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private RecipeViewBinding binding;

    public static SavedRecipe recipe;
    public static boolean isOnPrimary = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RecipeViewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (savedInstanceState != null) {
            MainActivity.DataManager.getRecipeByIdAsync(savedInstanceState.getInt(getString(R.string.recipeID)));
        } else {
            FirstFragment.updateFragment((Recipe)null);
            Intent intent = this.getIntent();
            int id = intent.getIntExtra(getString(R.string.recipeID), 0) - 1;
            if (id < 0)
                MainActivity.DataManager.getRecipeByIdAsync(id);
            else
                MainActivity.DataManager.getRecipe(id);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_view);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.DataManager.saveRecipe(recipe);
                Snackbar.make(view, MainActivity.AppContext.getResources().getString(R.string.recipe_saved), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        System.out.println("Pressed Button:\n\tBACK");
        if (isOnPrimary) // Should go back to main activity
            finish();
        else  // Should go back to last fragment
            Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_view).popBackStack();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_view);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @NonNull
    @Override
    public void onSaveInstanceState(Bundle r_SavedInstanceState) {
        super.onSaveInstanceState(r_SavedInstanceState);
        r_SavedInstanceState.putInt(getString(R.string.recipeID), recipe.id);
    }
}