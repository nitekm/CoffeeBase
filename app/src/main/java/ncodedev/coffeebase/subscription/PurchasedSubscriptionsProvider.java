package ncodedev.coffeebase.subscription;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.QueryPurchasesParams;
import org.jetbrains.annotations.NotNull;

import static ncodedev.coffeebase.subscription.BillingClientUtils.billingResultOK;

public class PurchasedSubscriptionsProvider {

    private static final String TAG = "PurchasedSubscriptionsProvider";

    private final Context context;
    private final BillingClient billingClient;

    public PurchasedSubscriptionsProvider(Context context, BillingClient billingClient) {
        this.context = context;
        this.billingClient = billingClient;
    }

    public void connectAndGetPurchasedSubscriptions(SubscriptionResponseListener listener) {
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
                    getPurchasedSubscriptions(listener);
                }
            }
        });
    }

    private void getPurchasedSubscriptions(SubscriptionResponseListener listener) {
        billingClient.queryPurchasesAsync(
                prepareParams(),
                (billingResult, purchases) -> {
                    if (billingResultOK(billingResult)) {
                        listener.handlePurchasesList(purchases);
                    }
                });
    }

    private QueryPurchasesParams prepareParams() {
        return QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build();
    }
}
