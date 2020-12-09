package com.example.coffeebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import static com.example.coffeebase.CoffeeActivity.COFFEE_ID_KEY;

import java.util.ArrayList;

public class CoffeeRecViewAdapter extends RecyclerView.Adapter<CoffeeRecViewAdapter.ViewHolder> {

    private static final String TAG = "BooksRecViewAdapter";

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
        //TODO enable choosing photo from phone memory
        //Glide.with(mContext)
        //        .asBitmap()
        //        .load(coffees.get(position).getCoffeeUrl())
        //        .into(holder.coffeeImg);
        
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
