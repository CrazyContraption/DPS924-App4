package dps924.assignment4.ui.saved;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dps924.assignment4.MainActivity;
import dps924.assignment4.R;
import dps924.assignment4.data.RecipeBrowserResultRecyclerAdapter;
import dps924.assignment4.data.RecipeBrowserResults;
import dps924.assignment4.data.SavedRecipe;
import dps924.assignment4.databinding.FragmentSavedBinding;

public class SavedFragment extends Fragment {

    private SavedViewModel dashboardViewModel;
    private FragmentSavedBinding binding;

    public static ArrayList<SavedRecipe> lastResults;

    private static RecipeBrowserResultRecyclerAdapter adapter_browser;

    public static SavedFragment newInstance() {
        return new SavedFragment();
    }
    public static View currentView;

    public static void updateFragment(ArrayList<SavedRecipe> results) {
        lastResults = results;
        RecyclerView list_browser = currentView.findViewById(R.id.list_browser);
        if (results == null || results.size() <= 0) {
            list_browser.setVisibility(View.GONE);
            return;
        }
        list_browser.setVisibility(View.VISIBLE);
        adapter_browser = (RecipeBrowserResultRecyclerAdapter) list_browser.getAdapter();
        if (adapter_browser == null) {
            RecipeBrowserResults browser = new RecipeBrowserResults(results);
            adapter_browser = new RecipeBrowserResultRecyclerAdapter(browser);
            list_browser.setAdapter(adapter_browser);
            list_browser.setLayoutManager(new LinearLayoutManager(MainActivity.AppContext));
        }
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        currentView = binding.getRoot();

        MainActivity.DataManager.getAllRecipes();

        /*final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return currentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}