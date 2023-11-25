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
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.PourOver;
import ncodedev.coffeebase.model.validator.Validator;
import ncodedev.coffeebase.ui.utility.PourOverRecyclerViewAdapter;
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.add_pour_over_dialog, null);

        waterAmount = view.findViewById(R.id.waterAmountInPourTxt);
        timeInSeconds = view.findViewById(R.id.pourTimeTxt);
        dialogBuilder.setView(view)
                .setTitle(R.string.add_pour)
                .setPositiveButton("OK", (dialog, which) -> validateAndAddPourOver(pourOvers, waterAmount, timeInSeconds))
                .setNegativeButton(R.string.colorpicker_dialog_cancel, (dialog, which) -> dialog.cancel());

        return dialogBuilder.create();
    }

    private void validateAndAddPourOver(List<PourOver> pourOvers, TextView waterAmount, TextView timeInSeconds) {
        if (!validatePour()) {
            return;
        }
        pourOvers.add(createPourOver(
                Integer.parseInt(waterAmount.getText().toString()),
                Integer.parseInt(timeInSeconds.getText().toString())
        ));
    }

    private PourOver createPourOver(Integer waterAmount, Integer timeInSeconds) {
        return new PourOver(new Brew(brew.getId()), timeInSeconds, waterAmount);
    }

    private boolean validatePour() {
        return Validator.textNotBlank(waterAmount, getString(R.string.constraint_pour_water_amount)) &&
                Validator.numberFromTo(waterAmount, 1, 1500, getString(R.string.constraint_pour_water_amount)) &&
                Validator.textNotBlank(timeInSeconds, getString(R.string.constraint_pourover_time)) &&
                Validator.numberFromTo(timeInSeconds, 1, 600, getString(R.string.constraint_pourover_time));
    }
}
