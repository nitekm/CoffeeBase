package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import ncodedev.coffeebase.R;

public class BrewStepGeneralInfo extends Fragment {

    public BrewStepGeneralInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew_step_general_info, container, false);

        //TODO: rename
        GridView gridView = view.findViewById(R.id.methodGrid);
        gridView.setAdapter(new MethodSelector());

        return view;
    }

    private class MethodSelector extends BaseAdapter {
        private int[] items = {
                R.drawable.ic_account,
                R.drawable.ic_info,
                R.drawable.ic_water_drop,
                R.drawable.ic_water_drop,
                R.drawable.ic_water_drop,
                R.drawable.ic_water_drop,
                R.drawable.ic_water_drop,
                R.drawable.ic_water_drop
        };

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {return null;}

        @Override
        public long getItemId(int position) {return 0;}

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