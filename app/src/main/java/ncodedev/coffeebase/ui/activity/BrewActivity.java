
package ncodedev.coffeebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.process.BrewActionDTO;
import ncodedev.coffeebase.model.domain.process.BrewActionType;
import ncodedev.coffeebase.model.enums.Unit;
import ncodedev.coffeebase.web.listener.BrewResponseListener;
import ncodedev.coffeebase.web.provider.BrewApiProvider;

import java.util.Optional;

import static ncodedev.coffeebase.model.enums.Unit.*;
import static ncodedev.coffeebase.ui.activity.CoffeeActivity.COFFEE_ID_KEY;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class BrewActivity extends AppCompatActivity implements BrewResponseListener {

    private final String TAG = this.getClass().getSimpleName();

    public static String BREW = "brew";
    private Brew brew;
    private Long coffeeId;

    private TextView txtBrewName, txtMethod, coffeeWeightTxt, grinderSettingTxt, waterAmountTxt, waterTemperatureTxt,
            timeTxt, txtFilter, txtComment;
    private MaterialToolbar toolbar;
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
        txtComment = findViewById(R.id.txtComment);
        //remove later when comment will be added
        txtComment.setVisibility(View.INVISIBLE);

        toolbar = findViewById(R.id.topAppBarBrewActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, CoffeeActivity.class);
            intent.putExtra(COFFEE_ID_KEY, coffeeId);
            startActivity(intent);
        });
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    private void showBrewInfo() {
        Intent intent = getIntent();
        if (null != intent) {
            brew = intent.getSerializableExtra(BREW, Brew.class);
            coffeeId = intent.getLongExtra(COFFEE_ID_KEY, -1L);

           if (brew != null) {
               loadBrewData(brew);
           }
        }
    }

    private void loadBrewData(Brew brew) {
        Optional.ofNullable(brew.getName()).ifPresent(txtBrewName::setText);
        Optional.ofNullable(brew.getMethod()).ifPresent(txtMethod::setText);
        Optional.ofNullable(brew.getFilter()).ifPresent(txtFilter::setText);
        Optional.ofNullable(brew.getCoffeeWeightInGrams()).map(String::valueOf).ifPresent(coffeeWeight -> setTextAndUnit(coffeeWeight, coffeeWeightTxt, GRAM));
        Optional.ofNullable(brew.getGrinderSetting()).map(String::valueOf).ifPresent(grinderSetting -> setTextAndUnit(grinderSetting, grinderSettingTxt, CLICK));
        Optional.ofNullable(brew.getWaterAmountInMl()).map(String::valueOf).ifPresent(waterAmount -> setTextAndUnit(waterAmount, waterAmountTxt, ML));
        Optional.ofNullable(brew.getWaterTemp()).map(String::valueOf).ifPresent(waterTemp -> setTextAndUnit(waterTemp, waterTemperatureTxt, TEMP));
        Optional.ofNullable(brew.getTotalTime()).map(String::valueOf).ifPresent(time -> setTextAndUnit(time, timeTxt, TIME));
    }

    private void setTextAndUnit(String text, TextView textView, Unit unit) {
        String textToSet = "";
        switch(unit) {
            case GRAM -> textToSet = text + " " + getString(R.string.gram);
            case CLICK -> textToSet = text + " " + getString(R.string.click);
            case ML -> textToSet = text + " " + getString(R.string.mililiters);
            case TEMP -> textToSet = text + " " + "°C";
            case TIME -> textToSet = text + " " + getString(R.string.minutes);
        }
        textView.setText(textToSet);
    }

    //TOOLBAR - START -----------------------------------------------------------------------------------------------\\
    private boolean onMenuItemClick(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.deleteBrewMenuItem) {
            showDeleteDialog(brew.getId(), coffeeId);
            Log.d(TAG, "deleteMenuItem clicked");
        }
        return true;
    }

    private void showDeleteDialog(long brewId, long coffeeId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.detach_brew_question);
        alertDialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {});
        alertDialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            brewApiProvider.executeAction(new BrewActionDTO(BrewActionType.DETACH, coffeeId, brewId), this);
        });
        alertDialogBuilder.create().show();

    }
    //TOOLBAR END


    @Override
    public void handleExecuteActionResult() {
        Intent intent = new Intent(this, CoffeeActivity.class);
        intent.putExtra(COFFEE_ID_KEY, coffeeId);
        startActivity(intent);
    }

    @Override
    public void handleError() {
        showToast(this, getString(R.string.error));
    }
}
