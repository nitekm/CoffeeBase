package com.example.coffeebase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyCoffeeBase extends AppCompatActivity {

    private CoffeeRecViewAdapter adapter;
    private RecyclerView coffeeRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coffee_base);

        adapter = new CoffeeRecViewAdapter(this);
        coffeeRecView = findViewById(R.id.coffeeRecView);
        //coffeeRecView.setLayoutManager(new LinearLayoutManager(this));
        coffeeRecView.setAdapter(adapter);
    }
}