package ncodedev.coffeebase.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.appbar.MaterialToolbar;
import ncodedev.coffeebase.R;

public class EditBrewActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private ImageButton btnPrevStep, btnNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brew);

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.topAppBarBrewActivity);
        toolbar.getMenu().getItem(0).setVisible(false);
        progressBar = findViewById(R.id.brewProgressBar);
        btnPrevStep = findViewById(R.id.btnPrevStep);
        btnNextStep = findViewById(R.id.btnNextStep);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.step_container, new BrewStepGeneralInfoFragment(btnPrevStep, btnNextStep));
        transaction.commit();
    }
}