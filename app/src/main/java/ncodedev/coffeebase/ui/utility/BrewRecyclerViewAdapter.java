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
import ncodedev.coffeebase.ui.BrewActivity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static ncodedev.coffeebase.ui.BrewActivity.BREW;
import static ncodedev.coffeebase.ui.CoffeeActivity.COFFEE_ID_KEY;

public class BrewRecyclerViewAdapter extends RecyclerView.Adapter<BrewRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "BrewRecyclerViewAdapter";
    private final Context context;
    private final List<Brew> brews;
    private final Long coffeeId;

    public BrewRecyclerViewAdapter(Context context, List<Brew> brews, Long coffeeId) {
        this.context = context;
        this.brews = brews;
        this.coffeeId = coffeeId;
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
        Optional.ofNullable(brews.get(holder.getAdapterPosition()).getName())
                .ifPresent(holder.brewNameTxt::setText);
        Optional.ofNullable(brews.get(holder.getAdapterPosition()).getMethod())
                .ifPresent(holder.methodNameTxt::setText);
        Optional.ofNullable(brews.get(holder.getAdapterPosition()).getWaterAmountInMl())
                .map(String::valueOf)
                .ifPresent(holder.waterAmountTxt::setText);
        Optional.ofNullable(brews.get(holder.getAdapterPosition()).getCoffeeWeightInGrams())
                .map(String::valueOf)
                .ifPresent(holder.coffeeAmountTxt::setText);
        Optional.ofNullable(brews.get(holder.getAdapterPosition()).getTotalTime())
                .map(String::valueOf)
                .ifPresent(holder.brewTimeTxt::setText);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BrewActivity.class);
            intent.putExtra(COFFEE_ID_KEY, coffeeId);
            intent.putExtra(BREW, brews.get(holder.getAdapterPosition()));
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
