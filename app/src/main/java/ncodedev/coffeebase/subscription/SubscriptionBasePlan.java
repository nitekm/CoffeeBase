package ncodedev.coffeebase.subscription;

public enum SubscriptionBasePlan {
    MONTHLY_1_99("coffeebase-subscription"),
    MONTHLY_4_99("coffeebase-subscription2");

    private final String value;

    SubscriptionBasePlan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
