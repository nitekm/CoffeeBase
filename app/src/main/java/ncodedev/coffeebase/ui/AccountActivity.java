package ncodedev.coffeebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails;
import com.android.billingclient.api.Purchase;
import com.google.android.material.appbar.MaterialToolbar;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.subscription.*;
import ncodedev.coffeebase.ui.utility.ImageHelper;
import ncodedev.coffeebase.web.provider.SubscriptionApiProvider;

import java.util.List;

import static ncodedev.coffeebase.subscription.SubscriptionBasePlan.MONTHLY_1_99;
import static ncodedev.coffeebase.subscription.SubscriptionBasePlan.MONTHLY_4_99;

public class AccountActivity extends AppCompatActivity implements SubscriptionResponseListener {

    private static final String TAG = "AccountActivity";
    private ImageView accountUserPictureImage;
    private Button sub1Btn, sub2Btn;
    private TextView accountUserNameTxt;
    private final ImageHelper imageHelper = ImageHelper.getInstance();
    private BillingClientSetup billingClientSetup;
    private final SubscriptionApiProvider subscriptionApiProvider = SubscriptionApiProvider.getInstance();

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

        MaterialToolbar toolbar = findViewById(R.id.topAppBarCoffeeActivity);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        billingClientSetup = new BillingClientSetup(this);
    }

    /*
    SUBSCRIPTION START
     */

    private void fetchProducts() {
        var subscriptionProduct = new SubscriptionProduct(this, billingClientSetup.getBillingClient());
        subscriptionProduct.connectAndGetAvailableProducts(this);
    }

    private void fetchPurchases() {
        var purchasedSubscriptionsProvider = new PurchasedSubscriptionsProvider(this, billingClientSetup.getBillingClient());
        purchasedSubscriptionsProvider.connectAndGetPurchasedSubscriptions(this);
    }

    @Override
    public void handlePurchasesList(List<Purchase> purchases) {
        if (purchases.isEmpty()) {
            Log.e(TAG, "Empty PurchaseList returned from google API");
        }
    }

    @Override
    public void handleProductDetailsList(List<ProductDetails> productDetailsList) {
        if (productDetailsList.isEmpty()) {
            Log.e(TAG, "Empty ProductDetailsList returned from google API");
            return;
        }
        fetchPurchases();
        initButtonClickListeners(productDetailsList);
    }

    private void initButtonClickListeners(List<ProductDetails> productDetailsList) {
        var productDetails = productDetailsList.get(0);
        var subscriptionOfferDetails = productDetails.getSubscriptionOfferDetails();

        if (subscriptionOfferDetails == null || subscriptionOfferDetails.isEmpty()) {
            Log.e(TAG, "Empty SubscriptionOfferDetails returned from google API");
            return;
        }

        for (SubscriptionOfferDetails offerDetails: subscriptionOfferDetails) {
            var offerBasePlanId = offerDetails.getBasePlanId();
            if (offerBasePlanId.equals(MONTHLY_1_99.getValue())) {
                sub1Btn.setOnClickListener(view -> processSubscription(productDetails, offerDetails));
            } else if (offerBasePlanId.equals(MONTHLY_4_99.getValue())) {
                sub2Btn.setOnClickListener(view -> processSubscription(productDetails, offerDetails));
            }
        }
    }

    private void processSubscription(ProductDetails productDetails, SubscriptionOfferDetails offerDetails) {
        SubscriptionPurchase subscriptionPurchase = new SubscriptionPurchase(billingClientSetup.getBillingClient());
        subscriptionPurchase.processSubscriptionPurchase(this, productDetails, offerDetails);
    }

    /*
    SUBSCRIPTION END
     */
}
