package ncodedev.coffeebase.ui.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrewRecyclerViewAdapter extends RecyclerView.Adapter<BrewRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "BrewRecyclerViewAdapter";

    private final List<Brew> brews;
    private final Context context;

    public BrewRecyclerViewAdapter(List<Brew> brews, Context context) {
        this.brews = brews;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public BrewRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_brew_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BrewRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.brewNameTxt.setText(brews.get(holder.getAdapterPosition()).getName());
        holder.methodNameTxt.setText(brews.get(holder.getAdapterPosition()).getMethod());
        holder.waterAmountTxt.setText(brews.get(holder.getAdapterPosition()).getWaterAmountInMl());
        holder.coffeeAmountTxt.setText(brews.get(holder.getAdapterPosition()).getCoffeeWeightInGrams());
        holder.brewTimeTxt.setText(brews.get(holder.getAdapterPosition()).getTotalTime());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BrewActivity.class);
            intent.putExtra(BREW_ID_KEY, brews.get(holder.getAdapterPosition()).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return brews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView brewNameTxt, methodNameTxt, brewTimeTxt, waterAmountTxt, coffeeAmountTxt;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.brewCardView);
            brewNameTxt = itemView.findViewById(R.id.brewNameTxt);
            methodNameTxt = itemView.findViewById(R.id.methodNameTxt);
            brewTimeTxt = itemView.findViewById(R.id.brewTimeTxt);
            waterAmountTxt = itemView.findViewById(R.id.waterAmountTxt);
            coffeeAmountTxt = itemView.findViewById(R.id.coffeeAmountTxt);
        }
    }
}
