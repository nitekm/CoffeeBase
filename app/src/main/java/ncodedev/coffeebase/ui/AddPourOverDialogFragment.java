package ncodedev.coffeebase.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.PourOver;
import ncodedev.coffeebase.ui.utility.PourOverRecyclerViewAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddPourOverDialogFragment extends DialogFragment {

    private List<PourOver> pourOvers;
    private PourOverRecyclerViewAdapter pourOverRecyclerViewAdapter;
    private TextView waterAmount, timeInSeconds;

    public AddPourOverDialogFragment(List<PourOver> pourOvers, PourOverRecyclerViewAdapter pourOverRecyclerViewAdapter) {
        this.pourOvers = pourOvers;
        this.pourOverRecyclerViewAdapter = pourOverRecyclerViewAdapter;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.add_pour_over_dialog, null);

        waterAmount = view.findViewById(R.id.waterAmountInPourTxt);
        timeInSeconds = view.findViewById(R.id.pourTimeTxt);

        dialogBuilder.setView(view)
                .setTitle(R.string.add_pour)
                .setPositiveButton("OK", (dialog, which) -> {
                    pourOvers.add(createPourOver(Integer.getInteger(waterAmount.getText().toString()), Integer.getInteger(timeInSeconds.getText().toString())));
                    pourOverRecyclerViewAdapter.notifyItemInserted(pourOvers.size()-1);
                })
                .setNegativeButton(R.string.colorpicker_dialog_cancel, (dialog, which) -> dialog.cancel());

        return dialogBuilder.create();
    }

    private PourOver createPourOver(Integer waterAmount, Integer timeInSeconds) {
        return new PourOver(timeInSeconds, waterAmount);
    }
}
