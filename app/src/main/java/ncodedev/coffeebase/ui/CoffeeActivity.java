package ncodedev.coffeebase.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.domain.Tag;
import ncodedev.coffeebase.ui.utility.CoffeeActivityViewAdjuster;
import ncodedev.coffeebase.ui.utility.ImageHelper;
import ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ncodedev.coffeebase.ui.utility.CoffeeActivityViewAdjuster.*;
import static ncodedev.coffeebase.utils.Utils.imageDownloadUrl;

public class CoffeeActivity extends AppCompatActivity implements CoffeeResponseListener {
    private static final String TAG = "CoffeeActivity";
    public static final String COFFEE_ID_KEY = "coffeeId";
    private int coffeeId;
    private MaterialToolbar toolbar;
    private ActionMenuItemView favouriteMenuItem;
    private ImageView imgCoffee;
    private TextView txtCoffeeName;
    private TextInputEditText txtRoaster, txtOrigin, txtRegion, txtFarm, txtCropHeight, txtProcessing, txtScaRating, txtContinent, txtRoastProfile;
    private RatingBar coffeeRating;
    private ChipGroup tagChipGroup;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();
    private final ImageHelper imageHelper = ImageHelper.getInstance();

    private final List<TextInputLayout> inputLayouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        initViews();
        setToolbar();
        showCoffeeInfo();
    }

    private void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        txtCoffeeName = findViewById(R.id.txtCoffeeName);
        txtRoaster = findViewById(R.id.txtRoaster);
        txtOrigin = findViewById(R.id.txtOrigin);
        txtRegion = findViewById(R.id.txtRegion);
        txtFarm = findViewById(R.id.txtFarm);
        txtCropHeight = findViewById(R.id.txtCropHeight);
        txtProcessing = findViewById(R.id.txtProcessing);
        txtScaRating = findViewById(R.id.txtScaScore);
        txtContinent = findViewById(R.id.txtContinent);
        txtRoastProfile = findViewById(R.id.txtRoastProfile);
        coffeeRating = findViewById(R.id.coffeeRating);
        toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        favouriteMenuItem = findViewById(R.id.favouritesMenuItem);
        tagChipGroup = findViewById(R.id.displayChipGroup);

        inputLayouts.add(findViewById(R.id.roasterOutputLayout));
        inputLayouts.add(findViewById(R.id.roastProfileOutputLayout));
        inputLayouts.add(findViewById(R.id.continentOutputLayout));
        inputLayouts.add(findViewById(R.id.originOutputLayout));
        inputLayouts.add(findViewById(R.id.regionOutputLayout));
        inputLayouts.add(findViewById(R.id.farmOutputLayout));
        inputLayouts.add(findViewById(R.id.cropHeightOutputLayout));
        inputLayouts.add(findViewById(R.id.processingOutputLayout));
        inputLayouts.add(findViewById(R.id.scaScoreOutputLayout));
    }



    //TOOLBAR - START -----------------------------------------------------------------------------------------------\\
    private void setToolbar() {
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onMenuItemClick(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favouritesMenuItem:
                coffeeApiProvider.switchFavourites(coffeeId, this, this);
                finish();
                startActivity(getIntent());
                Log.d(TAG, "addToFavouritesMenuItem clicked");
                return true;
            case R.id.editMenuItem:
                Intent intent = new Intent(this, EditCoffee.class);
                intent.putExtra(COFFEE_ID_KEY, coffeeId);
                startActivity(intent);
                Log.d(TAG, "editMenuItem clicked");
                return true;
            case R.id.deleteMenuItem:
                showDeleteDialog(coffeeId);
                Log.d(TAG, "deleteMenuItem clicked");
                return true;
            default:
                return true;
        }
    }

    private void showDeleteDialog(int coffeeId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete this coffee?");
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {});
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            coffeeApiProvider.delete(coffeeId, this);
            new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(new Intent(this, MainActivity.class)), 750);
        });
        alertDialogBuilder.create().show();

    }
    //TOOLBAR - END -------------------------------------------------------------------------------------------------\\



    private void showCoffeeInfo() {
        Intent intent = getIntent();
        if (null != intent) {
            coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            if (coffeeId != -1) {
                coffeeApiProvider.getOne(coffeeId, this, this);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleCoffeeResponse(final Coffee coffee) {
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

        if (coffee.isFavourite()) {
            favouriteMenuItem.setIcon(getDrawable(R.drawable.ic_favorite_filled));
        }

        List<Tag> tags = coffee.getTags();
        tags.forEach(tag -> {
            Chip chip = new Chip(CoffeeActivity.this);
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
