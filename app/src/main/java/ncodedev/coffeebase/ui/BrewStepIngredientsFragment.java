package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.validator.Validator;
import ncodedev.coffeebase.process.brewsteps.BrewStepHelper;
import ncodedev.coffeebase.ui.utility.BrewMapper;
import ncodedev.coffeebase.web.listener.BrewStepResponseListener;

import java.util.Optional;

public class BrewStepIngredientsFragment extends Fragment implements BrewStepResponseListener {

    private Brew brew;
    private TextView coffeeWeightTxt, grinderSettingTxt, waterAmountTxt, waterTempTxt, filterTxt;
    private ImageButton prevStepButton, nextStepButton;
    private ProgressBar progressBar;


    public BrewStepIngredientsFragment(Brew brew, ImageButton prevStepButton, ImageButton nextStepButton, ProgressBar progressBar) {
        this.brew = brew;
        this.prevStepButton = prevStepButton;
        this.nextStepButton = nextStepButton;
        this.progressBar = progressBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_brew_step_ingredients, container, false);

        initViews(view);
        setupStep();

        return view;
    }

    private void initViews(View view) {
        coffeeWeightTxt = view.findViewById(R.id.inputCoffeeWeight);
        grinderSettingTxt = view.findViewById(R.id.inputGrinderSetting);
        waterAmountTxt = view.findViewById(R.id.inputWaterAmount);
        waterTempTxt = view.findViewById(R.id.inputWaterTemperature);
        filterTxt = view.findViewById(R.id.inputFilter);
    }

    private void setupStep() {
        BrewStepHelper brewStepHelper = new BrewStepHelper();
        brewStepHelper.init(brew, this, this.getActivity());

        prevStepButton.setVisibility(View.VISIBLE);
        prevStepButton.setOnClickListener(view -> {
            FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.step_container, new BrewStepGeneralInfoFragment(brew, prevStepButton, nextStepButton, progressBar));
            transaction.commit();
        });

        nextStepButton.setOnClickListener(v -> executeFinishStep(coffeeWeightTxt, grinderSettingTxt, waterAmountTxt, waterTempTxt, filterTxt, brewStepHelper));
        nextStepButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        nextStepButton.setRotation(180);

        progressBar.setProgress(2);
    }

    private void executeFinishStep(TextView coffeeWeightTxt, TextView grinderSettingTxt, TextView waterAmountTxt, TextView waterTempTxt, TextView filterTxt, BrewStepHelper brewStepHelper) {
        if (!validate()) {
            return;
        }
        Brew brewWithIngredients = BrewMapper.mapIngredients(
                brew,
                coffeeWeightTxt.getText().toString(),
                grinderSettingTxt.getText().toString(),
                waterAmountTxt.getText().toString(),
                waterTempTxt.getText().toString(),
                filterTxt.getText().toString()
        );
        brewStepHelper.finish(brewWithIngredients, this, this.getActivity());
    }

    private boolean validate() {
        return Validator.numberFromTo(coffeeWeightTxt, 1, 250, getString(R.string.constraint_coffee_weight)) &&
                Validator.numberFromTo(grinderSettingTxt, 1, 100, getString(R.string.constraint_grinder_setting)) &&
                Validator.numberFromTo(waterAmountTxt, 1, 5000, getString(R.string.constraint_water_amount)) &&
                Validator.numberFromTo(waterTempTxt, 1, 100, getString(R.string.constraint_water_temp));
    }

    @Override
    public void handleInitBrewStepResponse(Brew brew) {
        this.brew =  brew;
        Optional.ofNullable(brew.getCoffeeWeightInGrams()).map(String::valueOf).ifPresent(coffeeWeightTxt::setText);
        Optional.ofNullable(brew.getGrinderSetting()).map(String::valueOf).ifPresent(grinderSettingTxt::setText);
        Optional.ofNullable(brew.getWaterAmountInMl()).map(String::valueOf).ifPresent(waterAmountTxt::setText);
        Optional.ofNullable(brew.getWaterTemp()).map(String::valueOf).ifPresent(waterTempTxt::setText);
        filterTxt.setText(brew.getFilter());
    }

    @Override
    public void handleFinishBrewStepResponse(Brew brew) {
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.step_container, new BrewStepPourOver(brew, prevStepButton, nextStepButton, progressBar));
        transaction.commit();
    }
}