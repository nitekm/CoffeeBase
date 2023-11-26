package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.validator.Validator;
import ncodedev.coffeebase.process.brewsteps.BrewStepHelper;
import ncodedev.coffeebase.ui.utility.BrewMapper;
import ncodedev.coffeebase.web.listener.BrewStepResponseListener;

import java.util.ArrayList;
import java.util.List;

public class BrewStepGeneralInfoFragment extends Fragment implements BrewStepResponseListener {

    private TextView brewNameTxt, brewMethodDisplayTxt;
    private ImageButton prevStepButton, nextStepButton;
    private ProgressBar progressBar;
    private Brew brew;

    public BrewStepGeneralInfoFragment(Brew brew, ImageButton prevStepButton, ImageButton nextStepButton, ProgressBar progressBar) {
        this.brew = brew;
        this.prevStepButton = prevStepButton;
        this.nextStepButton = nextStepButton;
        this.progressBar = progressBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew_step_general_info, container, false);

        initViews(view);
        setupStep();
        return view;
    }

    private void initViews(View view) {
        GridView brewMethodGrid = view.findViewById(R.id.methodGrid);
        brewMethodDisplayTxt = view.findViewById(R.id.methodNameDisplayTxt);
        brewMethodGrid.setAdapter(new MethodSelector(brewMethodDisplayTxt));
        brewNameTxt = view.findViewById(R.id.inputBrewName);
    }

    private void setupStep() {
        BrewStepHelper brewStepHelper = new BrewStepHelper();
        brewStepHelper.init(brew, this, this.getActivity());

        prevStepButton.setVisibility(View.INVISIBLE);
        prevStepButton.setClickable(false);

        nextStepButton.setOnClickListener(v -> executeFinishStep(brewNameTxt, brewMethodDisplayTxt, brewStepHelper));

        progressBar.setProgress(1);
    }

    private void executeFinishStep(TextView brewNameTxt, TextView brewMethodDisplayTxt, BrewStepHelper brewStepHelper) {
        if (!validateBrew()) {
            return;
        }
        Brew brewWithGeneralInfo = BrewMapper.mapGeneralInfo(brew, brewNameTxt.getText().toString(), brewMethodDisplayTxt.getText().toString());
        brewStepHelper.finish(brewWithGeneralInfo, this, this.getActivity());
    }

    private boolean validateBrew() {
        return Validator.textNotBlank(brewNameTxt, getString(R.string.constraint_brew_name_not_empty));
    }

    @Override
    public void handleInitBrewStepResponse(Brew brew) {
        this.brew =  brew;
        brewNameTxt.setText(brew.getName());
        brewMethodDisplayTxt.setText(brew.getMethod());
    }

    @Override
    public void handleFinishBrewStepResponse(Brew brew) {
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.step_container, new BrewStepIngredientsFragment(brew, prevStepButton, nextStepButton, progressBar));
        transaction.commit();
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

        private MethodSelector(TextView brewMethodDisplayTxt) {
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
                imageView.setLayoutParams(new GridView.LayoutParams(175, 175));
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
                case 4 ->
                        imageView.setOnClickListener(view -> setIconSelectedBehaviour(imageView, R.string.french_press));
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