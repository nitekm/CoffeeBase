
package ncodedev.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.web.listener.BrewResponseListener;
import ncodedev.coffeebase.web.provider.BrewApiProvider;

import java.util.Optional;

import static ncodedev.coffeebase.ui.CoffeeActivity.COFFEE_ID_KEY;

public class BrewActivity extends AppCompatActivity implements BrewResponseListener {

    private final String TAG = this.getClass().getSimpleName();

    public static String BREW = "brew";
    private Brew brew;
    private Integer coffeeId;

    private TextView txtBrewName, txtMethod, coffeeWeightTxt, grinderSettingTxt, waterAmountTxt, waterTemperatureTxt,
            timeTxt, txtFilter;
    private Toolbar toolbar;
    private final BrewApiProvider brewApiProvider = BrewApiProvider.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);

        initViews();
        showBrewInfo();

    }

    private void initViews() {
        txtBrewName = findViewById(R.id.txtBrewName);
        txtMethod = findViewById(R.id.txtMethod);
        coffeeWeightTxt = findViewById(R.id.coffeeWeightTxt);
        grinderSettingTxt = findViewById(R.id.grinderSettingTxt);
        waterAmountTxt = findViewById(R.id.waterAmountTxt);
        waterTemperatureTxt = findViewById(R.id.waterTempTxt);
        timeTxt = findViewById(R.id.timeTxt);
        txtFilter = findViewById(R.id.txtFilter);

        toolbar = findViewById(R.id.topAppBarBrewActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, CoffeeActivity.class);
            startActivity(intent);
        });
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    private void showBrewInfo() {
        Intent intent = getIntent();
        if (null != intent) {
            //tutaj tez musisz zgetować i ustawić coffeeId do dalszych metod
            brew = intent.getSerializableExtra(BREW, Brew.class);
            coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);

           if (brew != null) {
               loadBrewData(brew);
           }
        }
    }

    private void loadBrewData(Brew brew) {
        Optional.ofNullable(brew.getName()).ifPresent(txtBrewName::setText);
        Optional.ofNullable(brew.getMethod()).ifPresent(txtMethod::setText);
        Optional.ofNullable(brew.getFilter()).ifPresent(txtFilter::setText);
        Optional.ofNullable(brew.getCoffeeWeightInGrams()).ifPresent(coffeeWeightTxt::setText);
        Optional.ofNullable(brew.getGrinderSetting()).ifPresent(grinderSettingTxt::setText);
        Optional.ofNullable(brew.getWaterAmountInMl()).ifPresent(waterAmountTxt::setText);
        Optional.ofNullable(brew.getWaterTemp()).ifPresent(waterTemperatureTxt::setText);
        Optional.ofNullable(brew.getTotalTime()).ifPresent(timeTxt::setText);
    }

    //TOOLBAR - START -----------------------------------------------------------------------------------------------\\
    private boolean onMenuItemClick(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.deleteBrewMenuItem) {
            showDeleteDialog(brew.getId(), coffeeId);
            Log.d(TAG, "deleteMenuItem clicked");
        }
        return true;
    }

    private void showDeleteDialog(long brewId, int coffeeId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.detach_brew_question);
        alertDialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {});
        alertDialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            brewApiProvider.detachBrewFromCoffee(brewId, coffeeId, this, this);
        });
        alertDialogBuilder.create().show();

    }
    //TOOLBAR END


    @Override
    public void handleDetachBrewFromCoffee() {
        startActivity(new Intent(this, CoffeeActivity.class));
    }
}