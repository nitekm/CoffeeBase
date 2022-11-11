package com.ncodedev.coffeebase.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.domain.Coffee;
import com.ncodedev.coffeebase.model.domain.Tag;
import com.squareup.picasso.Picasso;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.ncodedev.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncodedev.coffeebase.client.provider.TagApiProvider.createTagApi;
import static com.ncodedev.coffeebase.utils.Global.USER_ID;
import static com.ncodedev.coffeebase.utils.Logger.logCall;
import static com.ncodedev.coffeebase.utils.Logger.logCallFail;
import static com.ncodedev.coffeebase.utils.PermissionsUtils.checkReadStoragePermission;
import static com.ncodedev.coffeebase.utils.PermissionsUtils.checkWriteStoragePermission;
import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;

public class EditCoffee extends AppCompatActivity {

    private int tagColor = Color.parseColor("#f84c44");
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
    private FloatingActionButton colorPickerBtn;
    private RatingBar coffeeRatingBar;
    private TextInputEditText inputCoffeeName, inputRoaster, inputOrigin, inputRegion, inputFarm, inputCropHeight, inputProcessing, inputScaRating;
    private Spinner roastProfileSpinner, continentSpinner;
    private ChipGroup tagsChipGroup;
    private AutoCompleteTextView tagsTextView;
    ArrayAdapter<CharSequence> roastProfileAdapter, continentAdapter;
    private Dialog imageDialog;
    List<Tag> searchTags = new ArrayList<>();
    List<Tag> allTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coffee);

        initViews();
        determineContext();
        getTags();
        getPermissions();
        getCoffeePhotoImage();
        getCoffeeGalleryImage();
        launchColorPicker();
        handleAddTag();
    }

    private void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        if (imageUri == null || imageUri.toString().isEmpty()) {
            imgCoffee.setImageResource(R.mipmap.coffeebean);
        }
        addImageBtn = findViewById(R.id.addImageBtn);
        addImageBtn.setOnClickListener(view -> showAddImageDialog());
        saveBtn = findViewById(R.id.saveBtn);
        colorPickerBtn = findViewById(R.id.colorPickerButton);
        colorPickerBtn.setBackgroundTintList(ColorStateList.valueOf(tagColor));
        colorPickerBtn.setSupportBackgroundTintList(ColorStateList.valueOf(tagColor));

        coffeeRatingBar = findViewById(R.id.coffeeRatingBar);
        inputCoffeeName = findViewById(R.id.inputCoffeeName);
        inputRoaster = findViewById(R.id.inputRoaster);
        inputOrigin = findViewById(R.id.inputOrigin);
        inputRegion = findViewById(R.id.inputRegion);
        inputFarm = findViewById(R.id.inputFarm);
        inputCropHeight = findViewById(R.id.inputCropHeight);
        inputProcessing = findViewById(R.id.inputProcessing);
        inputScaRating = findViewById(R.id.inputScaRating);
        tagsChipGroup = findViewById(R.id.tagsChipGroup);
        tagsTextView = findViewById(R.id.tagsTextView);
        tagsTextView.setThreshold(1);
        tagsTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> searchTags(charSequence.toString()), 1000);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
            }
        });

        tagsTextView.setOnItemClickListener((adapterView, view, i, l) -> searchTags.stream()
                .filter(tag -> tag.getName().equalsIgnoreCase(adapterView.getItemAtPosition(i).toString()))
                .findAny()
                .ifPresent(tag -> {
                    addTagChip(tag.getName(), Integer.parseInt(tag.getColor()));
                    tagsTextView.getText().clear();
                }));

        roastProfileSpinner = findViewById(R.id.roastProfileSpinner);
        roastProfileAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.roastProfiles,
                android.R.layout.simple_spinner_dropdown_item);
        roastProfileSpinner.setAdapter(roastProfileAdapter);

        continentSpinner = findViewById(R.id.continentSpinner);
        continentAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.continents,
                android.R.layout.simple_spinner_dropdown_item);
        continentSpinner.setAdapter(continentAdapter);

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
                }
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

    private void handleAddTag() {
        tagsTextView.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTagChip(tagsTextView.getText().toString(), tagColor);
                tagsTextView.getText().clear();
                return true;
            }
            return false;
        });
    }

    private void launchColorPicker() {
        colorPickerBtn.setOnClickListener(view -> {
            ColorPicker colorPicker = new ColorPicker(this);
            colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                        @Override
                        public void setOnFastChooseColorListener(final int position, final int color) {
                            tagColor = color;
                            colorPickerBtn.setBackgroundTintList(ColorStateList.valueOf(tagColor));
                            colorPickerBtn.setSupportBackgroundTintList(ColorStateList.valueOf(tagColor));
                        }

                        @Override
                        public void onCancel() {
                        }
                    })
                    .setRoundColorButton(true)
                    .setColumns(5)
                    .show();
        });
    }

    private void addTagChip(String tag, int color) {
        Chip chip = new Chip(this);

        if (tag.length() > 0 && tag.charAt(0) == '#') {
            chip.setText(tag);
        } else {
            chip.setText("#" + tag);
        }
        chip.setChipBackgroundColor(ColorStateList.valueOf(color));
        chip.setOnCloseIconClickListener(view -> tagsChipGroup.removeView(chip));

        chip.setCloseIconVisible(true);
        chip.setClickable(true);

        chip.setChipIconVisible(false);
        chip.setCheckable(false);

        tagsChipGroup.addView(chip);
    }

    private void getSingleCoffee(int coffeeId) {
        Call<Coffee> call = createCoffeeApi().getSingleCoffee(coffeeId);
        logCall(TAG, call);
        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                coffee = response.body();
                coffeeRatingBar.setRating(coffee.getRating().floatValue());
                inputCoffeeName.setText(coffee.getName());
                inputRoaster.setText(coffee.getRoaster());
                inputOrigin.setText(coffee.getOrigin());
                inputRegion.setText(coffee.getRegion());
                inputFarm.setText(coffee.getFarm());
                inputProcessing.setText(coffee.getProcessing());

                if (coffee.getCropHeight() != null) {
                    inputCropHeight.setText(String.valueOf(coffee.getCropHeight()));
                }
                if (coffee.getScaRating() != null) {
                    inputScaRating.setText(String.valueOf(coffee.getScaRating()));
                }

                roastProfileSpinner.post(() -> roastProfileSpinner.setSelection(roastProfileAdapter.getPosition(coffee.getRoastProfile())));
                continentSpinner.post(() -> continentSpinner.setSelection(continentAdapter.getPosition(coffee.getContinent())));

                List<Tag> tags = coffee.getTags();
                tags.forEach(tag -> {
                    Chip chip = new Chip(EditCoffee.this);
                    chip.setText(tag.getName());
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Integer.parseInt(tag.getColor())));
                    chip.setOnCloseIconClickListener(view -> tagsChipGroup.removeView(chip));

                    chip.setCloseIconVisible(true);
                    chip.setClickable(true);

                    chip.setChipIconVisible(false);
                    chip.setCheckable(false);

                    tagsChipGroup.addView(chip);
                });


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

    private void searchTags(String name) {
        Call<List<Tag>> call = createTagApi().searchTags(name);
        logCall(TAG, call);

        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(final Call<List<Tag>> call, final Response<List<Tag>> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    searchTags = response.body();
                    List<String> tagNames = searchTags.stream()
                            .map(Tag::getName)
                            .collect(Collectors.toList());
                    ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(EditCoffee.this, android.R.layout.simple_list_item_1, tagNames);
                    tagsTextView.setAdapter(tagAdapter);
                    tagAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(final Call<List<Tag>> call, final Throwable t) {
                showToast(EditCoffee.this, "Cannot search tags");
                logCallFail(TAG, call);
            }
        });
    }

    private void getTags() {
        Call<List<Tag>> call = createTagApi().getTags();
        logCall(TAG, call);

        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(final Call<List<Tag>> call, final Response<List<Tag>> response) {
                allTags = response.body();
            }

            @Override
            public void onFailure(final Call<List<Tag>> call, final Throwable t) {
                showToast(EditCoffee.this, "Cannot get tags");
                logCallFail(TAG, call);
            }
        });
    }

    private Coffee createCoffee() {
        String name = Objects.requireNonNull(inputCoffeeName.getText()).toString();
        String roaster = Objects.requireNonNull(inputRoaster.getText()).toString();
        String origin = Objects.requireNonNull(inputOrigin.getText()).toString();
        String region = Objects.requireNonNull(inputRegion.getText()).toString();
        String farm = Objects.requireNonNull(inputFarm.getText()).toString();
        String processing = Objects.requireNonNull(inputProcessing.getText()).toString();
        Double rating = (double) coffeeRatingBar.getRating();
        String roastProfile = roastProfileSpinner.getSelectedItem().toString();
        String continent = continentSpinner.getSelectedItem().toString();

        Integer cropHeight = null, scaRating = null;
        if (TextUtils.getTrimmedLength(inputCropHeight.getText()) > 0) {
            cropHeight = Integer.parseInt(inputCropHeight.getText().toString());
        }
        if (TextUtils.getTrimmedLength(inputScaRating.getText()) > 0) {
            scaRating = Integer.parseInt(inputScaRating.getText().toString());
        }

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagsChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) tagsChipGroup.getChildAt(i);
            Tag newTag = new Tag(chip.getText().toString(), String.valueOf(chip.getChipBackgroundColor().getDefaultColor()), USER_ID);
            Optional<Tag> existingTag = allTags.stream()
                    .filter(tag -> tag.getName().equalsIgnoreCase(newTag.getName())
                            && tag.getColor().equalsIgnoreCase(newTag.getColor()))
                    .findAny();
            if (existingTag.isPresent()) {
                tags.add(existingTag.get());
            } else {
                tags.add(newTag);
            }
        }


        //TODO: chane this v
        if (imageUri == null) {
            Coffee createdCoffee = new Coffee(name, origin, roaster, processing, roastProfile, region, continent, farm, cropHeight, scaRating, rating, null, USER_ID, tags);
            Log.d(TAG, " Object: " + createdCoffee + "created!");
            return createdCoffee;
        }

        Coffee createdCoffee = new Coffee(name, origin, roaster, processing, roastProfile, region, continent, farm, cropHeight, scaRating, rating, imageUri.toString(), USER_ID, tags);
        Log.d(TAG, " Object: " + createdCoffee + " created!");
        return createdCoffee;
    }

}