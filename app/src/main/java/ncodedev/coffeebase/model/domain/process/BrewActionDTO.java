package ncodedev.coffeebase.model.domain.process;

public class BrewActionDTO {
    private BrewActionType brewActionType;
    private Long coffeeId;
    private Long brewId;

    public BrewActionDTO(BrewActionType brewActionType, Long coffeeId, Long brewId) {
        this.brewActionType = brewActionType;
        this.coffeeId = coffeeId;
        this.brewId = brewId;
    }
}

