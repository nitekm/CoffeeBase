package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.ui.utility.BrewRecyclerViewAdapter;

import java.util.List;

public class BrewsFragment extends Fragment {

    private RecyclerView recyclerView;

    private final List<Brew> brews;
    private final Integer coffeeId;

    public BrewsFragment(List<Brew> brews, Integer coffeeId) {
        this.brews = brews;
        this.coffeeId = coffeeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brews, container, false);

        recyclerView = view.findViewById(R.id.brewRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        loadBrewsInfo(brews);

        return view;
    }

    private void loadBrewsInfo(List<Brew> brews) {
        BrewRecyclerViewAdapter brewRecyclerViewAdapter = new BrewRecyclerViewAdapter(getContext(), brews, coffeeId);
        recyclerView.setAdapter(brewRecyclerViewAdapter);
    }
}