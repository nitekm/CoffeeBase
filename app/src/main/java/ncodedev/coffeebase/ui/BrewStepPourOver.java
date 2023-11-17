package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.PourOver;
import ncodedev.coffeebase.process.brewsteps.BrewStepHelper;
import ncodedev.coffeebase.ui.utility.PourOverRecyclerViewAdapter;
import ncodedev.coffeebase.web.listener.BrewStepResponseListener;

import java.util.ArrayList;
import java.util.List;

public class BrewStepPourOver extends Fragment implements BrewStepResponseListener {

    private Brew brew;
    private ImageButton prevStepButton, nextStepButton, btnAddPourOver;
    private RecyclerView recyclerView;
    private PourOverRecyclerViewAdapter pourOverRecyclerViewAdapter;
    private List<PourOver> pourOvers;

    public BrewStepPourOver(Brew brew, ImageButton prevStepButton, ImageButton nextStepButton) {
        this.brew = brew;
        this.prevStepButton = prevStepButton;
        this.nextStepButton = nextStepButton;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew_step_pour_over, container, false);

        initViews(view);
        setupStep();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.pourOverRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pourOvers = new ArrayList<>();
        pourOverRecyclerViewAdapter = new PourOverRecyclerViewAdapter(pourOvers);
        recyclerView.setAdapter(pourOverRecyclerViewAdapter);
        btnAddPourOver = view.findViewById(R.id.btnAddPourOver);
        btnAddPourOver.setOnClickListener(v -> {
            AddPourOverDialogFragment addPourOverDialogFragment = new AddPourOverDialogFragment(pourOvers, pourOverRecyclerViewAdapter);
            addPourOverDialogFragment.show(getChildFragmentManager(), "AddPourOverDialogFragment");
        });

    }

    private void setupStep() {
        BrewStepHelper brewStepHelper = new BrewStepHelper();
        brewStepHelper.init(brew, this, this.getActivity());
        prevStepButton.setOnClickListener(view -> {
            FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.step_container, new BrewStepIngredientsFragment(brew, prevStepButton, nextStepButton));
            transaction.commit();
        });
        nextStepButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_confirm));
        nextStepButton.setRotation(0);
        nextStepButton.setOnClickListener(v -> executeFinishStep(brewStepHelper));
    }

    private void executeFinishStep(BrewStepHelper brewStepHelper) {
        
    }

    @Override
    public void handleInitBrewStepResponse(Brew brew) {
        this.brew = brew;
    }

    @Override
    public void handleFinishBrewStepResponse(Brew brew) {

    }
}