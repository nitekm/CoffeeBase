package ncodedev.coffeebase.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.billingclient.api.*;
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails;
import com.google.android.material.appbar.MaterialToolbar;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.subscription.*;
import ncodedev.coffeebase.ui.utility.ImageHelper;

import java.util.List;
import java.util.stream.Collectors;

import static ncodedev.coffeebase.subscription.SubscriptionBasePlan.MONTHLY_1_99;
import static ncodedev.coffeebase.subscription.SubscriptionBasePlan.MONTHLY_4_99;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class AccountActivity extends AppCompatActivity implements SubscriptionResponseListener {

    private static final String TAG = "AccountActivity";
    private ImageView accountUserPictureImage;
    private Button sub1Btn, sub2Btn;
    private TextView accountUserNameTxt;
    private final ImageHelper imageHelper = ImageHelper.getInstance();

    private BillingClientSetup billingClientSetup;

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
            return;
        }

//        purchases.stream()
//                .filter(purchase -> purchase.getPurchaseState() == 1)
//                .map(purchase -> purchase.getProducts().get(0))
//                .forEach(this::blockButtonAndSetText);
    }

    private void blockButtonAndSetText(String purchaseName) {
        if (purchaseName.equals(MONTHLY_1_99.getValue())) {
            sub1Btn.setEnabled(false);
            sub1Btn.setText(R.string.purchased);
        } else if (purchaseName.equals(MONTHLY_4_99.getValue())) {
            sub2Btn.setEnabled(false);
            sub2Btn.setText(R.string.purchased);
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
                sub1Btn.setOnClickListener(view -> processSubscription(productDetails, offerDetails.getOfferToken()));
            } else if (offerBasePlanId.equals(MONTHLY_4_99.getValue())) {
                sub2Btn.setOnClickListener(view -> processSubscription(productDetails, offerDetails.getOfferToken()));
            }
        }
    }

    private void processSubscription(ProductDetails productDetails, String offerToken) {
        SubscriptionPurchase subscriptionPurchase = new SubscriptionPurchase(billingClientSetup.getBillingClient());
        subscriptionPurchase.processSubscriptionPurchase(this, productDetails, offerToken);
    }
}