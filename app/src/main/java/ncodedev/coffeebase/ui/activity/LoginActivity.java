package ncodedev.coffeebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.service.GoogleSignInClientService;

import static ncodedev.coffeebase.service.ChangeLanguageHandler.translateIU;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private GoogleSignInClientService googleSignInClientService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "in loginActivity");

        translateIU(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        googleSignInClientService = new GoogleSignInClientService(this);

        //code 2 - standard sign in
        int code = getIntent().getIntExtra("code", -1);
        if (code ==  -1) {
            googleSignInClientService.silentSignInWithRedirect();
        }
        if (code == 2) {
            getIntent().removeExtra("code");
            signIn();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleSignInClientService.handleSignInResult(task);
        }
    }



    private void signIn() {
        Log.d(TAG, "No logged user found! Initializing signIn");
        Intent intent = googleSignInClientService.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(intent, 0);
    }
}
