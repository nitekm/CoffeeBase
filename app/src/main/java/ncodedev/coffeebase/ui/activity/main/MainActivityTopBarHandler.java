package ncodedev.coffeebase.ui.activity.main;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.PageCoffeeRequest;
import ncodedev.coffeebase.service.PageCoffeeRequestContextHolder;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivityTopBarHandler {
    private final static String TAG = "MainActivityTopBarHandler";
    private final AppCompatActivity appCompatActivity;
    private Menu menu;
    private MenuItem menuItem;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();
    private final CoffeeListResponseListener listener;
    private Map<String, List<String>> currentFilters;

    public MainActivityTopBarHandler(AppCompatActivity appCompatActivity, Menu menu, CoffeeListResponseListener listener) {
        this.appCompatActivity = appCompatActivity;
        this.menu = menu;
        this.listener = listener;
    }

    public MainActivityTopBarHandler(AppCompatActivity appCompatActivity, MenuItem menuItem, CoffeeListResponseListener listener, Map<String, List<String>> currentFilters) {
        this.appCompatActivity = appCompatActivity;
        this.menuItem = menuItem;
        this.listener = listener;
        this.currentFilters = currentFilters;
    }

    public boolean setUpTopAppBarSearch() {
        MenuInflater inflater = appCompatActivity.getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_my_coffeebase, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem menuItem) {
                Log.d(TAG, "onMenuItemActionExpand: Search mode activated");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem menuItem) {
                Log.d(TAG, "onMenuItemActionCollapse: Search mode deactivated");
                return true;
            }
        };
        menu.findItem(R.id.searchMenuItem).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchMenuItem).getActionView();
        searchView.setQueryHint(appCompatActivity.getString(R.string.search_by_anything));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String searchBy) {
                coffeeApiProvider.search(searchBy, listener);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String searchBy) {
                new Handler(Looper.getMainLooper())
                        .postDelayed(() -> coffeeApiProvider.search(searchBy, listener), 2000);
                return true;
            }
        });
        return true;
    }

    public void setUpTopAppBarSort() {
        if (menuItem.getItemId() == R.id.sortMenuItem) {
            var sortMenu = new PopupMenu(appCompatActivity, appCompatActivity.findViewById(menuItem.getItemId()));
            var menuInflater = sortMenu.getMenuInflater();
            menuInflater.inflate(R.menu.top_app_bar_sort_menu, sortMenu.getMenu());
            assignSortApiCallsToMenuItem(menuItem);
            sortMenu.show();
            sortMenu.setOnMenuItemClickListener(item -> {
                assignSortApiCallsToMenuItem(item);
                return true;
            });
        }
    }

    private void assignSortApiCallsToMenuItem(MenuItem menuItem) {
        final String id = "id";
        final String name = "name";
        final String roaster = "roaster";
        final String farm = "farm";
        final String cropHeight = "cropHeight";
        final String rating = "rating";
        final String scaRating = "scaRating";
        final String ascending = "ASC";
        final String descending = "DESC";

        var itemId = menuItem.getItemId();
        if (itemId == R.id.sort_default) {
            clearContextAndCallEndpoint(id, ascending);
            return;
        }
        if (itemId == R.id.sort_by_name_desc) {
            clearContextAndCallEndpoint(name, descending);
            return;
        }
        if (itemId == R.id.sort_by_name_asc) {
            clearContextAndCallEndpoint(name, ascending);
            return;
        }
        if (itemId == R.id.sort_by_roaster_desc) {
            clearContextAndCallEndpoint(roaster, descending);
            return;
        }
        if (itemId == R.id.sort_by_roaster_asc) {
            clearContextAndCallEndpoint(roaster, ascending);
            return;
        }
        if (itemId == R.id.sort_by_farm_desc) {
            clearContextAndCallEndpoint(farm, descending);
            return;
        }
        if (itemId == R.id.sort_by_farm_asc) {
            clearContextAndCallEndpoint(farm, ascending);
            return;
        }
        if (itemId == R.id.sort_by_crop_height_desc) {
            clearContextAndCallEndpoint(cropHeight, descending);
            return;
        }
        if (itemId == R.id.sort_by_crop_height_asc) {
            clearContextAndCallEndpoint(cropHeight, ascending);
            return;
        }
        if (itemId == R.id.sort_by_rating_asc) {
            clearContextAndCallEndpoint(rating, ascending);
            return;
        }
        if (itemId == R.id.sort_by_rating_desc) {
            clearContextAndCallEndpoint(rating, descending);
            return;
        }
        if (itemId == R.id.sort_by_sca_rating_desc) {
            clearContextAndCallEndpoint(scaRating, descending);
            return;
        }
        if (itemId == R.id.sort_by_sca_rating_asc) {
            clearContextAndCallEndpoint(scaRating, ascending);
            return;
        }
    }

    private void clearContextAndCallEndpoint(String sortProperty, String sortDirection) {
        PageCoffeeRequestContextHolder.getInstance().clearRequestContext();
        final var mainActivity = (MainActivity) appCompatActivity;
        mainActivity.clearContext(sortProperty, sortDirection);
        coffeeApiProvider.getAllPaged(listener, new PageCoffeeRequest.Builder()
                        .withSortProperty(sortProperty)
                        .withSortDirection(sortDirection)
                        .withFilters(currentFilters)
                        .build(),
                RequestContext.SORT);
    }

    public void setUpTopBarFilter() {
        if (menuItem.getItemId() == R.id.filterMenuItem) {
            LayoutInflater inflater = (LayoutInflater) appCompatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.top_app_bar_filter_menu, null);

            PopupWindow popupWindow = new PopupWindow(popupView, WRAP_CONTENT, WRAP_CONTENT, true);
            View anchorView = appCompatActivity.findViewById(R.id.topAppBarCoffeeActivity);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.END);

            setUpActiveFilters(popupView);
            setUpApplyFilters(popupView, popupWindow);
        }
    }

    private void setUpActiveFilters(View filterView) {
        LinearLayout favoriteLayout = filterView.findViewById(R.id.favouriteLayout);
        LinearLayout continentLayout = filterView.findViewById(R.id.continentLayout);
        LinearLayout roastProfileLayout = filterView.findViewById(R.id.roastProfileLayout);

        checkActiveFiltersCheckboxes(favoriteLayout, "favourite", currentFilters);
        checkActiveFiltersCheckboxes(continentLayout, "continent", currentFilters);
        checkActiveFiltersCheckboxes(roastProfileLayout, "roastProfile", currentFilters);
    }

    private void checkActiveFiltersCheckboxes(LinearLayout layout, String key, Map<String, List<String>> currentFilters) {
        currentFilters.getOrDefault(key, new ArrayList<>()).forEach(value -> {
            Optional<CheckBox> checkBox = Optional.ofNullable(layout.findViewWithTag(value));
            checkBox.ifPresent(activeFilter -> activeFilter.setChecked(true));
        });
    }

    private void setUpApplyFilters(View filterView, PopupWindow filterWindow) {
        Button applyFiltersButton = filterView.findViewById(R.id.applyFiltersBtn);
        applyFiltersButton.setOnClickListener(view -> {
            createAndSendFilterRequest(getCheckedItems(filterView));
            filterWindow.dismiss();
        });
    }

    private Map<String, List<String>> getCheckedItems(View filterView) {
        LinearLayout favoriteLayout = filterView.findViewById(R.id.favouriteLayout);
        LinearLayout continentLayout = filterView.findViewById(R.id.continentLayout);
        LinearLayout roastProfileLayout = filterView.findViewById(R.id.roastProfileLayout);

        getCheckedItemsForLayout(favoriteLayout, "favourite", currentFilters);
        getCheckedItemsForLayout(continentLayout, "continent", currentFilters);
        getCheckedItemsForLayout(roastProfileLayout, "roastProfile", currentFilters);

        final var mainActivity = (MainActivity) appCompatActivity;
        mainActivity.setCurrentFilters(currentFilters);
        return currentFilters;
    }

    private void getCheckedItemsForLayout(LinearLayout layout, String key, Map<String, List<String>> filtersMap) {
        for (int childPosition = 0; childPosition < layout.getChildCount(); childPosition++) {
            if (layout.getChildAt(childPosition) instanceof CheckBox checkBox) {
                List<String> values = filtersMap.getOrDefault(key, new ArrayList<>());
                if (checkBox.isChecked()) {
                    values.add(checkBox.getTag().toString());
                    filtersMap.put(key, values);
                } else {
                    values.remove(checkBox.getTag().toString());
                }
            }
        }
    }

    private void createAndSendFilterRequest(Map<String, List<String>> checkedFilters) {
        PageCoffeeRequestContextHolder.getInstance().clearRequestContext();
        var pageCoffeeRequest = new PageCoffeeRequest.Builder()
                .withFilters(checkedFilters)
                .build();
        coffeeApiProvider.getAllPaged(listener, pageCoffeeRequest, RequestContext.FILTER);
    }
}
