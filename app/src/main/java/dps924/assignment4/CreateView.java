package dps924.assignment4;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import dps924.assignment4.data.SavedRecipe;
import dps924.assignment4.databinding.ActivityCreateViewBinding;
import dps924.assignment4.ui.create.CreateFragment;

public class CreateView extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private static ActivityCreateViewBinding binding;

    public static SavedRecipe recipeData;
    public static int savedId = -1;
    public static boolean isOnPrimary = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateViewBinding.inflate(getLayoutInflater());
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new CreateFragment()).commit();}

        setContentView(R.layout.activity_create_view);


        Intent intent = this.getIntent();
        savedId = intent.getIntExtra(getString(R.string.recipeID), 0) - 1;
        if (savedId < 0)
            MainActivity.DataManager.getRecipeByIdAsync(savedId);
        else
            MainActivity.DataManager.getRecipe(savedId);

        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_create);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/
    }

    @Override
    public void onBackPressed() {
        System.out.println("Pressed Button:\n\tBACK");
        savedId = -1;
        if (isOnPrimary) // Should go back to main activity
            finish();
        //else  // Should go back to last fragment
        //    Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_create).popBackStack();
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_create);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    @NonNull
    @Override
    public void onSaveInstanceState(Bundle r_SavedInstanceState) {
        super.onSaveInstanceState(r_SavedInstanceState);
        r_SavedInstanceState.putInt(getString(R.string.recipeID), CreateView.recipeData.id);
    }
}