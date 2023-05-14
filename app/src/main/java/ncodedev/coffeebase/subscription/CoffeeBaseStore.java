package ncodedev.coffeebase.subscription;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.billingclient.BuildConfig;
import com.android.billingclient.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CoffeeBaseStore {

    private final static String TAG = "CoffeeBaseStore";

    private final Context context;
    private final BillingClient billingClient;

    public CoffeeBaseStore(Context context) {
        this.context = context;
        billingClient = initializeBillingClient(context);
    }

    private BillingClient initializeBillingClient(Context context) {
        return BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
    }

    private final PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, list) -> {
        //TODO
    };

    public void getAvailableProducts(CoffeeBaseStoreResponseListener listener) {
        getProductsFromGooglePlay(listener);
    }

    private void getProductsFromGooglePlay(CoffeeBaseStoreResponseListener listener) {
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

    private void getAvailableSubscriptions(CoffeeBaseStoreResponseListener listener) {
        billingClient.queryProductDetailsAsync(
                prepareParams(),
                (billingResult, productDetailsList) -> {
                    if (billingResultOK(billingResult)) {
                        listener.handleProductDetailsList(productDetailsList);
                    }
                });
    }

    private QueryProductDetailsParams prepareParams() {
        String projectId = "109227410439";
        return QueryProductDetailsParams.newBuilder()
                .setProductList(
                        List.of(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(projectId)
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
}
