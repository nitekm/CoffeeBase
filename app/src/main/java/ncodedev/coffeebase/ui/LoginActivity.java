package ncodedev.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.service.GoogleSignInClientService;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private GoogleSignInClientService googleSignInClientService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "in loginActivity");

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
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleSignInClientService.handleSignInResult(task);
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void signIn() {
        Log.d(TAG, "No logged user found! Initializing signIn");
        Intent intent = googleSignInClientService.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(intent, 0);
    }
}
