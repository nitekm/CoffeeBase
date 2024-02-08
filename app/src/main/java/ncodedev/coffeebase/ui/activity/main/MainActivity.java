package ncodedev.coffeebase.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.model.utils.Page;
import ncodedev.coffeebase.model.utils.PageCoffeeRequest;
import ncodedev.coffeebase.service.GoogleSignInClientService;
import ncodedev.coffeebase.service.PageCoffeeRequestContextHolder;
import ncodedev.coffeebase.ui.utility.ImageHelper;
import ncodedev.coffeebase.ui.view.adapter.CoffeeRecyclerViewAdapter;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static ncodedev.coffeebase.service.PageCoffeeRequestContextHolder.getInstance;
import static ncodedev.coffeebase.service.PageCoffeeRequestContextHolder.updateInstance;
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
    private PageCoffeeRequestContextHolder requestContextHolder;

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
    }


    private void getAllCoffees() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            requestContextHolder = PageCoffeeRequestContextHolder.getInstance();
        } catch (IllegalStateException e) {
            requestContextHolder = updateInstance("id", "ASC", new HashMap<>());
        }
        coffeeApiProvider.getAllPaged(this,
                new PageCoffeeRequest.Builder()
                        .withSortDirection(requestContextHolder.getCurrentSortDirection())
                        .withSortProperty(requestContextHolder.getCurrentSortProperty())
                        .withPageNumber(0)
                        .withFilters(requestContextHolder.getFilters())
                        .build(),
                RequestContext.GET_ALL);
    }

    @Override
    public void handleGetAllPage(Page<Coffee> coffeesPage) {
        requestContextHolder = PageCoffeeRequestContextHolder.getInstance();
        handleResponseBasedOnContext(coffeesPage, RequestContext.GET_ALL);
    }

    @Override
    public void handleSortPage(Page<Coffee> coffeePage) {
        requestContextHolder = PageCoffeeRequestContextHolder.getInstance();
        handleResponseBasedOnContext(coffeePage, RequestContext.SORT);
    }

    @Override
    public void handleFilterPage(Page<Coffee> coffeePage) {
        requestContextHolder = getInstance();
        handleResponseBasedOnContext(coffeePage, RequestContext.FILTER);
    }

    private void handleResponseBasedOnContext(Page<Coffee> coffeesPage, RequestContext requestContext) {
        Log.d(TAG, "Callback from " + coffeeApiProvider.getClass().getSimpleName() + " received");
        if (coffeeRecyclerViewAdapter == null || requestContextHolder.getCurrentRequestContext() != requestContext) {
            coffeeRecyclerViewAdapter = new CoffeeRecyclerViewAdapter(MainActivity.this, coffeesPage.getContent());
            recyclerView.setAdapter(coffeeRecyclerViewAdapter);
        } else {
            coffeeRecyclerViewAdapter.addItems(coffeesPage.getContent());
            coffeeRecyclerViewAdapter.notifyDataSetChanged();
        }
        requestContextHolder.updateContextOnNewPage(coffeesPage, requestContext);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                if (shouldFetchMore(newState)) {
                    coffeeApiProvider.getAllPaged(
                            MainActivity.this,
                            createPageCoffeeRequestFromContextHolder(),
                            requestContext);
                    requestContextHolder.setCurrentlyLoadingData(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean shouldFetchMore(int newState) {
        return !recyclerView.canScrollVertically(1) &&
                newState == RecyclerView.SCROLL_STATE_IDLE &&
                requestContextHolder.canFetchMoreData();
    }

    private PageCoffeeRequest createPageCoffeeRequestFromContextHolder() {
        return new PageCoffeeRequest.Builder()
                .withPageNumber(requestContextHolder.getNextPageNumber())
                .withSortProperty(requestContextHolder.getCurrentSortProperty())
                .withSortDirection(requestContextHolder.getCurrentSortDirection())
                .withFilters(requestContextHolder.getFilters())
                .build();
    }

    @Override
    public void handleGetAll(List<Coffee> coffees) {
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
        var topBarHandler = new MainActivityTopBarHandler(this, menu, this);
        return topBarHandler.setUpTopAppBarSearch();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        var topBarHandler = new MainActivityTopBarHandler(this, item, this);
        topBarHandler.setUpTopAppBarSort();
        topBarHandler.setUpTopBarFilter();
        return true;
    }
    //TOP BAR - END --------------------------------------------------------------------------------------------------\\

    @Override
    public void handleError() {
    }
}
