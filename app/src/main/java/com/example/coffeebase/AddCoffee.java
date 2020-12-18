package com.example.coffeebase;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;

public class AddCoffee extends AppCompatActivity {

    private String name, origin, roaster, imageUri;
    private int rating;
    CoffeeBaseApi coffeeBaseApi;
    private Button pickImageBtn, addToCoffeeBaseBtn;
    private ImageView imgAddCoffee;
    private TextView coffeeAddNameTxt, originAddTxt, roasterAddTxt;
    private EditText txtAddCoffeeName, txtAddOrigin, txtAddRoaster;
    private RadioGroup ratingRadioGroup;
    private RadioButton radio1, radio2, radio3, radio4, radio5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coffee);

        initViews();

        addToCoffeeBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addCoffee();
            }
        });

       pickImageBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                           == PackageManager.PERMISSION_DENIED) {
                       String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                       requestPermissions(permissions, 1001);
                   }
                   else {
                       pickImageFromGallery();
                   }
               }
               else {
                   pickImageFromGallery();
               }
           }
       });
    }

    public void initViews() {
        pickImageBtn = findViewById(R.id.pickImageBtn);
        addToCoffeeBaseBtn = findViewById(R.id.addToCoffeeBaseBtn);
        imgAddCoffee = findViewById(R.id.imgAddCoffee);
        coffeeAddNameTxt = findViewById(R.id.coffeeAddNameTxt);
        originAddTxt = findViewById(R.id.originAddTxt);
        roasterAddTxt = findViewById(R.id.roasterAddTxt);
        ratingRadioGroup = findViewById(R.id.ratingRadio);
        txtAddCoffeeName = findViewById(R.id.txtAddCoffeeName);
        txtAddOrigin = findViewById(R.id.txtAddOrigin);
        txtAddRoaster = findViewById(R.id.txtAddRoaster);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);
        radio5 = findViewById(R.id.radio5);

    }

    public void addCoffee() {
        name = txtAddCoffeeName.getText().toString();
        origin = txtAddOrigin.getText().toString();
        roaster = txtAddRoaster.getText().toString();
        checkRating();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.67:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        Coffee coffee = new Coffee(name, origin, roaster, rating, imageUri);
        Call<Void> call = coffeeBaseApi.addToCoffeeBase(coffee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(AddCoffee.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddCoffee.this, "Coffee added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCoffee.this, MyCoffeeBase.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddCoffee.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1000) {
            imgAddCoffee.setImageURI(data.getData());
            Uri uri = data.getData();
            File imgFile = new File(String.valueOf(uri));
            imageUri = imgFile.toString();
        }
    }

    public void checkRating() {
        if (radio1.isChecked()) rating = 1;
        if (radio2.isChecked()) rating = 2;
        if (radio3.isChecked()) rating = 3;
        if (radio4.isChecked()) rating = 4;
        if (radio5.isChecked()) rating = 5;
    }
}