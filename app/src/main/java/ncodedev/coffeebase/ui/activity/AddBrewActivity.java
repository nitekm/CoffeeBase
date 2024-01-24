package ncodedev.coffeebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.appbar.MaterialToolbar;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.process.BrewStatus;
import ncodedev.coffeebase.ui.activity.main.MainActivity;
import ncodedev.coffeebase.ui.fragment.brewstep.BrewStepGeneralInfoFragment;

public class AddBrewActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private ImageButton btnPrevStep, btnNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brew);

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.topAppBarBrewActivity);
        toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        progressBar = findViewById(R.id.brewProgressBar);
        btnPrevStep = findViewById(R.id.btnPrevStep);
        btnNextStep = findViewById(R.id.btnNextStep);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.step_container, new BrewStepGeneralInfoFragment(new Brew(BrewStatus.STARTED), btnPrevStep, btnNextStep, progressBar));
        transaction.commit();
    }
}
