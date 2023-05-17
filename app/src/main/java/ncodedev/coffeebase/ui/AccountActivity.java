package ncodedev.coffeebase.ui;

import android.app.Activity;
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
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.security.User;
import ncodedev.coffeebase.subscription.SubscriptionBasePlan;
import ncodedev.coffeebase.subscription.SubscriptionProcessor;
import ncodedev.coffeebase.subscription.SubscriptionProductGetter;
import ncodedev.coffeebase.subscription.SubscriptionResponseListener;
import ncodedev.coffeebase.ui.utility.ImageHelper;

import java.util.List;

import static ncodedev.coffeebase.subscription.SubscriptionBasePlan.MONTHLY_1_99;
import static ncodedev.coffeebase.subscription.SubscriptionBasePlan.MONTHLY_4_99;

public class AccountActivity extends AppCompatActivity implements SubscriptionResponseListener {

    private static final String TAG = "AccountActivity";
    private ImageView accountUserPictureImage;
    private Button sub1Btn, sub2Btn;
    private TextView accountUserNameTxt;
    private final ImageHelper imageHelper = ImageHelper.getInstance();
    private SubscriptionProductGetter subscriptionProductGetter;

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
        subscriptionProductGetter = new SubscriptionProductGetter(this);
        subscriptionProductGetter.getAvailableProducts(this);
    }

    @Override
    public void handleProductDetailsList(List<ProductDetails> productDetailsList) {
        if (productDetailsList.isEmpty()) {
            Log.e(TAG, "Empty ProductDetailsList returned from google API");
            return;
        }
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
        SubscriptionProcessor subscriptionProcessor = new SubscriptionProcessor(subscriptionProductGetter.getBillingClient());
        subscriptionProcessor.processSubscriptionPurchase(this, productDetails, offerToken);
    }
}