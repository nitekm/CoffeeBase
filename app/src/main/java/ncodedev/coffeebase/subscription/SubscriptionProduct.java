package ncodedev.coffeebase.subscription;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.android.billingclient.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ncodedev.coffeebase.subscription.BillingClientUtils.billingResultOK;

public class SubscriptionProduct {

    private final Context context;
    private final BillingClient billingClient;

    public SubscriptionProduct(Context context, BillingClient billingClient) {
        this.context = context;
        this.billingClient = billingClient;
    }

    private final static String TAG = "CoffeeBaseStore";

    public void connectAndGetAvailableProducts(SubscriptionResponseListener listener) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected: retrying connection...");
                BillingClientSetup billingClientSetup = new BillingClientSetup(context);
                billingClientSetup.initializeBillingClient();
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

}
