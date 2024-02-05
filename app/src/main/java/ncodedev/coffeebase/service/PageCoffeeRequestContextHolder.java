package ncodedev.coffeebase.service;

import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.Page;

public class PageCoffeeRequestContextHolder {
    private RequestContext currentRequestContext;
    private String currentSortProperty;
    private String currentSortDirection;
    private boolean currentlyLoadingData;
    private boolean lastPage;
    private int pageNumber;

    private PageCoffeeRequestContextHolder(RequestContext currentRequestContext) {
        this.currentRequestContext = currentRequestContext;
        this.currentlyLoadingData = false;
        this.lastPage = false;
        pageNumber = 0;
    }
    private PageCoffeeRequestContextHolder(RequestContext currentRequestContext, String currentSortProperty, String currentSortDirection) {
        this.currentRequestContext = currentRequestContext;
        this.currentSortProperty = currentSortProperty;
        this.currentSortDirection =currentSortDirection;
        this.currentlyLoadingData = false;
        this.lastPage = false;
        pageNumber = 0;
    }

    public static PageCoffeeRequestContextHolder createNewStandardRequestContext(RequestContext requestContext) {
        return new PageCoffeeRequestContextHolder(requestContext);
    }
    public static PageCoffeeRequestContextHolder createNewSortRequestContext(String currentSortProperty,
                                                                             String currentSortDirection) {
        return new PageCoffeeRequestContextHolder(RequestContext.SORT, currentSortProperty, currentSortDirection);
    }

    public void updateContextOnNewPage(Page page) {
        this.lastPage = page.isLast();
        this.pageNumber = page.getNumber();
        this.currentlyLoadingData = false;
    }

    public boolean canFetchMoreData() {
        return !lastPage && !currentlyLoadingData;
    }

    public int getCurrentPageNumber() {
        return this.pageNumber;
    }

    public int getNextPageNumber() {
        return this.pageNumber + 1;
    }


    public void setCurrentlyLoadingData(boolean currentlyLoadingData) {
        this.currentlyLoadingData = currentlyLoadingData;
    }

    public String getCurrentSortProperty() {
        return this.currentSortProperty;
    }

    public String getCurrentSortDirection() {
        return this.currentSortDirection;
    }
    public RequestContext getCurrentRequestContext() {
        return this.currentRequestContext;
    }
}
