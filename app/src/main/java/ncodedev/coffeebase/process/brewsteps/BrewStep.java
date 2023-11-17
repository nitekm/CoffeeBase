package ncodedev.coffeebase.process.brewsteps;

public interface BrewStep {
    void setupStep();
    boolean validate();
    void executeFinishStep(Object... brewData);
}
