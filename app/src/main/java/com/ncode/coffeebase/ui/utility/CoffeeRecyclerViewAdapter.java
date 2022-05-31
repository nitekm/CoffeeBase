package com.ncode.coffeebase.ui.utility;

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
import com.bumptech.glide.Glide;
import com.ncode.coffeebase.ui.CoffeeActivity;
import com.ncode.coffeebase.R;
import com.ncode.coffeebase.model.Coffee;

import java.util.List;

import static com.ncode.coffeebase.ui.CoffeeActivity.COFFEE_ID_KEY;

public class CoffeeRecyclerViewAdapter extends RecyclerView.Adapter<CoffeeRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "CoffeeRecViewAdapter";

    private List<Coffee> coffees;
    private Context context;

    public CoffeeRecyclerViewAdapter(final Context context, final List<Coffee> coffees) {
        this.context = context;
        this.coffees = coffees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_coffee_card_view, parent, false);
        return new ViewHolder(view);
    }

    //TODO: it may not work with position not used holder.getAdapterPosition
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.coffeeNameTxt.setText(coffees.get(holder.getAdapterPosition()).getName());
        Glide.with(context)
                .asBitmap()
                .load(coffees.get(holder.getAdapterPosition()).getImageUrl())
                .placeholder(R.mipmap.coffeebean)
                .into(holder.coffeeImg);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoffeeActivity.class);
                intent.putExtra(COFFEE_ID_KEY, coffees.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coffees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView coffeeImg;
        private TextView coffeeNameTxt;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.coffeeCardView);
            coffeeImg = itemView.findViewById(R.id.imgCoffee);
            coffeeNameTxt = itemView.findViewById(R.id.coffeeNameTxt);
        }
    }
}
