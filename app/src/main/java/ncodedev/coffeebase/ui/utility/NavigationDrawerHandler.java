package ncodedev.coffeebase.ui.utility;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.service.GoogleSignInClientService;
import ncodedev.coffeebase.ui.activity.*;

public class NavigationDrawerHandler extends MainActivity {

    private final AppCompatActivity mainActivity;
    private GoogleSignInClientService googleSignInClientService;

    public NavigationDrawerHandler(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
        googleSignInClientService = new GoogleSignInClientService(mainActivity);
    }

    public void setUpNavigationDrawer(MaterialToolbar toolbar, DrawerLayout drawerLayout, NavigationView navigationView) {
        mainActivity.setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mainActivity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return true;
                });
    }

    @SuppressLint("NonConstantResourceId")
    private void selectDrawerItem(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCoffee -> launchEditCoffee();
            case R.id.addBrew -> launchEditBrew();
            case R.id.about -> showAbout();
            case R.id.account -> showAccountInfo();
            case R.id.settings -> launchSettings();
            case R.id.signout -> googleSignInClientService.signOut();
            default -> {}
        }
    }

    private void launchEditCoffee() {
        Intent intent = new Intent(mainActivity, EditCoffee.class);
        mainActivity.startActivity(intent);
    }

    private void launchEditBrew() {
        Intent intent = new Intent(mainActivity, AddBrewActivity.class);
        mainActivity.startActivity(intent);
    }

    private void showAbout() {
        Dialog aboutDialog = new Dialog(mainActivity);
        aboutDialog.setContentView(R.layout.about_dialog);

        String aboutContentFormatted = mainActivity.getString(R.string.about_content);
        Spanned spannedAboutContent = Html.fromHtml(aboutContentFormatted);

        TextView aboutContentTxt = aboutDialog.findViewById(R.id.aboutContentTxt);
        aboutContentTxt.setText(spannedAboutContent);

        aboutDialog.show();
    }

    private void launchSettings() {
        Intent intent = new Intent(mainActivity, SettingsActivity.class);
        mainActivity.startActivity(intent);
    }

    private void showAccountInfo() {
        Intent intent = new Intent(mainActivity, AccountActivity.class);
        mainActivity.startActivity(intent);
    }
}
