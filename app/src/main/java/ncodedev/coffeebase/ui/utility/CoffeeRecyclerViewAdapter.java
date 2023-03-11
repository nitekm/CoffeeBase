package ncodedev.coffeebase.ui.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.ui.CoffeeActivity;

import java.util.List;

import static ncodedev.coffeebase.ui.CoffeeActivity.COFFEE_ID_KEY;
import static ncodedev.coffeebase.utils.Utils.imageDownloadUrl;

public class CoffeeRecyclerViewAdapter extends RecyclerView.Adapter<CoffeeRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "CoffeeRecyclerViewAdapter";

    private final List<Coffee> coffees;
    private final Context context;
    private final ImageHelper imageHelper = ImageHelper.getInstance();

    public CoffeeRecyclerViewAdapter(final Context context, final List<Coffee> coffees) {
        this.context = context;
        this.coffees = coffees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_coffee_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.coffeeNameTxt.setText(coffees.get(holder.getAdapterPosition()).getName());
        if (coffees.get(holder.getAdapterPosition()).isFavourite()) {
            holder.imgFavourite.setVisibility(View.VISIBLE);
        }

        if (coffees.get(holder.getAdapterPosition()).getCoffeeImageName() != null) {
            imageHelper.picassoSetImage(imageDownloadUrl(coffees.get(holder.getAdapterPosition()).getCoffeeImageName()),
                    holder.coffeeImg,
                    R.mipmap.coffeebean);
        } else {
            holder.coffeeImg.setImageResource(R.mipmap.coffeebean);
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CoffeeActivity.class);
            intent.putExtra(COFFEE_ID_KEY, coffees.get(holder.getAdapterPosition()).getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return coffees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageView coffeeImg;
        private final TextView coffeeNameTxt;
        private final ImageView imgFavourite;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.coffeeCardView);
            coffeeImg = itemView.findViewById(R.id.imgCoffee);
            coffeeNameTxt = itemView.findViewById(R.id.coffeeNameTxt);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
        }
    }
}
