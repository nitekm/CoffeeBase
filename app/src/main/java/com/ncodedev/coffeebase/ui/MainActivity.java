package com.ncodedev.coffeebase.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.domain.Coffee;
import com.ncodedev.coffeebase.model.security.User;
import com.ncodedev.coffeebase.ui.utility.CoffeeRecyclerViewAdapter;
import com.ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import com.ncodedev.coffeebase.web.provider.CoffeeApiProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;
import static com.ncodedev.coffeebase.utils.Utils.hideProgressBar;
import static com.ncodedev.coffeebase.utils.Utils.showProgressBar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CoffeeListResponseListener {

    public static final String TAG = "MainActivity";
    private CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView userNameTxt;
    private ImageView userPictureImage;
    private ProgressBar progressBar;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getAllCoffees();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "No user instance found, initializing LoginActivity");
        if (User.getInstance() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.coffeeRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.getHeaderView(0);
        userNameTxt = headerView.findViewById(R.id.userNameTxt);
        userPictureImage = headerView.findViewById(R.id.userPictureImage);
        setUpNavigationDrawer();
        setUpNavigationDrawerContent(navigationView);
        updateUI();
    }

    private void updateUI() {
        User user = User.getInstance();
        userNameTxt.setText(user.getUsername());
        if (user.getPictureUri() != null) {
            Picasso.with(this)
                    .load(user.getPictureUri())
                    .placeholder(R.drawable.ic_account)
                    .into(userPictureImage);
        }
        hideProgressBar(progressBar, MainActivity.this);
    }

    private void getAllCoffees() {
        showProgressBar(progressBar, this);
        coffeeApiProvider.getAll(this, this);
    }

    @Override
    public void handleGetList(List<Coffee> coffees) {
        Log.d(TAG, "Callback from " + coffeeApiProvider.getClass().getSimpleName() + " received");
        coffeeRecyclerViewAdapter = new CoffeeRecyclerViewAdapter(MainActivity.this, coffees);
        recyclerView.setAdapter(coffeeRecyclerViewAdapter);
        hideProgressBar(progressBar, this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit Application");
        alertDialogBuilder.setNegativeButton("No", ((dialogInterface, i) -> {
        }));
        alertDialogBuilder.setPositiveButton("Yes", ((dialogInterface, i) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }));
        alertDialogBuilder.create().show();
    }


    //TOP BAR - START ------------------------------------------------------------------------------------------------\\
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_my_coffeebase, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem menuItem) {
                Log.d(TAG, "onMenuItemActionExpand: Search mode activated");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem menuItem) {
                Log.d(TAG, "onMenuItemActionCollapse: Search mode deactivated");
                return true;
            }
        };
        menu.findItem(R.id.searchMenuItem).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchMenuItem).getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String searchBy) {
                coffeeApiProvider.search(searchBy, MainActivity.this, MainActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String searchBy) {
                new Handler(Looper.getMainLooper())
                        .postDelayed(() -> coffeeApiProvider.search(
                                searchBy,
                                MainActivity.this,
                                MainActivity.this), 2000);
                return true;
            }
        });
        return true;
    }
    //TOP BAR - END --------------------------------------------------------------------------------------------------\\


    //NAVIGATION DRAWER - START --------------------------------------------------------------------------------------\\
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

    @SuppressLint("NonConstantResourceId")
    private void selectDrawerItem(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCoffee:
                launchEditCoffee();
                break;
            case R.id.about:
                showAbout();
                break;
            case R.id.account:
                showAccountInfo();
                break;
            case R.id.signout:
                signOut();
                break;
            default:
                break;
        }
    }

    private void launchEditCoffee() {
        Intent intent = new Intent(MainActivity.this, EditCoffee.class);
        startActivity(intent);
    }

    private void showAbout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Developed by NCode");
        alertDialogBuilder.setNegativeButton("Dismiss", (dialogInterface, i) -> {
        });
        alertDialogBuilder.create().show();
    }

    private void showAccountInfo() {
        showToast(this, "Signed in with Google");
    }

    private void signOut() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //Code 1 to pass logout request to login activity
        intent.putExtra("CODE", 1);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
    //NAVIGATION DRAWER - END ---------------------------------------------------------------------------------------\\
}
