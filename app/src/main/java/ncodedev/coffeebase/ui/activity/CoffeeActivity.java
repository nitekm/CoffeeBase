package ncodedev.coffeebase.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.error.ErrorResponse;
import ncodedev.coffeebase.ui.activity.main.MainActivity;
import ncodedev.coffeebase.ui.fragment.BrewsFragment;
import ncodedev.coffeebase.ui.fragment.CoffeeInfo;
import ncodedev.coffeebase.ui.view.adapter.CoffeeTabPagerAdapter;
import ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;

import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class CoffeeActivity extends AppCompatActivity implements CoffeeResponseListener {
    private static final String TAG = "CoffeeActivity";
    public static final String COFFEE_ID_KEY = "coffeeId";
    private long coffeeId;
    private MaterialToolbar toolbar;
    private ActionMenuItemView favouriteMenuItem;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        initViews();
        setToolbar();
        showCoffeeInfo();
    }

    private void initViews() {
        toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        favouriteMenuItem = findViewById(R.id.favouritesMenuItem);
        viewPager = findViewById(R.id.viewPagerCoffeeActivity);
        tabLayout = findViewById(R.id.tabLayoutCoffeeActivity);
    }

    private void showCoffeeInfo() {
        Intent intent = getIntent();
        if (null != intent) {
            coffeeId = intent.getLongExtra(COFFEE_ID_KEY, -1L);
            if (coffeeId != -1) {
                coffeeApiProvider.getOne(coffeeId, this);
            }
        }
    }

    //TOOLBAR - START -----------------------------------------------------------------------------------------------\\
    private void setToolbar() {
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @SuppressLint({"NonConstantResourceId", "UnsafeIntentLaunch"})
    private boolean onMenuItemClick(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favouritesMenuItem -> {
                coffeeApiProvider.switchFavourites(coffeeId, this);
                finish();
                startActivity(getIntent());
                Log.d(TAG, "addToFavouritesMenuItem clicked");
                return true;
            }
            case R.id.editMenuItem -> {
                Intent intent = new Intent(this, EditCoffee.class);
                intent.putExtra(COFFEE_ID_KEY, coffeeId);
                startActivity(intent);
                Log.d(TAG, "editMenuItem clicked");
                return true;
            }
            case R.id.deleteMenuItem -> {
                showDeleteDialog(coffeeId);
                Log.d(TAG, "deleteMenuItem clicked");
                return true;
            }
            default -> {
                return true;
            }
        }
    }

    private void showDeleteDialog(long coffeeId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.delete_coffee_question);
        alertDialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {});
        alertDialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            coffeeApiProvider.delete(coffeeId, this);
        });
        alertDialogBuilder.create().show();

    }
    //TOOLBAR - END -------------------------------------------------------------------------------------------------\\

    @SuppressLint("RestrictedApi")
    @Override
    public void handleCoffeeResponse(Coffee coffee) {
        CoffeeTabPagerAdapter pagerAdapter = new CoffeeTabPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addTabFragment(new CoffeeInfo(coffee), getString(R.string.coffee_info));
        pagerAdapter.addTabFragment(new BrewsFragment(coffee.getBrews(), coffeeId), getString(R.string.brews));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (coffee.isFavourite()) {
            favouriteMenuItem.setIcon(getDrawable(R.drawable.ic_favorite_filled));
        }
    }

    @Override
    public void handleDeleteResponse() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void handleSaveResponse(Coffee coffee) {}

    @Override
    public void handleError(ErrorResponse errorResponse) {
        showToast(this, getString(R.string.error));
    }
}
