package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.service.GoogleSignInClientService;
import ncodedev.coffeebase.ui.utility.ChangeLanguageHandler;
import ncodedev.coffeebase.ui.utility.LanguageCode;
import ncodedev.coffeebase.web.listener.UserSettingsResponseListener;
import ncodedev.coffeebase.web.provider.UserSettingsApiProvider;

import static ncodedev.coffeebase.ui.utility.LanguageCode.*;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class SettingsActivity extends AppCompatActivity implements UserSettingsResponseListener {

    private Spinner languageSpinner;
    private final UserSettingsApiProvider userSettingsApiProvider = UserSettingsApiProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setUpDeleteAccountBtnAction();
    }

    private void initViews() {
        languageSpinner = findViewById(R.id.languageSpinner);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        LanguageCode.stream().forEach(languageCode -> languageAdapter.add(languageCode.getValue()));
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               LanguageCode chosenLanguageCode = LanguageCode.valueOf(parent.getItemAtPosition(position).toString());

               switch(chosenLanguageCode) {
                   case EN -> ChangeLanguageHandler.changeLanguage(EN, SettingsActivity.this);
                   case ES -> ChangeLanguageHandler.changeLanguage(ES, SettingsActivity.this);
                   case DE -> ChangeLanguageHandler.changeLanguage(DE, SettingsActivity.this);
                   case FR -> ChangeLanguageHandler.changeLanguage(FR, SettingsActivity.this);
                   case PL -> ChangeLanguageHandler.changeLanguage(PL, SettingsActivity.this);
                   case PT -> ChangeLanguageHandler.changeLanguage(PT, SettingsActivity.this);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
