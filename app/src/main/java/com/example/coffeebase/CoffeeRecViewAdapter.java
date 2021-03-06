package com.example.coffeebase;

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

import static com.example.coffeebase.CoffeeActivity.COFFEE_ID_KEY;

import java.util.ArrayList;

public class CoffeeRecViewAdapter extends RecyclerView.Adapter<CoffeeRecViewAdapter.ViewHolder> {

    private static final String TAG = "CoffeeRecViewAdapter";

    private ArrayList<Coffee> coffees;
    private Context mContext;

    public CoffeeRecViewAdapter(Context mContext, ArrayList<Coffee> coffees) {
        this.mContext = mContext;
        this.coffees = coffees;
    }

    @NonNull
    @Override
    public CoffeeRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_coffee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.coffeeNameTxt.setText(coffees.get(position).getName());
        holder.rating.setText(coffees.get(position).getRating());
        Glide.with(mContext)
                .asBitmap()
                .load(coffees.get(position).getImageUrl())
                .into(holder.coffeeImg);
        
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CoffeeActivity.class);
                intent.putExtra(COFFEE_ID_KEY, coffees.get(position).getId());
                mContext.startActivity(intent);
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
        private TextView coffeeNameTxt, rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.coffeeCardView);
            coffeeImg = itemView.findViewById(R.id.imgCoffee);
            coffeeNameTxt = itemView.findViewById(R.id.coffeeNameTxt);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
