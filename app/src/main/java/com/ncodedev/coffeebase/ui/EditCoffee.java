package com.ncodedev.coffeebase.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.domain.Coffee;
import com.ncodedev.coffeebase.model.domain.Tag;
import com.ncodedev.coffeebase.model.security.User;
import com.ncodedev.coffeebase.ui.utility.ImageHelper;
import com.ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import com.ncodedev.coffeebase.web.listener.TagListResponseListener;
import com.ncodedev.coffeebase.web.provider.CoffeeApiProvider;
import com.ncodedev.coffeebase.web.provider.TagApiProvider;
import com.squareup.picasso.Picasso;
import petrov.kristiyan.colorpicker.ColorPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;

public class EditCoffee extends AppCompatActivity implements CoffeeResponseListener, TagListResponseListener {
    private int tagColor = Color.parseColor("#f84c44");
    public static final String COFFEE_ID_KEY = "coffeeId";
    private static final String TAG = "EditCoffee";
    private int coffeeId;
    private ImageView imgCoffee;
    private Button saveBtn;
    private FloatingActionButton colorPickerBtn;
    private RatingBar coffeeRatingBar;
    private TextInputEditText inputCoffeeName, inputRoaster, inputOrigin, inputRegion, inputFarm, inputCropHeight, inputProcessing, inputScaRating;
    private Spinner roastProfileSpinner, continentSpinner;
    private ChipGroup tagsChipGroup;
    private AutoCompleteTextView tagsTextView;
    private ArrayAdapter<CharSequence> roastProfileAdapter, continentAdapter;
    private List<Tag> searchTags = new ArrayList<>();
    private List<Tag> allTags = new ArrayList<>();
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();
    private final TagApiProvider tagApiProvider = TagApiProvider.getInstance();
    private final ImageHelper imageHelper = ImageHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coffee);

        initViews();
        determineContext();
        handleImage();
        tagApiProvider.getAll(this, this);
        launchColorPicker();
        handleAddTag();
    }

    private void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        if (imgCoffee.getTag() == null || imgCoffee.getTag().toString().isEmpty()) {
            imgCoffee.setImageResource(R.mipmap.coffeebean);
        }

        Button addImageBtn = findViewById(R.id.addImageBtn);
        addImageBtn.setOnClickListener(view -> imageHelper.showAddImageDialog(this));
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
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {}

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                new Handler(Looper.getMainLooper())
                        .postDelayed(() -> tagApiProvider.search(charSequence.toString(),
                                        EditCoffee.this,
                                        EditCoffee. this), 1000);
            }

            @Override
            public void afterTextChanged(final Editable editable) {}
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

        MaterialToolbar toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(EditCoffee.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void determineContext() {
        if (isCoffeeEdited()) {
            coffeeApiProvider.getOne(coffeeId, this, this);
            saveBtn.setOnClickListener(view -> {
                if (validate()) {
                    Coffee coffee = createCoffee();
                    coffeeApiProvider.update(coffeeId, coffee, this, this);
                    Intent intent = new Intent(EditCoffee.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            saveBtn.setOnClickListener(view -> {
                if (validate()) {
                    Coffee coffee = createCoffee();
                    coffeeApiProvider.save(coffee, this, this);
                    Intent intent = new Intent(EditCoffee.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void handleImage() {
        imageHelper.getPermissions(this);
        imageHelper.getCoffeeGalleryImage(this, imgCoffee);
        imageHelper.getCoffeePhotoImage(this, imgCoffee);
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

    @Override
    public void handleCoffeeResponse(final Coffee editedCoffee) {
        coffeeRatingBar.setRating(editedCoffee.getRating().floatValue());
        inputCoffeeName.setText(editedCoffee.getName());
        inputRoaster.setText(editedCoffee.getRoaster());
        inputOrigin.setText(editedCoffee.getOrigin());
        inputRegion.setText(editedCoffee.getRegion());
        inputFarm.setText(editedCoffee.getFarm());
        inputProcessing.setText(editedCoffee.getProcessing());

        if (editedCoffee.getCropHeight() != null) {
            inputCropHeight.setText(String.valueOf(editedCoffee.getCropHeight()));
        }
        if (editedCoffee.getScaRating() != null) {
            inputScaRating.setText(String.valueOf(editedCoffee.getScaRating()));
        }

        roastProfileSpinner.post(() -> roastProfileSpinner.setSelection(roastProfileAdapter.getPosition(editedCoffee.getRoastProfile())));
        continentSpinner.post(() -> continentSpinner.setSelection(continentAdapter.getPosition(editedCoffee.getContinent())));

        List<Tag> tags = editedCoffee.getTags();
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
                .load(editedCoffee.getImageUrl())
                .placeholder(R.mipmap.coffeebean)
                .into(imgCoffee);

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
            Tag newTag = new Tag(chip.getText().toString(), String.valueOf(chip.getChipBackgroundColor().getDefaultColor()), User.getInstance().getUserId());
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

        Coffee createdCoffee = new Coffee(name, origin, roaster, processing, roastProfile, region, continent, farm, cropHeight, scaRating, rating, User.getInstance().getUserId(), tags);
        if (imgCoffee.getTag() != null) {
            createdCoffee.setImageUrl(imgCoffee.getTag().toString());
        }
        Log.d(TAG, " Object: " + createdCoffee + "created!");
        return createdCoffee;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(inputCoffeeName.getText().toString().trim())) {
            showToast(EditCoffee.this, "Name cannot be empty!");
            return false;
        }
        return true;
    }


//TAGS - START ------------------------------------------------------------------------------------------------------\\
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

    @Override
    public void handleGetList(final List<Tag> tags) {
        allTags = tags;
    }

    @Override
    public void handleSearchResult(final List<Tag> tags) {
        searchTags = tags;
        List<String> tagNames = searchTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(EditCoffee.this, android.R.layout.simple_list_item_1, tagNames);
        tagsTextView.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
    }

    //TAGS - END ----------------------------------------------------------------------------------------------------\\
}
