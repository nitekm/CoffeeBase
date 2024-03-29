package ncodedev.coffeebase.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription {
    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("purchaseToken")
    @Expose
    private String purchaseToken;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("purchasedDate")
    @Expose
    private String purchasedDate;

    public Subscription(String productId, String purchaseToken, boolean active, String purchasedDate) {
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.active = active;
        this.purchasedDate = purchasedDate;
    }

    public String getProductId() {
        return productId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public boolean isActive() {
        return active;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

}


