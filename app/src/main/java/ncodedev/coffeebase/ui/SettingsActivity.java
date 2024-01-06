package ncodedev.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
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
        setUpLanguageSpinner();

        MaterialToolbar toolbar = findViewById(R.id.topAppBarSettingsActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    //TODO: zmień - spinner ustawia a dodaj guzik zapisz i on bierze wtedy wartość ze spinnera, podmienia język i odświezą activity.
    // Dodatkowo, można z shared preferences brać język i ustawiać spinner na pozycji jaka aktualnie jest wybrana
    private void setUpLanguageSpinner() {
        languageSpinner.getSelectedItem();
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LanguageCode chosenLanguageCode = ChangeLanguageHandler.getLanguageCodeByValue(parent.getItemAtPosition(position).toString());

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
