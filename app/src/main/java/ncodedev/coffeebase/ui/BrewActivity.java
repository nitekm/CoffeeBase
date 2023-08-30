
package ncodedev.coffeebase.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;

public class BrewActivity extends AppCompatActivity {

    public static String BREW_ID_KEY = "brewId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);
    }
}