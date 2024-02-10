package ncodedev.coffeebase.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.error.ErrorResponse;
import ncodedev.coffeebase.service.ErrorMessageTranslator;
import ncodedev.coffeebase.ui.activity.CoffeeActivity;
import ncodedev.coffeebase.ui.view.adapter.BrewRecyclerViewAdapter;
import ncodedev.coffeebase.utils.ToastUtils;
import ncodedev.coffeebase.web.listener.BrewResponseListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ncodedev.coffeebase.ui.activity.EditCoffee.COFFEE_ID_KEY;

public class DisplayAllBrewsDialog extends DialogFragment implements BrewResponseListener {

    private List<Brew> brews;
    private long coffeeId;
    private RecyclerView recyclerView;

    public DisplayAllBrewsDialog(List<Brew> brews, long coffeeId) {
        this.brews = brews;
        this.coffeeId = coffeeId;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.display_all_brews_dialog, null);

        recyclerView = view.findViewById(R.id.brewRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        BrewRecyclerViewAdapter brewRecyclerViewAdapter = new BrewRecyclerViewAdapter(getActivity(), brews, coffeeId, "DISPLAY_BREWS_DIALOG");
        recyclerView.setAdapter(brewRecyclerViewAdapter);

        dialogBuilder.setView(view)
                .setTitle(getString(R.string.add_brew_to_coffee))
                .setNegativeButton(getString(R.string.dismiss), (dialog, which) -> dialog.cancel());

        return dialogBuilder.create();

    }

    @Override
    public void handleExecuteActionResult() {
        Intent intent = new Intent(this.getActivity(), CoffeeActivity.class);
        intent.putExtra(COFFEE_ID_KEY, coffeeId);
        startActivity(intent);
    }

    @Override
    public void handleResponseError(ErrorResponse errorResponse) {
        ErrorMessageTranslator.tranlateAndToastErrorMessage(getActivity(), errorResponse);
    }

    @Override
    public void handleCallFailed() {
        ToastUtils.showToast(getActivity(), getString(R.string.server_unavailable_retrying));
    }
}
