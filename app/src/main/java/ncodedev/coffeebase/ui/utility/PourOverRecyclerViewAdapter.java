package ncodedev.coffeebase.ui.utility;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        Optional.ofNullable(pourOvers.get(holder.getAdapterPosition()).getWaterAmountInMl())
                .ifPresent(holder.waterAmountTxt::setText);
        Optional.ofNullable(pourOvers.get(holder.getAdapterPosition()).getTime())
                .map(String::valueOf)
                .ifPresent(holder.pourTimeTxt::setText);
    }

    @Override
    public int getItemCount() {
        return pourOvers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageButton btnAddPourOver;
        private final TextView pourOverNumber, waterAmountTxt, pourTimeTxt;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.pourOverCardView);
            btnAddPourOver = itemView.findViewById(R.id.btnAddPourOver);
            pourOverNumber = itemView.findViewById(R.id.pourNumberTxt);
            waterAmountTxt = itemView.findViewById(R.id.pourWaterAmountTxt);
            pourTimeTxt = itemView.findViewById(R.id.pourTimeTxt);
        }
    }
}
