package ncodedev.coffeebase.service;

import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.Page;

public class PageCoffeeRequestContextHolder {
    private RequestContext requestContext;
    private String currentSortProperty;
    private String currentSortDirection;
    private boolean currentlyLoadingData;
    private boolean lastPage;
    private int pageNumber;

    private PageCoffeeRequestContextHolder(RequestContext requestContext) {
        this.requestContext = requestContext;
        this.currentlyLoadingData = false;
        this.lastPage = false;
        pageNumber = 0;
    }
    private PageCoffeeRequestContextHolder(RequestContext requestContext, String currentSortProperty, String currentSortDirection) {
        this.requestContext = requestContext;
        this.currentSortProperty = currentSortProperty;
        this.currentSortDirection =currentSortDirection;
        this.currentlyLoadingData = false;
        this.lastPage = false;
        pageNumber = 0;
    }

    public static PageCoffeeRequestContextHolder createNewStandardRequestContext(RequestContext requestContext) {
        return new PageCoffeeRequestContextHolder(requestContext);
    }
    public static PageCoffeeRequestContextHolder createNewSortRequestContext(RequestContext requestContext,
                                                                  String currentSortProperty,
                                                                  String currentSortDirection) {
        return new PageCoffeeRequestContextHolder(requestContext, currentSortProperty, currentSortDirection);
    }

    public void updateContextOnNewPage(Page page) {
        this.lastPage = page.isLast();
        this.pageNumber = page.getNumber();
        this.currentlyLoadingData = false;
    }

    public int getCurrentPageNumber() {
        return this.pageNumber;
    }

    public int getNextPageNumber() {
        return this.pageNumber + 1;
    }

    public boolean isCurrentlyLoadingData() {
        return currentlyLoadingData;
    }

    public void setCurrentlyLoadingData(boolean currentlyLoadingData) {
        this.currentlyLoadingData = currentlyLoadingData;
    }
}
