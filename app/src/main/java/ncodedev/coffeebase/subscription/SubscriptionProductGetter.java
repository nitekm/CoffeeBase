package ncodedev.coffeebase.subscription;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.billingclient.api.*;
import ncodedev.coffeebase.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class SubscriptionProductGetter {

    private final static String TAG = "CoffeeBaseStore";

    private final Context context;
    private final BillingClient billingClient;

    public SubscriptionProductGetter(Context context) {
        this.context = context;
        billingClient = initializeBillingClient(context);
    }

    private BillingClient initializeBillingClient(Context context) {
        return BillingClient.newBuilder(context)
                .setListener(createPurchasesUpdatedListener())
                .enablePendingPurchases()
                .build();
    }

    private PurchasesUpdatedListener createPurchasesUpdatedListener() {
        return ((billingResult, list) -> handlePurchasesUpdatedResponse(billingResult));
    }

    private void handlePurchasesUpdatedResponse(BillingResult billingResult) {
        if (billingResultOK(billingResult)) {
            showToast(context, context.getString(R.string.thank_you) + " <3");
        }
    }

    public void getAvailableProducts(SubscriptionResponseListener listener) {
        getProductsFromGooglePlay(listener);
    }

    private void getProductsFromGooglePlay(SubscriptionResponseListener listener) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected: retrying connection...");
                initializeBillingClient(context);
            }

            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
                if (billingResultOK(billingResult)) {
                    getAvailableSubscriptions(listener);
                }
            }
        });
    }

    private void getAvailableSubscriptions(SubscriptionResponseListener listener) {
        billingClient.queryProductDetailsAsync(
                prepareParams(),
                (billingResult, productDetailsList) -> {
                    if (billingResultOK(billingResult)) {
                        listener.handleProductDetailsList(productDetailsList);
                    }
                });
    }

    private QueryProductDetailsParams prepareParams() {
        String productId = "coffeebase_subscription_basic";
        return QueryProductDetailsParams.newBuilder()
                .setProductList(
                        List.of(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(productId)
                                .setProductType(BillingClient.ProductType.SUBS)
                                .build()))
                .build();
    }

    private boolean billingResultOK(BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            return true;
        } else {
            Log.d(TAG, "onBillingSetupFinished with code: " + billingResult.getResponseCode() +
                    "\nAnd message: " + billingResult.getDebugMessage());
            return false;
        }
    }

    public BillingClient getBillingClient() {
        return billingClient;
    }
}
