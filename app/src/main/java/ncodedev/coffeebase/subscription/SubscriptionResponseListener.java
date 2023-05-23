package ncodedev.coffeebase.subscription;

import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;

import java.util.List;

public interface SubscriptionResponseListener {
    void handleProductDetailsList(List<ProductDetails> productDetailsList);

    void handlePurchasesList(List<Purchase> purchases);
}
