package ncodedev.coffeebase.subscription;

import android.app.Activity;
import android.content.Context;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;

import java.util.Collections;

import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class SubscriptionProcessor {

    private final BillingClient billingClient;

    public SubscriptionProcessor(BillingClient billingClient) {
        this.billingClient = billingClient;
    }

    public void processSubscriptionPurchase(Activity context, ProductDetails productDetails, String offerToken) {
        var productDetailsParams = prepareProductDetailsParams(productDetails, offerToken);
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

    private void handleLaunchBillingFlowResult(Activity context, BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            return;
        }

        showToast(context, billingResult.getDebugMessage());
    }
}
