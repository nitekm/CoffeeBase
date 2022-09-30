package com.ncodedev.coffeebase.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.Coffee;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ncodedev.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncodedev.coffeebase.utils.Global.USER_ID;
import static com.ncodedev.coffeebase.utils.Logger.logCall;
import static com.ncodedev.coffeebase.utils.Logger.logCallFail;
import static com.ncodedev.coffeebase.utils.PermissionsUtils.checkReadStoragePermission;
import static com.ncodedev.coffeebase.utils.PermissionsUtils.checkWriteStoragePermission;
import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;

public class EditCoffee extends AppCompatActivity {

    private boolean isReadPermissionGranted = false;
    private boolean isWritePermissionGranted = false;
    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    ActivityResultLauncher<Intent> mGetPhotoImage;
    ActivityResultLauncher<Intent> mGetGalleryImage;
    public static final String COFFEE_ID_KEY = "coffeeId";
    private static final String TAG = "EditCoffee";
    private Coffee coffee;
    private int coffeeId;
    private Uri imageUri;
    private MaterialToolbar toolbar;
    private ImageView imgCoffee;
    private Button addImageBtn, saveBtn;
    private RatingBar coffeeRatingBar;
    private TextInputEditText inputCoffeeName, inputOrigin, inputRoaster;

    private Dialog imageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coffee);

        initViews();
        determineContext();
        getPermissions();
        getCoffeePhotoImage();
        getCoffeeGalleryImage();
    }

    private void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        if (imageUri == null) {
            imgCoffee.setImageResource(R.mipmap.coffeebean);
        }
        addImageBtn = findViewById(R.id.addImageBtn);
        addImageBtn.setOnClickListener(view -> showAddImageDialog());
        saveBtn = findViewById(R.id.saveBtn);
        coffeeRatingBar = findViewById(R.id.coffeeRatingBar);
        inputCoffeeName = findViewById(R.id.inputCoffeeName);
        inputOrigin = findViewById(R.id.inputOrigin);
        inputRoaster = findViewById(R.id.inputRoaster);
        toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(EditCoffee.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void showAddImageDialog() {
        imageDialog = new Dialog(EditCoffee.this);
        imageDialog.setContentView(R.layout.imagedialog);
        ImageButton btnPhotoCamera = imageDialog.findViewById(R.id.btnPhotoCamera);
        ImageButton btnPhotoLibrary = imageDialog.findViewById(R.id.btnPhotoLibrary);
        btnPhotoLibrary.setOnClickListener(view -> {
            mGetGalleryImage.launch(new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            imageDialog.hide();
        });
        btnPhotoCamera.setOnClickListener(view -> {
            mGetPhotoImage.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            imageDialog.hide();
        });
        imageDialog.show();
    }

    private void getPermissions() {
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE) != null) {
                isReadPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE));
            }
            if (result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != null) {
                isWritePermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            }
        });
    }

    private void getCoffeeGalleryImage() {
        mGetGalleryImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData();

                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                EditCoffee.this.getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                Picasso.with(EditCoffee.this)
                        .load(imageUri.toString())
                        .into(imgCoffee);
                imageDialog.hide();

            } else {
                showToast(EditCoffee.this, "Permission not granted!");
            }
        });
    }

    private void getCoffeePhotoImage() {
        mGetPhotoImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");

                if (isWritePermissionGranted) {
                    if (saveImageToExternalStorage(UUID.randomUUID().toString(), bitmap)) {
                        Picasso.with(EditCoffee.this)
                                .load(imageUri.toString())
                                .into(imgCoffee);
                        imageDialog.hide();
                    }
                } else {
                    showToast(EditCoffee.this, "Permission not granted!");
                }
            }
        });

        requestPermission();
    }

    private void requestPermission() {
        boolean minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        isReadPermissionGranted = checkReadStoragePermission(this);
        isWritePermissionGranted = checkWriteStoragePermission(this);

        isWritePermissionGranted = isWritePermissionGranted || minSDK;

        List<String> permissionRequest = new ArrayList<>();
        if (!isReadPermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!isWritePermissionGranted) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    private boolean saveImageToExternalStorage(String imgName, Bitmap bmp) {
        Uri imageCollection;
        ContentResolver resolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = resolver.insert(imageCollection, contentValues);

        try {
            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);
            return true;
        } catch (Exception e) {
            showToast(this, "Image not saved!");
        }
        return false;
    }

    private void determineContext() {
        if (isCoffeeEdited()) {
            getSingleCoffee(coffeeId);
            saveBtn.setOnClickListener(view -> {
                if (validate()) {
                    editCoffee(coffeeId);
                }
            });
        } else {
            saveBtn.setOnClickListener(view -> {
                if (validate()) {
                    addCoffee();
                };
            });
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
        logCall(TAG, call);
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
                logCallFail(TAG, call);
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(inputCoffeeName.getText().toString().trim())) {
            showToast(EditCoffee.this, "Name cannot be empty!");
            return false;
        }
        return true;
    }

    private void editCoffee(int id) {
        Coffee coffee = createCoffee();
        Call<Void> call = createCoffeeApi().updateCoffee(id, coffee);
        logCall(TAG, call);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                showToast(EditCoffee.this, "Changes saved");
                Intent intent = new Intent(EditCoffee.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                if (inputCoffeeName.toString().trim().isEmpty()) {
                    showToast(EditCoffee.this, "Name cannot be empty!");
                } else {
                    showToast(EditCoffee.this, "Something went wrong");
                    logCallFail(TAG, call);
                }
            }
        });
    }

    private void addCoffee() {
        Coffee coffee = createCoffee();
        Call<Coffee> call = createCoffeeApi().createCoffee(coffee);
        logCall(TAG, call);
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
                logCallFail(TAG, call);
            }
        });
    }

    private Coffee createCoffee() {
        String name = Objects.requireNonNull(inputCoffeeName.getText()).toString();
        String origin = Objects.requireNonNull(inputOrigin.getText()).toString();
        String roaster = Objects.requireNonNull(inputRoaster.getText()).toString();
        BigDecimal rating = BigDecimal.valueOf(coffeeRatingBar.getRating());
        if (imageUri == null) {
            Log.d(TAG, " Object: Coffee[" + name + ", " + origin + ", " + roaster + ", " + rating + ", " + imageUri + ", " + USER_ID + "] created!");
            return new Coffee(name, origin, roaster, rating, null, USER_ID);
        }

        Log.d(TAG, " Object: Coffee[" + name + ", " + origin + ", " + roaster + ", " + rating + ", " + imageUri + ", " + USER_ID + "] created!");
        return new Coffee(name, origin, roaster, rating, imageUri.toString(), USER_ID);
    }
}