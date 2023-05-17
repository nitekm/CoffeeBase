package ncodedev.coffeebase.subscription;

import com.android.billingclient.api.ProductDetails;

import java.util.List;

public interface SubscriptionResponseListener {
    void handleProductDetailsList(List<ProductDetails> productDetailsList);
}
