package dps924.assignment4.ui.browse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import dps924.assignment4.MainActivity;
import dps924.assignment4.R;
import dps924.assignment4.RecipeView;
import dps924.assignment4.data.RecipeBase;
import dps924.assignment4.data.RecipeBrowserResultRecyclerAdapter;
import dps924.assignment4.data.RecipeBrowserResults;
import dps924.assignment4.databinding.FragmentBrowseBinding;

public class BrowseFragment extends Fragment {

    private BrowseViewModel browseViewModel;
    private FragmentBrowseBinding binding;

    private String searchQuery = null;
    private String lastSearch = "";

    private static RecipeBrowserResultRecyclerAdapter adapter_browser;

    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }
    public static View currentView;

    public static void updateFragment(RecipeBrowserResults results) {
        RecyclerView list_browser = currentView.findViewById(R.id.list_browser);
        if (results == null || results.totalResults <= 0) {
            list_browser.setVisibility(View.GONE);
            currentView.findViewById(R.id.recipe_search).setFocusable(true);
            if (results != null)
                Snackbar.make(currentView, "Oops, we couldn't find anything!\nPerhaps try a lighter search?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            return;
        }
        list_browser.setVisibility(View.VISIBLE);
        currentView.findViewById(R.id.recipe_search).clearFocus();
        currentView.findViewById(R.id.recipe_search).setFocusable(false);
        if (adapter_browser == null) {
            adapter_browser = new RecipeBrowserResultRecyclerAdapter(results);
            list_browser.setAdapter(adapter_browser);
            list_browser.setLayoutManager(new LinearLayoutManager(MainActivity.AppContext));
        }
            //adapter_browser = (RecipeBrowserResultRecyclerAdapter) list_browser.getAdapter();
        adapter_browser.notifyDataSetChanged();
    }

    public static void updateFragment(Bitmap bmp, int index) {
        try {
            if (index >= 0) {
                RecyclerView list_browser = currentView.findViewById(R.id.list_browser);
                adapter_browser = (RecipeBrowserResultRecyclerAdapter) list_browser.getAdapter();
                adapter_browser.images[index] = bmp;
                adapter_browser.notifyItemChanged(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAutoComplete(ArrayList<RecipeBase> suggestions) {
        LinearLayout list_suggestions = currentView.findViewById(R.id.list_suggestions);
        list_suggestions.removeAllViews();
        if (suggestions == null)
            return;

        final int MAX_LENGTH = 25;

        for (RecipeBase suggestion : suggestions) {
            TextView newSuggestion = new TextView(currentView.getContext());
            String title = suggestion.title;
            if (title.length() > MAX_LENGTH) {
                title = title.substring(0 , MAX_LENGTH) + "...";
            }
            newSuggestion.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.AppContext, RecipeView.class);
                    intent.putExtra(MainActivity.AppContext.getResources().getString(R.string.recipeID), view.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.AppContext.startActivity(intent);
                }
            });
            newSuggestion.setText(title);
            newSuggestion.setId(suggestion.id + 1);
            list_suggestions.addView(newSuggestion);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_browse, container, false);

        searchQuery = "";
        if (savedInstanceState != null)
            searchQuery = savedInstanceState.getString(getString(R.string.searchQuery));

        SearchView recipe_search = currentView.findViewById(R.id.recipe_search);
        recipe_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.trim().equals("") && newText.length() >= 3) {
                    MainActivity.DataManager.getAutocompleteAsync(newText);
                } else {
                    updateAutoComplete(null);
                }
                lastSearch = "";
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != lastSearch) {
                    lastSearch = query;
                    MainActivity.DataManager.getRecipesAsync(query);
                }
                return false;
            }
        });

        browseViewModel = new ViewModelProvider(this).get(BrowseViewModel.class);
        binding = FragmentBrowseBinding.inflate(inflater, container, false);

        /*
        final TextView textView = binding.searchView;
        browseViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
         */
        return currentView;
    }

    @NonNull
    @Override
    public void onSaveInstanceState(Bundle r_SavedInstanceState) {
        r_SavedInstanceState.putString(getString(R.string.searchQuery), searchQuery);
    }
}