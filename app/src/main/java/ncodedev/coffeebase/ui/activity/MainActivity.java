package ncodedev.coffeebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.model.utils.Page;
import ncodedev.coffeebase.model.utils.PageCoffeeRequest;
import ncodedev.coffeebase.service.GoogleSignInClientService;
import ncodedev.coffeebase.ui.utility.ImageHelper;
import ncodedev.coffeebase.ui.utility.NavigationDrawerHandler;
import ncodedev.coffeebase.ui.view.adapter.CoffeeRecyclerViewAdapter;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ncodedev.coffeebase.service.SharedPreferencesNames.MY_COFFEEBASE_COFFEES_IN_ROW;

public class MainActivity extends AppCompatActivity implements CoffeeListResponseListener {
    public static final String TAG = "MainActivity";

    private CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private TextView userNameTxt;
    private ImageView userPictureImage;
    private ProgressBar progressBar;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();
    private final ImageHelper imageHelper = ImageHelper.getInstance();
    private GoogleSignInClientService googleSignInClientService;
    private Integer currentPage = 0;
    private boolean lastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageHelper.setPicassoInstance(this);
        googleSignInClientService = new GoogleSignInClientService(this);

        initViews();
        getAllCoffees();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (User.getInstance() == null) {
            googleSignInClientService.silentSignIn();
            Log.d(TAG, "No logged user found, initializing silent SignIn");
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.coffeeRecView);
        setUpCoffeesView();
        MaterialToolbar toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.getHeaderView(0);
        userNameTxt = headerView.findViewById(R.id.userNameTxt);
        userPictureImage = headerView.findViewById(R.id.userPictureImage);
        NavigationDrawerHandler navigationDrawerHandler = new NavigationDrawerHandler(this);
        navigationDrawerHandler.setUpNavigationDrawer(toolbar, drawerLayout, navigationView);
        progressBar = findViewById(R.id.progressBar);
        updateUI();
    }

    private void updateUI() {
        User user = User.getInstance();
        userNameTxt.setText(user.getUsername());
        if (user.getPictureUri() != null) {
            imageHelper.picassoSetImage(user.getPictureUri(), userPictureImage, R.drawable.ic_account);
        }
    }

    private void setUpCoffeesView() {
        int coffeesInRowPreference = PreferenceManager.getDefaultSharedPreferences(this).getInt(MY_COFFEEBASE_COFFEES_IN_ROW, 2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, coffeesInRowPreference);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !lastPage) {
                    coffeeApiProvider.getAllPaged(MainActivity.this, new PageCoffeeRequest(currentPage + 1));
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    private void getAllCoffees() {
        progressBar.setVisibility(View.VISIBLE);
        coffeeApiProvider.getAllPaged(this, new PageCoffeeRequest(currentPage));
    }

    @Override
    public void handleGetList(Page<Coffee> coffeesPage) {
        Log.d(TAG, "Callback from " + coffeeApiProvider.getClass().getSimpleName() + " received");
        if (coffeeRecyclerViewAdapter == null) {
            coffeeRecyclerViewAdapter = new CoffeeRecyclerViewAdapter(MainActivity.this, coffeesPage.getContent());
            recyclerView.setAdapter(coffeeRecyclerViewAdapter);
        } else {
            coffeeRecyclerViewAdapter.addItems(coffeesPage.getContent());
            coffeeRecyclerViewAdapter.notifyDataSetChanged();
        }
        currentPage = coffeesPage.getNumber();
        lastPage = coffeesPage.isLast();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void handleGetList(List<Coffee> coffees) {
        progressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "Callback from " + coffeeApiProvider.getClass().getSimpleName() + " received");
        CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter = new CoffeeRecyclerViewAdapter(MainActivity.this, coffees);
        recyclerView.setAdapter(coffeeRecyclerViewAdapter);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(R.string.exit_app);
        alertDialogBuilder.setNegativeButton(R.string.no, ((dialogInterface, i) -> {
        }));
        alertDialogBuilder.setPositiveButton(R.string.yes, ((dialogInterface, i) -> {
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
        searchView.setQueryHint(getString(R.string.search_by_anything));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String searchBy) {
                coffeeApiProvider.search(searchBy, MainActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String searchBy) {
                new Handler(Looper.getMainLooper())
                        .postDelayed(() -> coffeeApiProvider.search(searchBy, MainActivity.this), 2000);
                return true;
            }
        });
        return true;
    }
    //TOP BAR - END --------------------------------------------------------------------------------------------------\\

    @Override
    public void handleError() { }
}
