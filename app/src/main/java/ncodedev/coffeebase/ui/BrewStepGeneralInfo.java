package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import ncodedev.coffeebase.R;

import java.util.ArrayList;
import java.util.List;

public class BrewStepGeneralInfo extends Fragment {

    private TextView brewMethodDisplayTxt;

    public BrewStepGeneralInfo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew_step_general_info, container, false);

        GridView brewMethodGrid = view.findViewById(R.id.methodGrid);
        brewMethodDisplayTxt = view.findViewById(R.id.methodNameDisplayTxt);
        brewMethodGrid.setAdapter(new MethodSelector(brewMethodDisplayTxt));
        return view;
    }

    private class MethodSelector extends BaseAdapter {

        private TextView brewMethodDisplayTxt;
        private List<ImageView> methodIcons = new ArrayList<>();
        private int[] items = {
                R.drawable.ic_aeropress,
                R.drawable.ic_auto_drip,
                R.drawable.ic_chemex,
                R.drawable.ic_espresso,
                R.drawable.ic_french_press,
                R.drawable.ic_mokka_pot,
                R.drawable.ic_v60,
        };

        public MethodSelector(TextView brewMethodDisplayTxt) {
            this.brewMethodDisplayTxt = brewMethodDisplayTxt;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(195, 195));
                imageView.setPadding(10, 10, 10, 10);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(items[position]);
            setOnLickListeners(position, imageView);
            methodIcons.add(imageView);
            return imageView;
        }

        private void setOnLickListeners(int position, ImageView imageView) {
            switch (position) {
                case 0 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.aeropress));
                case 1 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.auto_drip));
                case 2 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.chemex));
                case 3 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.espresso));
                case 4 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.french_press));
                case 5 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.mokka_pot));
                case 6 -> imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.v60));
            }
        }

        private void setIconSelectedBehaviour(ImageView imageView, int textResourceId) {
            methodIcons.forEach(icon -> icon.setBackgroundResource(0));
            imageView.setBackgroundResource(R.drawable.roundcorner);
            brewMethodDisplayTxt.setText(textResourceId);
        }
    }
}