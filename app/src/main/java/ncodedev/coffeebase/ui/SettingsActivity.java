package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.service.GoogleSignInClientService;
import ncodedev.coffeebase.web.listener.UserSettingsResponseListener;
import ncodedev.coffeebase.web.provider.UserSettingsApiProvider;

import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class SettingsActivity extends AppCompatActivity implements UserSettingsResponseListener {

    private final UserSettingsApiProvider userSettingsApiProvider = UserSettingsApiProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setUpDeleteAccountBtnAction();
    }

    private void setUpDeleteAccountBtnAction() {
        Button deleteAccountBtn = findViewById(R.id.deleteAccountBtn);
        deleteAccountBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.delete_account_question);
            alertDialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {});
            alertDialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> userSettingsApiProvider.deleteAccount( this));
            alertDialogBuilder.create().show();
        });
    }

    @Override
    public void handleDeleteAccount() {
        new GoogleSignInClientService(this).signOut();
    }

    @Override
    public void handleError() {
        showToast(this, getString(R.string.error));
    }
}
