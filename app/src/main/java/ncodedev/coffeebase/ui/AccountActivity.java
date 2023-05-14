package ncodedev.coffeebase.ui;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.subscription.CoffeeBaseStore;
import ncodedev.coffeebase.subscription.CoffeeBaseStoreResponseListener;
import ncodedev.coffeebase.ui.utility.ImageHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountActivity extends AppCompatActivity implements CoffeeBaseStoreResponseListener, PurchasesUpdatedListener {

    private static final String TAG = "AccountActivity";
    private ImageView accountUserPictureImage;
    private Button sub1Btn, sub2Btn;
    private TextView accountUserNameTxt;
    private final ImageHelper imageHelper = ImageHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViews();
        fetchProducts();
    }

    private void initViews() {
        accountUserPictureImage = findViewById(R.id.accountUserPictureImage);
        sub1Btn = findViewById(R.id.sub1Btn);
        sub2Btn = findViewById(R.id.sub2Btn);
        accountUserNameTxt = findViewById(R.id.accountUserNameTxt);

        var user = User.getInstance();
        accountUserNameTxt.setText(user.getUsername());
        if (user.getPictureUri() != null) {
            imageHelper.picassoSetImage(user.getPictureUri(), accountUserPictureImage, R.drawable.ic_account);
        }
    }

    private void fetchProducts() {
        CoffeeBaseStore coffeeBaseStore = new CoffeeBaseStore(this);
        coffeeBaseStore.getAvailableProducts(this);
    }

    @Override
    public void handleProductDetailsList(List<ProductDetails> productDetailsList) {
        if (productDetailsList.isEmpty()) {
            Log.i(TAG, "handleProductDetailsList: emptylistbutworking");
        }
        productDetailsList.forEach(product -> Log.i(TAG, "handleProductDetailsList: " + product.getProductId() + "\n"
                + product.getName() + "\n"
                + product.getDescription()));
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

    }
}