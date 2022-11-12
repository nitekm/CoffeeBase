package com.ncodedev.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ncodedev.coffeebase.R;
import com.ncodedev.coffeebase.model.security.Token;
import com.ncodedev.coffeebase.model.security.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncodedev.coffeebase.client.provider.SecurityApiProvider.createSecurityApi;
import static com.ncodedev.coffeebase.utils.Logger.logCall;
import static com.ncodedev.coffeebase.utils.Logger.logCallFail;
import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;
import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpGoogleSignIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        final int code = intent.getIntExtra("CODE", -1);
        if (code == 1) {
            signOut();
            getIntent().removeExtra("CODE");
        } else {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account == null) {
                signIn();
            } else {
                setCurrentUser(account);
                authenticateWithBackend(account);
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable @org.jetbrains.annotations.Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void setUpGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Log.d(TAG, "No logged user found! Initializing signIn");
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "OAuth2.0 token acquired: " + account.getIdToken());
            setCurrentUser(account);
            authenticateWithBackend(account);
        } catch (ApiException e) {
            signIn();
            showToast(this, "Login failed with code: " + e.getStatusCode());
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void authenticateWithBackend(GoogleSignInAccount account) {
        Log.d(TAG, "Authenticating with backend server...");
        Call<Token> call = createSecurityApi().authenticate(new Token(account.getIdToken()));
        logCall(TAG, call);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(final Call<Token> call, final Response<Token> response) {
                if (response.body() == null) {
                    signOut();
                } else {
                    User user = User.getInstance();
                    user.setToken(response.body().getToken());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(final Call<Token> call, final Throwable t) {
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

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    showToast(this, "Successfully logged out!");
                    finish();
                    startActivity(getIntent());
                });
    }

    private void setCurrentUser(GoogleSignInAccount account) {
        Log.d(TAG, "User logged [id: " + account.getId() +
                " name: " + account.getDisplayName() +
                " email: " + account.getEmail() +
                " photoUrl: " + account.getPhotoUrl() + "]");

        new User(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
    }
}