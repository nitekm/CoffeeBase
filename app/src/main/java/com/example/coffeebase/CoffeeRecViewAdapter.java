package com.example.coffeebase;

import android.content.Context;
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

import java.util.ArrayList;

public class CoffeeRecViewAdapter extends RecyclerView.Adapter<CoffeeRecViewAdapter.ViewHolder> {

    private static final String TAG = "BooksRecViewAdapter";

    private ArrayList<Coffee> coffees;
    private Context mContext;

    public CoffeeRecViewAdapter(Context mContext) {
        this.mContext = mContext;
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
        //Glide.with(mContext)
        //        .asBitmap()
        //        .load(coffees.get(position).getCoffeeUrl())
        //        .into(holder.coffeeImg);
    }


    @Override
    public int getItemCount() {
        return coffees.size();
    }

    public void setCoffees(ArrayList<Coffee> coffees) {
        this.coffees = coffees;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView coffeeImg;
        private TextView coffeeNameTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.coffeeCardView);
            coffeeImg = itemView.findViewById(R.id.imgCoffee);
            coffeeNameTxt = itemView.findViewById(R.id.coffeeNameTxt);
        }
    }
}
