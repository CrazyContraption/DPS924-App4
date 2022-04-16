package dps924.assignment4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import dps924.assignment4.data.Recipe;
import dps924.assignment4.databinding.FragmentIngredientsBinding;

public class SecondFragment extends Fragment {

    private FragmentIngredientsBinding binding;
    private static View thisView;

    public static void updateFragment(Recipe recipe) {
        if (recipe != null) {
            //((TextView) thisView.findViewById(R.id.recipe_title)).setText(recipe.title == null ? "Unknown Recipe" : recipe.title);
            //((TextView) thisView.findViewById(R.id.recipe_summary)).setText(recipe.summary == null ? "No summary." : Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_COMPACT));
            //((TextView) thisView.findViewById(R.id.recipe_rating)).setText(recipe.spoonScore == 0 ? "No ratings" : recipe.spoonScore + "");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);
        thisView = binding.getRoot();
        return thisView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeView.isOnPrimary = false;
        if (thisView != null)
            updateFragment(RecipeView.recipe);
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}