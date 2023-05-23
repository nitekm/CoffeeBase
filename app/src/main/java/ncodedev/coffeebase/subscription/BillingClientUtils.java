package ncodedev.coffeebase.subscription;

import android.util.Log;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;

public class BillingClientUtils {

    private final static String TAG = "BillingClientUtils";


    public static boolean billingResultOK(BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            return true;
        } else {
            Log.d(TAG, "onBillingSetupFinished with code: " + billingResult.getResponseCode() +
                    "\nAnd message: " + billingResult.getDebugMessage());
            return false;
        }
    }
}
