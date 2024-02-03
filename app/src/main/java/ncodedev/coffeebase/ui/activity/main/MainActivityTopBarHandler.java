package ncodedev.coffeebase.ui.activity.main;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.PageCoffeeRequest;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;

public class MainActivityTopBarHandler {
    private final static String TAG = "MainActivityTopBarHandler";
    private final AppCompatActivity appCompatActivity;
    private Menu menu;
    private MenuItem menuItem;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();
    private final CoffeeListResponseListener listener;

    public MainActivityTopBarHandler(AppCompatActivity appCompatActivity, Menu menu, CoffeeListResponseListener listener) {
        this.appCompatActivity = appCompatActivity;
        this.menu = menu;
        this.listener = listener;
    }

    public MainActivityTopBarHandler(AppCompatActivity appCompatActivity, MenuItem menuItem, CoffeeListResponseListener listener) {
        this.appCompatActivity = appCompatActivity;
        this.menuItem = menuItem;
        this.listener = listener;
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

    public boolean setUpTopAppBarSort() {
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
            return true;
        }
        return false;
    }

    private void assignSortApiCallsToMenuItem(MenuItem menuItem) {
        final String name = "name";
        final String roaster = "roaster";
        final String farm = "farm";
        final String cropHeight = "cropHeight";
        final String rating = "rating";
        final String scaRating = "scaRating";
        final String ascending = "ASC";
        final String descending = "DESC";

        var itemId = menuItem.getItemId();
        if (itemId == R.id.sort_by_name_desc) {
            clearContextAndCallEndpoint(name, descending);
            return;
        }
        if (itemId == R.id.sort_by_name_asc) {
            clearContextAndCallEndpoint(name, ascending);
            return;
        }
        if (itemId == R.id.sort_by_roaster_desc) {
            clearContextAndCallEndpoint(roaster,descending);
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
        }if (itemId == R.id.sort_by_crop_height_desc) {
            clearContextAndCallEndpoint(cropHeight, descending);
            return;
        }
        if (itemId == R.id.sort_by_crop_height_asc) {
            clearContextAndCallEndpoint(cropHeight, ascending);
            return;
        }if (itemId == R.id.sort_by_rating_asc) {
            clearContextAndCallEndpoint(rating, ascending);
            return;
        }
        if (itemId == R.id.sort_by_rating_desc) {
            clearContextAndCallEndpoint(rating, descending);
            return;
        }if (itemId == R.id.sort_by_sca_rating_desc) {
            clearContextAndCallEndpoint(scaRating, descending);
            return;
        }
        if (itemId == R.id.sort_by_sca_rating_asc) {
            clearContextAndCallEndpoint(scaRating, ascending);
            return;
        }
    }

    private void clearContextAndCallEndpoint(String sortProperty, String sortDirection) {
        final var mainActivity = (MainActivity) appCompatActivity;
        mainActivity.clearRequestContext();
        coffeeApiProvider.getAllPaged(listener, new PageCoffeeRequest(sortProperty, sortDirection), RequestContext.SORT);
    }
}
