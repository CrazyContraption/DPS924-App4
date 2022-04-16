package dps924.assignment4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import dps924.assignment4.data.DataService;
import dps924.assignment4.databinding.ActivityMainBinding;
import dps924.assignment4.ui.browse.BrowseFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static DataService DataManager;
    public static Context AppContext;

    public void handleAction(View view) {
        Resources resources = AppContext.getResources();
        if (view instanceof Button){ // Buttons
            Button button = (Button) view;
            String text = (String) button.getText();
            if (text.equals(resources.getString(R.string.browse_admin_random))) {
                System.out.println("ADMIN:\n\t- Getting Random Recipe...");
                Snackbar.make(view, "Getting you a random recipe...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                MainActivity.DataManager.getRandomRecipe();
                System.out.println("- DONE -");
            } else if (text.equals(resources.getString(R.string.browse_admin_api))) {
                System.out.println("ADMIN:\n\t- Getting API Stats...");
                Snackbar.make(view, "Oops, not implemented!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //System.out.println("- DONE -");
            } else if (text.equals(resources.getString(R.string.browse_admin_clear))) {
                System.out.println("ADMIN:\n\t- Clearing Storage...");
                MainActivity.DataManager.clearRecipes();
                Snackbar.make(view, "Storage Cleared!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                System.out.println("- DONE -");
            }
        } else if (view instanceof ConstraintLayout) { // Recipes
            TextView textView = view.findViewById(R.id.id);
            String identifier = String.valueOf(textView.getText());
            Intent intent;
            if (identifier.charAt(0) == '-') {
                intent = new Intent(AppContext, RecipeView.class);
                intent.putExtra(resources.getString(R.string.recipeID), Integer.parseInt(identifier));
            } else {
                intent = new Intent(AppContext, CreateView.class);
                intent.putExtra(resources.getString(R.string.recipeID), Integer.parseInt(identifier.substring(1)));
            }
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("Pressed Button:\n\tBACK");
        BrowseFragment.updateFragment(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext = this.getBaseContext();
        try {
            if (DataManager == null)
                DataManager = new DataService(AppContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_saved, R.id.navigation_browse, R.id.navigation_create)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}