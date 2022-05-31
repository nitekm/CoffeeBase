package com.ncode.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.ncode.coffeebase.R;
import com.ncode.coffeebase.model.Coffee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.math.BigDecimal;
import java.util.Objects;

import static com.ncode.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncode.coffeebase.utils.ToastUtils.showToast;

public class EditCoffee extends AppCompatActivity {

    public static final String COFFEE_ID_KEY = "coffeeId";
    private int coffeeId;

    private Coffee coffee;
    private ImageView imgCoffee;
    private Button addImageBtn, saveBtn;
    private RatingBar coffeeRatingBar;
    private TextInputEditText inputCoffeeName, inputOrigin, inputRoaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coffee);

        initViews();
        if (isCoffeeEdited()) {
            getSingleCoffee(coffeeId);
            saveBtn.setOnClickListener(view -> editCoffee(coffeeId));
        } else {
            saveBtn.setOnClickListener(view -> addCoffee());
        }
    }

    private void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        addImageBtn = findViewById(R.id.addImageBtn);
        saveBtn = findViewById(R.id.saveBtn);
        coffeeRatingBar = findViewById(R.id.coffeeRatingBar);
        inputCoffeeName = findViewById(R.id.inputCoffeeName);
        inputOrigin = findViewById(R.id.inputOrigin);
        inputRoaster = findViewById(R.id.inputRoaster);
    }

    private boolean isCoffeeEdited() {
        Intent intent = getIntent();
        if (null != intent) {
            coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            return coffeeId != -1;
        }
        return false;
    }

    private void getSingleCoffee(int coffeeId) {
        Call<Coffee> call = createCoffeeApi().getSingleCoffee(coffeeId);

        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                coffee = response.body();
                inputCoffeeName.setText(coffee.getName());
                inputOrigin.setText(coffee.getOrigin());
                inputRoaster.setText(coffee.getRoaster());
                coffeeRatingBar.setRating(coffee.getRating().floatValue());
                Glide.with(EditCoffee.this)
                        .asBitmap()
                        .load(coffee.getImageUrl())
                        .placeholder(R.mipmap.coffeebean)
                        .into(imgCoffee);
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                showToast(EditCoffee.this, "Something went wrong");
            }
        });
    }

    private void editCoffee(int id) {
        Coffee coffee = createCoffee();
        Call<Void> call = createCoffeeApi().updateCoffee(id, coffee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                showToast(EditCoffee.this, "Changes saved");
                Intent intent = new Intent(EditCoffee.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                showToast(EditCoffee.this, "Something went wrong");
            }
        });
    }

    private void addCoffee() {
        Coffee coffee = createCoffee();
        Call<Coffee> call = createCoffeeApi().createCoffee(coffee);

        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                showToast(EditCoffee.this, "Coffee added!");
                Intent intent = new Intent(EditCoffee.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                showToast(EditCoffee.this, "Something went wrong");
            }
        });
    }

    private Coffee createCoffee() {
        String name = Objects.requireNonNull(inputCoffeeName.getText()).toString();
        String origin = Objects.requireNonNull(inputOrigin.getText()).toString();
        String roaster = Objects.requireNonNull(inputRoaster.getText()).toString();
        BigDecimal rating = BigDecimal.valueOf(coffeeRatingBar.getRating());

        return new Coffee(name, origin, roaster, rating);
    }
}