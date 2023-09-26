package ncodedev.coffeebase.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.domain.Tag;
import ncodedev.coffeebase.ui.utility.ImageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ncodedev.coffeebase.ui.utility.CoffeeActivityViewAdjuster.hideBlankTextViewsAndAdjustConstraints;
import static ncodedev.coffeebase.utils.Utils.imageDownloadUrl;


public class CoffeeInfo extends Fragment {

    private ImageView imgCoffee;
    private TextView txtCoffeeName;
    private TextInputEditText txtRoaster, txtOrigin, txtRegion, txtFarm, txtCropHeight, txtProcessing, txtScaRating, txtContinent, txtRoastProfile;
    private RatingBar coffeeRating;
    private ChipGroup tagChipGroup;
    private final ImageHelper imageHelper = ImageHelper.getInstance();
    private final List<TextInputLayout> inputLayouts = new ArrayList<>();
    private final Coffee coffee;

    public CoffeeInfo(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coffee_info, container, false);

        imgCoffee = view.findViewById(R.id.imgCoffee);
        txtCoffeeName = view.findViewById(R.id.txtCoffeeName);
        txtRoaster = view.findViewById(R.id.txtRoaster);
        txtOrigin = view.findViewById(R.id.txtOrigin);
        txtRegion = view.findViewById(R.id.txtRegion);
        txtFarm = view.findViewById(R.id.txtFarm);
        txtCropHeight = view.findViewById(R.id.txtCropHeight);
        txtProcessing = view.findViewById(R.id.txtProcessing);
        txtScaRating = view.findViewById(R.id.txtScaScore);
        txtContinent = view.findViewById(R.id.txtContinent);
        txtRoastProfile = view.findViewById(R.id.txtRoastProfile);
        coffeeRating = view.findViewById(R.id.coffeeRating);
        tagChipGroup = view.findViewById(R.id.displayChipGroup);

        inputLayouts.add(view.findViewById(R.id.roasterOutputLayout));
        inputLayouts.add(view.findViewById(R.id.roastProfileOutputLayout));
        inputLayouts.add(view.findViewById(R.id.continentOutputLayout));
        inputLayouts.add(view.findViewById(R.id.originOutputLayout));
        inputLayouts.add(view.findViewById(R.id.regionOutputLayout));
        inputLayouts.add(view.findViewById(R.id.farmOutputLayout));
        inputLayouts.add(view.findViewById(R.id.cropHeightOutputLayout));
        inputLayouts.add(view.findViewById(R.id.processingOutputLayout));
        inputLayouts.add(view.findViewById(R.id.scaScoreOutputLayout));

        loadCoffeeData(coffee);

        return view;
    }
    
    private void loadCoffeeData(final Coffee coffee) {
        txtCoffeeName.setText(coffee.getName());
        txtRoaster.setText(coffee.getRoaster());
        txtOrigin.setText(coffee.getOrigin());
        txtRegion.setText(coffee.getRegion());
        txtFarm.setText(coffee.getFarm());
        txtProcessing.setText(coffee.getProcessing());
        txtRoastProfile.setText(coffee.getRoastProfile());
        txtContinent.setText(coffee.getContinent());

        Optional.ofNullable(coffee.getRating()).ifPresent(rating -> coffeeRating.setRating(rating.floatValue()));
        Optional.ofNullable(coffee.getCropHeight()).ifPresent(cropHeight -> txtCropHeight.setText(String.valueOf(cropHeight)));
        Optional.ofNullable(coffee.getScaRating()).ifPresent(sca -> txtScaRating.setText(String.valueOf(sca)));

        if (coffee.getCoffeeImageName() != null) {
            imageHelper.picassoSetImage(imageDownloadUrl(coffee.getCoffeeImageName()),
                    imgCoffee,
                    R.mipmap.coffeebean);
        } else {
            imgCoffee.setImageResource(R.mipmap.coffeebean);
        }

        List<Tag> tags = coffee.getTags();
        tags.forEach(tag -> {
            Chip chip = new Chip(getActivity());
            chip.setText(tag.getName());
            chip.setChipBackgroundColor(ColorStateList.valueOf(Integer.parseInt(tag.getColor())));

            chip.setCloseIconVisible(false);
            chip.setClickable(false);
            chip.setChipIconVisible(false);
            chip.setCheckable(false);

            tagChipGroup.addView(chip);
        });

        hideBlankTextViewsAndAdjustConstraints(inputLayouts, tagChipGroup.getId());
    }
}