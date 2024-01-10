package ncodedev.coffeebase.ui.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.PourOver;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;


public class PourOverRecyclerViewAdapter extends RecyclerView.Adapter<PourOverRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "PourOverRecyclerViewAdapter";
    private final List<PourOver> pourOvers;

    public PourOverRecyclerViewAdapter(List<PourOver> pourOvers) {
        this.pourOvers = pourOvers;
    }

    @NonNull
    @NotNull
    @Override
    public PourOverRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_pour_over_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PourOverRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.pourOverNumber.setText(String.valueOf(holder.getAdapterPosition() + 1));
        Optional.of(pourOvers.get(holder.getAdapterPosition()).getWaterAmountInMl())
                .map(String::valueOf)
                .ifPresent(holder.waterAmountTxt::setText);
        Optional.of(pourOvers.get(holder.getAdapterPosition()).getTime())
                .map(String::valueOf)
                .ifPresent(holder.pourTimeTxt::setText);
    }

    @Override
    public int getItemCount() {
        return pourOvers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView pourOverNumber, waterAmountTxt, pourTimeTxt;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.pourOverCardView);
            pourOverNumber = itemView.findViewById(R.id.pourNumberTxt);
            waterAmountTxt = itemView.findViewById(R.id.pourWaterAmountTxt);
            pourTimeTxt = itemView.findViewById(R.id.pourTimeTxt);
        }
    }
}
