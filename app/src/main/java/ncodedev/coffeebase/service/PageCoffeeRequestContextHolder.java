package ncodedev.coffeebase.service;

import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.Page;

import java.util.Map;
import java.util.Set;

public class PageCoffeeRequestContextHolder {
    private RequestContext currentRequestContext;
    private final String currentSortProperty;
    private final String currentSortDirection;
    private boolean currentlyLoadingData;
    private boolean lastPage;
    private int pageNumber;
    private Map<String, Set<String>> filters;

    private static PageCoffeeRequestContextHolder instance;

    private PageCoffeeRequestContextHolder(String currentSortProperty,
                                           String currentSortDirection,
                                           Map<String, Set<String>> filters) {
        this.currentlyLoadingData = false;
        this.lastPage = false;
        this.pageNumber = 0;
        this.currentSortProperty = currentSortProperty;
        this.currentSortDirection = currentSortDirection;
        this.filters = filters;
    }

    public static PageCoffeeRequestContextHolder updateInstance(String currentSortProperty,
                                                                String currentSortDirection,
                                                                Map<String, Set<String>> filters) {
        instance = new PageCoffeeRequestContextHolder(currentSortProperty, currentSortDirection, filters);
        return instance;
    }

    public static PageCoffeeRequestContextHolder getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PageCoffeeRequestHolder has not been initialized");
        }
        return instance;
    }

    public void updateContextOnNewPage(Page page, RequestContext requestContext) {
        this.currentRequestContext = requestContext;
        this.lastPage = page.isLast();
        this.pageNumber = page.getNumber();
        this.currentlyLoadingData = false;
    }

    public void clearRequestContext() {
        this.currentRequestContext = null;
    }

    public boolean canFetchMoreData() {
        return !lastPage && !currentlyLoadingData;
    }

    public int getNextPageNumber() {
        return this.pageNumber + 1;
    }

    public void setCurrentlyLoadingData(boolean currentlyLoadingData) {
        this.currentlyLoadingData = currentlyLoadingData;
    }

    public String getCurrentSortProperty() {
        return currentSortProperty;
    }

    public String getCurrentSortDirection() {
        return currentSortDirection;
    }

    public RequestContext getCurrentRequestContext() {
        return this.currentRequestContext;
    }

    public Map<String, Set<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Set<String>> filters) {
        this.filters = filters;
    }

}
