package ncodedev.coffeebase.service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import ncodedev.coffeebase.BuildConfig;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.ui.activity.LoginActivity;
import ncodedev.coffeebase.ui.activity.main.MainActivity;

import static ncodedev.coffeebase.R.string.*;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class GoogleSignInClientService {

    private static final String TAG = "GoogleSignInClientService";

    private GoogleSignInClient googleSignInClient;
    private Activity context;

    public GoogleSignInClientService(Activity context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.ClientId)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public void silentSignInWithRedirect() {

        final Task<GoogleSignInAccount> silentSignInTask = googleSignInClient.silentSignIn();
        if (silentSignInTask.isSuccessful()) {
            setCurrentUser(silentSignInTask.getResult());
            showToast(context, context.getString(welcome_back));
            Log.d(TAG, "silentSignInWithRedirect: calling main activity after silent signin");
            context.startActivity(new Intent(context, MainActivity.class));
        } else {
            silentSignInTask.addOnSuccessListener(task -> {
                handleSignInResult(silentSignInTask);
                showToast(context, context.getString(welcome_back));
                context.startActivity(new Intent(context, MainActivity.class));
            });
            silentSignInTask.addOnFailureListener(task -> signOut());
        }
    }

    public void silentSignIn() {
        final Task<GoogleSignInAccount> silentSignInTask = googleSignInClient.silentSignIn();
        if (silentSignInTask.isSuccessful()) {
            handleSignInResult(silentSignInTask);
        } else {
            silentSignInTask.addOnSuccessListener(task -> handleSignInResult(silentSignInTask));
            silentSignInTask.addOnFailureListener(task -> signOut());
        }
    }

    public void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(context, task -> {
            showToast(context, context.getString(successfully_logged_out));
            User.clearUserData();
            context.finish();
            startLoginActivityWithSignIn();
        });
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "OAuth2.0 token acquired");
            setCurrentUser(account);
            context.startActivity(new Intent(context, MainActivity.class));
        } catch (ApiException e) {
            startLoginActivityWithSignIn();
            showToast(context, context.getString(login_failed));
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void startLoginActivityWithSignIn() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("code", 2);
        context.startActivity(intent);
    }

    private void setCurrentUser(GoogleSignInAccount account) {
        Log.d(TAG, "User logged [\nid: " + account.getId() +
                "\nname: " + account.getDisplayName() +
                "\nemail: " + account.getEmail() +
                "\nphotoUrl: " + account.getPhotoUrl());

        new User(account.getId(),
                account.getDisplayName(),
                account.getEmail(),
                account.getPhotoUrl().toString(),
                account.getIdToken());
    }

}
