package ncodedev.coffeebase.service;

import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.Page;

import java.util.List;
import java.util.Map;

public class PageCoffeeRequestContextHolder {
    private RequestContext currentRequestContext;
    private String currentSortProperty;
    private String currentSortDirection;
    private boolean currentlyLoadingData;
    private boolean lastPage;
    private int pageNumber;
    private Map<String, List<String>> filters;

    private static PageCoffeeRequestContextHolder instance;

    private PageCoffeeRequestContextHolder(String currentSortProperty,
                                           String currentSortDirection,
                                           Map<String, List<String>> filters) {
        this.currentlyLoadingData = false;
        this.lastPage = false;
        this.pageNumber = 0;
        this.currentSortProperty = currentSortProperty;
        this.currentSortDirection = currentSortDirection;
        this.filters = filters;
    }

    public static PageCoffeeRequestContextHolder updateInstance(String currentSortProperty,
                                                                String currentSortDirection,
                                                                Map<String, List<String>> filters) {
        instance = new PageCoffeeRequestContextHolder(currentSortProperty, currentSortDirection, filters);
        return instance;
    }

    public static PageCoffeeRequestContextHolder getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PageCoffeeRequestHolder has not been initialized");
        }
        return instance;
    }

//    private PageCoffeeRequestContextHolder(RequestContext currentRequestContext) {
//        this.currentRequestContext = currentRequestContext;
//        this.currentlyLoadingData = false;
//        this.lastPage = false;
//        pageNumber = 0;
//    }
//    private PageCoffeeRequestContextHolder(RequestContext currentRequestContext, String currentSortProperty, String currentSortDirection) {
//        this.currentRequestContext = currentRequestContext;
//        this.currentSortProperty = currentSortProperty;
//        this.currentSortDirection =currentSortDirection;
//        this.currentlyLoadingData = false;
//        this.lastPage = false;
//        pageNumber = 0;
//    }
//
//    public static PageCoffeeRequestContextHolder createNewStandardRequestContext(RequestContext requestContext) {
//        return new PageCoffeeRequestContextHolder(requestContext);
//    }
//    public static PageCoffeeRequestContextHolder createNewSortRequestContext(String currentSortProperty,
//                                                                             String currentSortDirection) {
//        return new PageCoffeeRequestContextHolder(RequestContext.SORT, currentSortProperty, currentSortDirection);
//    }

    public void updateContextOnNewPage(Page page, RequestContext requestContext) {
        this.currentRequestContext = requestContext;
        this.lastPage = page.isLast();
        this.pageNumber = page.getNumber();
        this.currentlyLoadingData = false;
    }

    public void setCurrentRequestContext(RequestContext requestContext) {
        this.currentRequestContext = requestContext;
    }

    public static void clearCurrentRequestContextHolder() {
        instance = null;
    }

    public void clearRequestContext() {
        this.currentRequestContext = null;
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
        return currentSortProperty;
    }

    public void setCurrentSortProperty(String currentSortProperty) {
        this.currentSortProperty = currentSortProperty;
    }

    public String getCurrentSortDirection() {
        return currentSortDirection;
    }

    public void setCurrentSortDirection(String currentSortDirection) {
        this.currentSortDirection = currentSortDirection;
    }

    public RequestContext getCurrentRequestContext() {
        return this.currentRequestContext;
    }

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }
}
