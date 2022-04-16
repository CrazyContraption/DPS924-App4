package dps924.assignment4.data;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dps924.assignment4.MainActivity;
import dps924.assignment4.R;

public class RecipeBrowserResultRecyclerAdapter extends RecyclerView.Adapter<RecipeBrowserResultRecyclerAdapter.ViewHolder> {

    private final ArrayList<RecipeBasic> values;
    public final Bitmap[] images = new Bitmap[10];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public RecipeBrowserResultRecyclerAdapter(RecipeBrowserResults data) {
        values = data.results;
        for (int index = 0; index < images.length; index++)
            images[index] = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_result, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CardView view = viewHolder.itemView.findViewById(R.id.card);
        if (view == null)
            return;

        int id = values.get(position).id;
        String identifier = (id >= 0 ? "+" : (id == 0 ? "-" : "")) + (id + 1);
        ((TextView)view.findViewById(R.id.id)).setText(identifier);

        if (images[position] == null && values.get(position).image != null && values.get(position).image.length() > 0)
            MainActivity.DataManager.getImageAsync(values.get(position).image, (id >= 0 ? "+" : "-") + (position + 1));

        Bitmap bmp = images[position];
        ImageView image = view.findViewById(R.id.recipeImage);

        if (image != null && bmp != null)
            image.setImageBitmap(bmp);
        //DecimalFormat f = new DecimalFormat("0.00");
        ((TextView)view.findViewById(R.id.recipeTitle)).setText(values.get(position).title);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}