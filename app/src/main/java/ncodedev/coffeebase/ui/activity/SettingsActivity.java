package ncodedev.coffeebase.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.google.android.material.appbar.MaterialToolbar;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.enums.LanguageCode;
import ncodedev.coffeebase.service.ChangeLanguageHandler;
import ncodedev.coffeebase.service.GoogleSignInClientService;
import ncodedev.coffeebase.web.listener.UserSettingsResponseListener;
import ncodedev.coffeebase.web.provider.UserSettingsApiProvider;

import static ncodedev.coffeebase.model.enums.LanguageCode.*;
import static ncodedev.coffeebase.service.SharedPreferencesNames.LANGUAGE;
import static ncodedev.coffeebase.service.SharedPreferencesNames.MY_COFFEEBASE_COFFEES_IN_ROW;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class SettingsActivity extends AppCompatActivity implements UserSettingsResponseListener {

    private Spinner languageSpinner, coffeesInRowSpinner;
    private Button saveSettingsBtn;
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

        coffeesInRowSpinner = findViewById(R.id.coffeesInRowSpinner);
        setUpCoffeesInRowSpinner();

        saveSettingsBtn = findViewById(R.id.saveSettingsBtn);
        saveSettingsBtn.setOnClickListener(view -> saveSettings());

        MaterialToolbar toolbar = findViewById(R.id.topAppBarSettingsActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void setUpLanguageSpinner() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final var languageCode = valueOf(sharedPreferences.getString(LANGUAGE, "EN"));
        switch (languageCode) {
            case EN -> languageSpinner.setSelection(0);
            case ES -> languageSpinner.setSelection(1);
            case DE -> languageSpinner.setSelection(2);
            case FR -> languageSpinner.setSelection(3);
            case PL -> languageSpinner.setSelection(4);
            case PT -> languageSpinner.setSelection(5);
        }
    }

    private void setUpCoffeesInRowSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.coffees_in_row, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coffeesInRowSpinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final var coffeesInRow = sharedPreferences.getInt(MY_COFFEEBASE_COFFEES_IN_ROW, 2);
        switch (coffeesInRow) {
            case 2 -> coffeesInRowSpinner.setSelection(0);
            case 3 -> coffeesInRowSpinner.setSelection(1);
            case 4 -> coffeesInRowSpinner.setSelection(2);
            case 5 -> coffeesInRowSpinner.setSelection(3);
        }
    }

    private void saveSettings() {
        applyChangeLanguage();
        applyChangeCoffeesInRow();
    }

    private void applyChangeLanguage() {
        LanguageCode chosenLanguageCode = ChangeLanguageHandler.getLanguageCodeByValue(languageSpinner.getSelectedItem().toString());
        switch (chosenLanguageCode) {
            case EN -> ChangeLanguageHandler.changeLanguage(EN, SettingsActivity.this);
            case ES -> ChangeLanguageHandler.changeLanguage(ES, SettingsActivity.this);
            case DE -> ChangeLanguageHandler.changeLanguage(DE, SettingsActivity.this);
            case FR -> ChangeLanguageHandler.changeLanguage(FR, SettingsActivity.this);
            case PL -> ChangeLanguageHandler.changeLanguage(PL, SettingsActivity.this);
            case PT -> ChangeLanguageHandler.changeLanguage(PT, SettingsActivity.this);
        }

        Intent intent = this.getIntent();
        this.finish();
        this.startActivity(intent);
    }

    private void applyChangeCoffeesInRow() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putInt(MY_COFFEEBASE_COFFEES_IN_ROW, Integer.parseInt((String) coffeesInRowSpinner.getSelectedItem())).apply();
    }

    private void setUpDeleteAccountBtnAction() {
        Button deleteAccountBtn = findViewById(R.id.deleteAccountBtn);
        deleteAccountBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.delete_account_question);
            alertDialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
            });
            alertDialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> userSettingsApiProvider.deleteAccount(this));
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
