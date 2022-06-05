package com.ncode.coffeebase.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.ncode.coffeebase.R;
import com.ncode.coffeebase.model.Coffee;
import com.ncode.coffeebase.model.security.Token;
import com.ncode.coffeebase.model.security.User;
import com.ncode.coffeebase.ui.utility.CoffeeRecyclerViewAdapter;
import com.ncode.coffeebase.utils.Global;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.ncode.coffeebase.client.provider.CoffeeApiProvider.createCoffeeApi;
import static com.ncode.coffeebase.client.provider.SecurityApiProvider.createSecurityApi;
import static com.ncode.coffeebase.utils.Logger.logCall;
import static com.ncode.coffeebase.utils.Logger.logCallFail;
import static com.ncode.coffeebase.utils.ToastUtils.showToast;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;

    private GoogleSignInClient googleSignInClient;
    private CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView userNameTxt;
    private ImageView userPictureImage;
    private ProgressBar progressBar;
    private List<Coffee> coffees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setUpNavigationDrawer();
        setUpNavigationDrawerContent(navigationView);

        //TODO: to method
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            signIn();
        } else {
            updateUI(account);
            authenticateWithBackend(account);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable @org.jetbrains.annotations.Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "OAuth2.0 token acquired: " + account.getIdToken());
            updateUI(account);
            authenticateWithBackend(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.coffeeRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.getHeaderView(0);
        userNameTxt = headerView.findViewById(R.id.userNameTxt);
        userPictureImage = headerView.findViewById(R.id.userPictureImage);
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

    @SuppressLint("NonConstantResourceId")
    private void selectDrawerItem(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCoffee: launchEditCoffee();
                break;
            case R.id.about: showAbout();
                break;
            case R.id.account: showAccountInfo();
                break;
            case R.id.signout: signOut();
                break;
            default:
                break;

        }
    }

    private void signIn() {
        Log.d(TAG, "No logged user found! Initializing signIn");
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    showToast(this, "Successfully logged out!");
                    finish();
                    startActivity(getIntent());
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.d(TAG, "User logged [id: " + account.getId() +
                " name: " + account.getDisplayName() +
                " email: " + account.getEmail() +
                " photoUrl: " + account.getPhotoUrl() + "]");

        User user = createUserFromAccount(account);
        Log.d(TAG, "Set up Global.USER_ID " +  user.getUserId());
        Global.USER_ID = user.getUserId();
        userNameTxt.setText(user.getUsername());
        if (user.getPicture() != null) {
            Picasso.with(this)
                    .load(user.getPicture())
                    .placeholder(R.drawable.ic_account)
                    .into(userPictureImage);
        }
    }

    private void
    authenticateWithBackend(GoogleSignInAccount account) {
        Log.d(TAG, "Authenticating with backend server...");
        Call<Token> call = createSecurityApi().authenticate(new Token(account.getIdToken()));
        logCall(TAG, call);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(final Call<Token> call, final Response<Token> response) {
                if (response.body() == null) {
                    signOut();
                } else {
                    Global.TOKEN = response.body().getToken();
                    Log.d(TAG, "Authentication success! JWT: " + Global.TOKEN);
                    getAllCoffees();
                }
            }

            @Override
            public void onFailure(final Call<Token> call, final Throwable t) {
                showToast(MainActivity.this, "Something went wrong!");
                logCallFail(TAG, call);
            }
        });
    }

    private User createUserFromAccount(GoogleSignInAccount account) {
        User user = new User();
        user.setUserId(account.getId());
        user.setUsername(account.getDisplayName());
        user.setEmail(account.getEmail());
        if (account.getPhotoUrl() != null) {
            user.setPicture(account.getPhotoUrl().toString());
        }
        Log.d(TAG, "User created!");
        return user;
    }

    private void getAllCoffees() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Coffee>> call = createCoffeeApi().getCoffees();
        logCall(TAG, call);
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
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(final Call<List<Coffee>> call, final Throwable t) {
                logCallFail(TAG, call);
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d(TAG, "Retrying...");
                call.clone().enqueue(this);
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
        alertDialogBuilder.setNegativeButton("Dismiss", (dialogInterface, i) -> {
        });
        alertDialogBuilder.create().show();
    }

    private void showAccountInfo() {
        showToast(this, "Signed in with Google");
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
}