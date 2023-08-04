package ncodedev.coffeebase.subscription;

import android.app.Activity;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails;
import ncodedev.coffeebase.model.domain.Subscription;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;
import ncodedev.coffeebase.web.provider.SubscriptionApiProvider;

import java.time.LocalDateTime;
import java.util.Collections;

import static ncodedev.coffeebase.subscription.BillingClientUtils.billingResultOK;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class SubscriptionPurchase {

    private final BillingClient billingClient;

    public SubscriptionPurchase(BillingClient billingClient) {
        this.billingClient = billingClient;
    }

    public void processSubscriptionPurchase(Activity context, ProductDetails productDetails, SubscriptionOfferDetails offerDetails) {
        var productDetailsParams = prepareProductDetailsParams(productDetails, offerDetails.getOfferToken());
        var billingFlowParams = prepareBillingFlowParams(productDetailsParams);
        var billingResult = billingClient.launchBillingFlow(context, billingFlowParams);
        handleLaunchBillingFlowResult(context, billingResult);
    }

    private ProductDetailsParams prepareProductDetailsParams(ProductDetails productDetails, String offerToken) {
        return ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build();
    }

    private BillingFlowParams prepareBillingFlowParams(ProductDetailsParams productDetailsParams) {
        return BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(Collections.singletonList(productDetailsParams))
                .build();
    }

    private void handleLaunchBillingFlowResult(Activity context,
                                               BillingResult billingResult) {
        if (billingResultOK(billingResult)) {
            return;
        }
        showToast(context, billingResult.getDebugMessage());
    }
}
