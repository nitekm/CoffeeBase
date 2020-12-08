package com.example.coffeebase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Favourites extends AppCompatActivity {

    private CoffeeRecViewAdapter adapter;
    private RecyclerView favRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        adapter = new CoffeeRecViewAdapter(this);
        favRecView = findViewById(R.id.favRecView);
        //favRecView.setLayoutManager(new LinearLayoutManager(this));
        favRecView.setAdapter(adapter);
    }
}