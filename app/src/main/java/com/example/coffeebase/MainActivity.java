package com.example.coffeebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button addNewCoffeeBtn, myCoffeeBaseBtn, favouriteCoffeesBtn, aboutBtn, groupsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        addNewCoffeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCoffee.class);
                startActivity(intent);
            }
        });

        myCoffeeBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCoffeeBase.class);
                startActivity(intent);
            }
        });

        favouriteCoffeesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Favourites.class);
                startActivity(intent);
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Developed by mNitek");
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.create().show();
            }
        });

        groupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        addNewCoffeeBtn = findViewById(R.id.btnAddCoffee);
        myCoffeeBaseBtn = findViewById(R.id.btnMyCoffeeBase);
        favouriteCoffeesBtn = findViewById(R.id.btnFavCoffee);
        aboutBtn = findViewById(R.id.btnAbout);
        groupsBtn = findViewById(R.id.btnMyGroups);
    }
}