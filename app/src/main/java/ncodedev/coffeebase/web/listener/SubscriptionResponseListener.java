package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Subscription;

public interface SubscriptionResponseListener {

    void handleSaveSubscription(Subscription subscription);
}
