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
import com.ncodedev.coffeebase.model.security.User;

import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;

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

        //code 1 passed by sign out request
        final int code = getIntent().getIntExtra("CODE", -1);

        signOutOnRequest(code);
        handleGetLastSignedInAccount();
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

    private void signOutOnRequest(int code) {
        getIntent().removeExtra("CODE");
        if (code != 1) {
            return;
        }
        googleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    showToast(this, "Successfully logged out!");
                    finish();
                    startActivity(getIntent());
                });
    }

    private void handleGetLastSignedInAccount() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            signIn();
        } else {
            setCurrentUser(account);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "OAuth2.0 token acquired: " + account.getIdToken());
            setCurrentUser(account);
        } catch (ApiException e) {
            signIn();
            showToast(this, "Login failed with code: " + e.getStatusCode());
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signIn() {
        Log.d(TAG, "No logged user found! Initializing signIn");
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    private void setCurrentUser(GoogleSignInAccount account) {
        Log.d(TAG, "User logged [\nid: " + account.getId() +
                "\nname: " + account.getDisplayName() +
                "\nemail: " + account.getEmail() +
                "\nphotoUrl: " + account.getPhotoUrl() +
                "\ntoken " + account.getIdToken() + "]");

        new User(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString(), account.getIdToken());
    }
}
