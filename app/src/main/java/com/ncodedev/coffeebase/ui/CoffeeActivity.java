package com.ncodedev.coffeebase.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
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
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.domain.Coffee;
import com.ncodedev.coffeebase.model.domain.Tag;
import com.ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import com.ncodedev.coffeebase.web.provider.CoffeeApiProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

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
                coffeeApiProvider.switchFavourites(coffeeId, this);
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
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> coffeeApiProvider.delete(coffeeId, this));
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
        coffeeRating.setRating(coffee.getRating().floatValue());
        txtCoffeeName.setText(coffee.getName());
        txtRoaster.setText(coffee.getRoaster());
        txtOrigin.setText(coffee.getOrigin());
        txtRegion.setText(coffee.getRegion());
        txtFarm.setText(coffee.getFarm());
        txtProcessing.setText(coffee.getProcessing());
        txtRoastProfile.setText(coffee.getRoastProfile());
        txtContinent.setText(coffee.getContinent());

        if (coffee.getCropHeight() != null) {
            txtCropHeight.setText(String.valueOf(coffee.getCropHeight()));
        }
        if (coffee.getScaRating() != null) {
            txtScaRating.setText(String.valueOf(coffee.getScaRating()));
        }

        Picasso.with(CoffeeActivity.this)
                .load(coffee.getImageUrl())
                .placeholder(R.mipmap.coffeebean)
                .into(imgCoffee);

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
    }
}