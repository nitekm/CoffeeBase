package ncodedev.coffeebase.ui.utility;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.provider.CoffeeApiProvider;

public class MainActivityTopBarHandler {
    private final static String TAG = "MainActivityTopBarHandler";
    private AppCompatActivity appCompatActivity;
    private Menu menu;
    private final CoffeeApiProvider coffeeApiProvider = CoffeeApiProvider.getInstance();
    private final CoffeeListResponseListener listener;

    public MainActivityTopBarHandler(AppCompatActivity appCompatActivity, Menu menu, CoffeeListResponseListener listener) {
        this.appCompatActivity = appCompatActivity;
        this.menu = menu;
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

    private void setUpSearch() {

    }
}
