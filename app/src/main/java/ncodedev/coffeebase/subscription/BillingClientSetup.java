package ncodedev.coffeebase.subscription;

import android.content.Context;
import androidx.annotation.NonNull;
import com.android.billingclient.api.*;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ncodedev.coffeebase.subscription.BillingClientUtils.billingResultOK;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class BillingClientSetup {

    private final Context context;
    private final BillingClient billingClient;

    public BillingClientSetup(Context context) {
        this.context = context;
        billingClient = initializeBillingClient();
    }

    public BillingClient initializeBillingClient() {
        return BillingClient.newBuilder(context)
                .setListener(createPurchasesUpdatedListener())
                .enablePendingPurchases()
                .build();
    }

    private PurchasesUpdatedListener createPurchasesUpdatedListener() {
        return (this::handlePurchasesUpdatedResponse);
    }

    private void handlePurchasesUpdatedResponse(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResultOK(billingResult)) {
            purchases.stream()
                    .filter(purchase -> !purchase.isAcknowledged())
                    .forEach(this::acknowledgePurchase);
        }
    }

    private void acknowledgePurchase(Purchase purchase) {
        billingClient.acknowledgePurchase(
                prepareParams(purchase.getPurchaseToken()),
                billingResult -> {
                    if (BillingClientUtils.billingResultOK(billingResult)) {
                        showToast(context, context.getString(R.string.thank_you) + " <3");
                    }
                });
    }

    private AcknowledgePurchaseParams prepareParams(String token) {
        return AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(token)
                .build();
    }

    public BillingClient getBillingClient() {
        return billingClient;
    }

}
