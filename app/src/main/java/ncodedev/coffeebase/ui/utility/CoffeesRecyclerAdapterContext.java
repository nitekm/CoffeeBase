package ncodedev.coffeebase.ui.utility;

import ncodedev.coffeebase.model.enums.ContextName;
import ncodedev.coffeebase.ui.view.adapter.CoffeeRecyclerViewAdapter;

public class CoffeesRecyclerAdapterContext {
    private CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter;
    private int currentPage = 0;
    private boolean isLastPage = false;
    private static ContextName contextName;

    private CoffeesRecyclerAdapterContext(ContextName contextName) {
        CoffeesRecyclerAdapterContext.contextName = contextName;
    }

    //TODO: do wyrzucenia. W Interfejsie listReposnelistener dodać metody handleSort handle get all itd.
    // Do wywołania api przekazywac context i na bazie jego wywoływać odpowiednia metode do zhandlowania z interfejsu np. handle sort jak to sort
    // W main activity musi być curtrentocntext i sprawedzanie czy to pierszy strzal czy nastepny jak pierwszy to czyscimy liste
    // i zapelniamy wyuikami jak anstepny to dodajemy wyniki do obencyh
    // dodatkowo mozna to rozsz4erzy co search po wyrzuceniu getAlla
    public static CoffeesRecyclerAdapterContext getCoffeesRecyclerAdapterContext(ContextName contextName) {
        if (CoffeesRecyclerAdapterContext.contextName == null || CoffeesRecyclerAdapterContext.contextName != contextName) {
            return new CoffeesRecyclerAdapterContext(contextName);
        }
        return this;
    }

    public CoffeeRecyclerViewAdapter getCoffeeRecyclerViewAdapter() {
        return coffeeRecyclerViewAdapter;
    }

    public void setCoffeeRecyclerViewAdapter(CoffeeRecyclerViewAdapter coffeeRecyclerViewAdapter) {
        this.coffeeRecyclerViewAdapter = coffeeRecyclerViewAdapter;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }
}
