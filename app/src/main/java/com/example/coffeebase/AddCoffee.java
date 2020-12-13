package com.example.coffeebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

public class AddCoffee extends AppCompatActivity {

    private int id;
    private String name, origin;
    CoffeeBaseApi coffeeBaseApi;
    private Button pickImageBtn, addToCoffeeBaseBtn;
    private ImageView imgAddCoffee;
    private TextView coffeeAddNameTxt, originAddTxt;
    private EditText txtAddCoffeeName, txtAddOrigin, idTxt;

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
        txtAddCoffeeName = findViewById(R.id.txtAddCoffeeName);
        txtAddOrigin = findViewById(R.id.txtAddOrigin);
        idTxt = findViewById(R.id.idTxt);
    }

    public void addCoffee() {
        id = Integer.parseInt(idTxt.getText().toString());
        name = txtAddCoffeeName.getText().toString();
        origin = txtAddOrigin.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.67:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        Coffee coffee = new Coffee(id, name, origin);
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
        }
    }
}