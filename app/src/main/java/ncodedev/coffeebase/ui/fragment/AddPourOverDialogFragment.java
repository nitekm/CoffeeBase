package ncodedev.coffeebase.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.PourOver;
import ncodedev.coffeebase.model.validator.Validator;
import ncodedev.coffeebase.ui.view.adapter.PourOverRecyclerViewAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddPourOverDialogFragment extends DialogFragment {

    private List<PourOver> pourOvers;
    private PourOverRecyclerViewAdapter pourOverRecyclerViewAdapter;
    private TextView waterAmount, timeInSeconds;
    private Brew brew;

    public AddPourOverDialogFragment(Brew brew, List<PourOver> pourOvers, PourOverRecyclerViewAdapter pourOverRecyclerViewAdapter) {
        this.brew = brew;
        this.pourOvers = pourOvers;
        this.pourOverRecyclerViewAdapter = pourOverRecyclerViewAdapter;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.add_pour_over_dialog, null);
        waterAmount = view.findViewById(R.id.waterAmountInPourTxt);
        timeInSeconds = view.findViewById(R.id.pourTimeTxt);
        AlertDialog addPourOverDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_pour)
                .setPositiveButton("OK", null)
                .setNegativeButton(R.string.colorpicker_dialog_cancel, (dialog, which) -> dialog.cancel())
                .create();

        addPourOverDialog.setOnShowListener(dialog -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> validateAndAddPourOver(pourOvers, waterAmount, timeInSeconds, dialog));
        });

        return addPourOverDialog;
    }

    private void validateAndAddPourOver(List<PourOver> pourOvers, TextView waterAmount, TextView timeInSeconds, DialogInterface dialog) {
        if (!validatePour()) {
            return;
        }
        pourOvers.add(createPourOver(
                Integer.parseInt(waterAmount.getText().toString()),
                Long.parseLong(timeInSeconds.getText().toString())
        ));
        pourOverRecyclerViewAdapter.notifyItemInserted(pourOvers.size() -1);
        dialog.dismiss();
    }

    private PourOver createPourOver(Integer waterAmount, Long timeInSeconds) {
        return new PourOver(new Brew(brew.getId()), timeInSeconds, waterAmount);
    }

    private boolean validatePour() {
        return Validator.textNotBlank(waterAmount, getString(R.string.constraint_pour_water_amount)) &&
                Validator.numberFromTo(waterAmount, 1, 1500, getString(R.string.constraint_pour_water_amount)) &&
                Validator.textNotBlank(timeInSeconds, getString(R.string.constraint_pourover_time)) &&
                Validator.numberFromTo(timeInSeconds, 1, 600, getString(R.string.constraint_pourover_time));
    }
}
