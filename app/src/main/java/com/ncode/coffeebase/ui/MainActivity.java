package com.ncode.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.ncode.coffeebase.R;
import com.ncode.coffeebase.model.Coffee;
import com.ncode.coffeebase.ui.utility.CoffeeRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.ncode.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncode.coffeebase.utils.ToastUtils.showToast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";

    private CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Coffee> coffees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setUpNavigationDrawer();
        setUpNavigationDrawerContent(navigationView);
        getAllCoffees();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.coffeeRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);
    }

    private void setUpNavigationDrawer() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpNavigationDrawerContent(NavigationView navigationView) {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return true;
                }
        );
    }

    private void selectDrawerItem(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCoffee: launchEditCoffee();
                break;
            case R.id.about: showAbout();
                break;
            case R.id.account:
                showToast(this, "Selected item: " + item.getTitle());
                break;
            case R.id.signout:
                showToast(this, "Selected item: " + item.getTitle());
                break;
            default:
                break;

        }
    }

    private void getAllCoffees() {

        Call<List<Coffee>> call = createCoffeeApi().getCoffees();

        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(final Call<List<Coffee>> call, final Response<List<Coffee>> response) {
                if (!response.isSuccessful()) {
                    showToast(MainActivity.this, "Code: " + response.code() + " " + response.message());
                    return;
                }
                coffees = new ArrayList<>(response.body());
                coffeeRecyclerViewAdapter = new CoffeeRecyclerViewAdapter(MainActivity.this, coffees);
                recyclerView.setAdapter(coffeeRecyclerViewAdapter);
            }

            @Override
            public void onFailure(final Call<List<Coffee>> call, final Throwable t) {
                showToast(MainActivity.this, "Something went wrong");
            }
        });
    }

    private void launchEditCoffee() {
        Intent intent = new Intent(MainActivity.this, EditCoffee.class);
        startActivity(intent);
    }

    private void showAbout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Developed by NCode");
        alertDialogBuilder.setNegativeButton("Dismiss", (dialogInterface, i) -> { });
        alertDialogBuilder.create().show();
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
}