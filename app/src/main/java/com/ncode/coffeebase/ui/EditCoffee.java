package com.ncode.coffeebase.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ncode.coffeebase.R;
import com.ncode.coffeebase.model.Coffee;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.math.BigDecimal;
import java.util.Objects;

import static com.ncode.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncode.coffeebase.utils.Global.USER_ID;
import static com.ncode.coffeebase.utils.PermissionsUtils.checkCameraPermission;
import static com.ncode.coffeebase.utils.PermissionsUtils.checkStoragePermission;
import static com.ncode.coffeebase.utils.ToastUtils.showToast;

public class EditCoffee extends AppCompatActivity {
    private static final String TAG = "EditCoffee";
    public static final String COFFEE_ID_KEY = "coffeeId";
    private int coffeeId;
    private MaterialToolbar toolbar;
    private Coffee coffee;
    private ImageView imgCoffee;
    private String imageUri;
    private Button addImageBtn, saveBtn;
    private RatingBar coffeeRatingBar;
    private TextInputEditText inputCoffeeName, inputOrigin, inputRoaster;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coffee);

        initViews();
        //TODO: to method
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(EditCoffee.this, MainActivity.class);
            startActivity(intent);
        });
        addImageBtn.setOnClickListener(view -> addImage());

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
        toolbar = findViewById(R.id.topAppBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addImage() {
        boolean addImageChoice = true;
        if (addImageChoice) {
            if (!checkCameraPermission(this) || !checkStoragePermission(this)) {
                requestCameraPermission();
            } else {
                pickImage();
            }
        } else {
            if (!checkStoragePermission(this)) {
                requestStoragePermission();
            } else {
                pickImage();
            }
        }
    }
    //TODO: unnacesary method
    private void pickImage() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(imgCoffee);
                imageUri = resultUri.toString();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
                Picasso.with(EditCoffee.this)
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

        Log.d(TAG, " Object: Coffee[" + name + ", " + origin + ", " + roaster + ", " + rating + ", " + imageUri + ", " + USER_ID + "]");
        return new Coffee(name, origin, roaster, rating, imageUri, USER_ID);
    }
}