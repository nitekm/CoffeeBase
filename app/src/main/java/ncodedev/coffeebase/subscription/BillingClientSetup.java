package ncodedev.coffeebase.subscription;

import android.content.Context;
import com.android.billingclient.api.*;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Subscription;
import ncodedev.coffeebase.web.provider.SubscriptionApiProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        savePurchase(purchase);
    }

    private AcknowledgePurchaseParams prepareParams(String token) {
        return AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(token)
                .build();
    }

    private void savePurchase(Purchase purchase) {
        final String productId = purchase.getProducts().get(0);
        final Date purchaseDate = new Date(purchase.getPurchaseTime());
        final String formattedPurchasedDate =
                SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(purchaseDate);
        final boolean active = purchase.getPurchaseState() == 1;

        final Subscription subscription = new Subscription(
                productId, purchase.getPurchaseToken(), active, formattedPurchasedDate
        );
        SubscriptionApiProvider.getInstance().saveSubscription(subscription);
    }

    public BillingClient getBillingClient() {
        return billingClient;
    }

}
