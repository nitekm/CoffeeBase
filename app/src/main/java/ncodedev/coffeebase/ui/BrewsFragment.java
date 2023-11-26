package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.ui.utility.BrewRecyclerViewAdapter;
import ncodedev.coffeebase.web.listener.BrewListResponseListener;
import ncodedev.coffeebase.web.provider.BrewApiProvider;

import java.util.List;

public class BrewsFragment extends Fragment implements BrewListResponseListener {

    private RecyclerView recyclerView;
    private ImageButton addBrewBtn;
    private final List<Brew> brews;
    private final Long coffeeId;
    private final BrewApiProvider brewApiProvider = BrewApiProvider.getInstance();

    public BrewsFragment(List<Brew> brews, Long coffeeId) {
        this.brews = brews;
        this.coffeeId = coffeeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brews, container, false);

        initViews(view);
        loadBrewsInfo(brews);

        addBrewBtn = view.findViewById(R.id.btnAddBrewToCoffee);
        addBrewBtn.setOnClickListener(v -> brewApiProvider.getAll(this, getActivity()));

        return view;
    }

    private void initViews(View view) {

        recyclerView = view.findViewById(R.id.brewRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void loadBrewsInfo(List<Brew> brews) {
        BrewRecyclerViewAdapter brewRecyclerViewAdapter = new BrewRecyclerViewAdapter(getContext(), brews, coffeeId, "BREWS_FRAGMENT");
        recyclerView.setAdapter(brewRecyclerViewAdapter);
    }

    @Override
    public void handleGetList(List<Brew> allBrews) {
        DisplayAllBrewsDialog displayAllBrewsDialog = new DisplayAllBrewsDialog(allBrews, coffeeId);
        displayAllBrewsDialog.show(getChildFragmentManager(), "DisplayAllBrewsDialogFragment");
    }
}