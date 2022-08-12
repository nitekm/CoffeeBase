package com.ncodedev.coffeebase.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.material.textfield.TextInputEditText;
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.Coffee;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.ncodedev.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncodedev.coffeebase.utils.Logger.logCall;
import static com.ncodedev.coffeebase.utils.Logger.logCallFail;
import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;

public class CoffeeActivity extends AppCompatActivity {
    public static final String COFFEE_ID_KEY = "coffeeId";
    private static final String TAG = "CoffeeActivity";
    private Coffee coffee;
    private int coffeeId;
    private MaterialToolbar toolbar;
    private ActionMenuItemView favouriteMenuItem;
    private ImageView imgCoffee;
    private TextView txtCoffeeName;
    private TextInputEditText txtRoaster;
    private TextView txtOrigin;
    private RatingBar coffeeRating;


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
        txtOrigin = findViewById(R.id.txtOrigin);
        txtRoaster = findViewById(R.id.txtRoaster);
        coffeeRating = findViewById(R.id.coffeeRating);
        toolbar = findViewById(R.id.topAppBar);
        favouriteMenuItem = findViewById(R.id.favouritesMenuItem);
    }

    private void setToolbar() {
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(CoffeeActivity.this, MainActivity.class);
            startActivity(intent);
        });
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onMenuItemClick(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favouritesMenuItem:
                addToFavourites(coffeeId);
                Log.d(TAG, "addToFavouritesMenuItem clicked");
                return true;
            case R.id.editMenuItem:
                editCoffee(coffeeId);
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

    private void addToFavourites(int coffeeId) {
        Call<Void> call = createCoffeeApi().switchFavourite(coffeeId);
        logCall(TAG, call);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                refresh();
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                showToast(CoffeeActivity.this, "Something went wrong!");
                logCallFail(TAG, call);
            }
        });
    }

    private void refresh() {
        finish();
        startActivity(getIntent());
    }

    private void editCoffee(int coffeeId) {
        Intent intent = new Intent(CoffeeActivity.this, EditCoffee.class);
        intent.putExtra(COFFEE_ID_KEY, coffeeId);
        startActivity(intent);
    }

    private void showDeleteDialog(int coffeeId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoffeeActivity.this);
        alertDialogBuilder.setTitle("Delete this coffee?");
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> { });
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> deleteCoffee(coffeeId));
        alertDialogBuilder.create().show();
    }

    private void deleteCoffee(final int coffeeId) {
        Call<Void> call = createCoffeeApi().deleteCoffee(coffeeId);
        logCall(TAG, call);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                showToast(CoffeeActivity.this, "Coffee deleted!");
                Intent intent = new Intent(CoffeeActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                showToast(CoffeeActivity.this, "Something went wrong!");
                logCallFail(TAG, call);
            }
        });
    }

    private void showCoffeeInfo() {
        Intent intent = getIntent();
        if (null != intent) {
            coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            if (coffeeId != -1) {
                getSingleCoffee(coffeeId);
            }
        }
    }

    private void getSingleCoffee(int coffeeId) {
        Call<Coffee> call = createCoffeeApi().getSingleCoffee(coffeeId);
        logCall(TAG, call);
        call.enqueue(new Callback<Coffee>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (!response.isSuccessful()) {
                    showToast(CoffeeActivity.this, "Something went wrong");
                }
                coffee = response.body();
                txtCoffeeName.setText(coffee.getName());
                txtOrigin.setText(coffee.getOrigin());
                txtRoaster.setText(coffee.getRoaster());
                coffeeRating.setRating(coffee.getRating().floatValue());
                try {
                    InputStream is = getContentResolver().openInputStream(Uri.parse((coffee.getImageUrl())));
                    System.out.println(is);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Picasso.with(CoffeeActivity.this)
                        .load(coffee.getImageUrl())
                        .placeholder(R.mipmap.coffeebean)
                        .into(imgCoffee);
                if (coffee.isFavourite()) {
                    favouriteMenuItem.setIcon(getDrawable(R.drawable.ic_favorite_filled));
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                showToast(CoffeeActivity.this, "Something went wrong");
                logCallFail(TAG, call);
            }
        });
    }
}