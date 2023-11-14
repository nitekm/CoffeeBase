package ncodedev.coffeebase.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ncodedev.coffeebase.R;

public class BrewStepGeneralInfo extends Fragment {

    private GridView brewMethodGrid;
    private TextView brewMethodDisplayTxt;
    public BrewStepGeneralInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew_step_general_info, container, false);

        initViews(view);
//        setOnClickListenersOnGrid(brewMethodGrid);

        return view;
    }

    private void initViews(View view) {
        brewMethodGrid = view.findViewById(R.id.methodGrid);
        brewMethodGrid.setAdapter(new MethodSelector());
        brewMethodDisplayTxt = view.findViewById(R.id.methodNameDisplayTxt);
    }

    private void setOnClickListenersOnGrid(GridView brewMethodGrid) {
        brewMethodGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MethodSelector adapter = (MethodSelector) brewMethodGrid.getAdapter();
        //0 - aeropress
        brewMethodGrid.getChildAt(0).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.aeropress));
        //1 - auto drip
        brewMethodGrid.getChildAt(1).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.auto_drip));
        //2 - chemex
        brewMethodGrid.getChildAt(2).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.chemex));
        //3 - espresso
        brewMethodGrid.getChildAt(3).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.espresso));
        //4 - french press
        brewMethodGrid.getChildAt(4).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.french_press));
        //5 - mokka pot
        brewMethodGrid.getChildAt(5).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.mokka_pot));
        //6 - v60
        brewMethodGrid.getChildAt(6).setOnClickListener(view -> brewMethodDisplayTxt.setText(R.string.v60));
    }

    private class MethodSelector extends ArrayAdapter<ImageView> {
        private int[] items = {
                R.drawable.ic_aeropress,
                R.drawable.ic_auto_drip,
                R.drawable.ic_chemex,
                R.drawable.ic_espresso,
                R.drawable.ic_french_press,
                R.drawable.ic_mokka_pot,
                R.drawable.ic_v60,
        };

        public MethodSelector(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(items[position]);
            return imageView;
        }
    }
}