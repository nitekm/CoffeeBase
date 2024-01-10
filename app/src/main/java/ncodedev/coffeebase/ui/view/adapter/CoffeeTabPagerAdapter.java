package ncodedev.coffeebase.ui.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CoffeeTabPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> coffeeTabFragments = new ArrayList<>();
    private final List<String> coffeeTabTitles = new ArrayList<>();

    public CoffeeTabPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addTabFragment(Fragment fragment, String title) {
        coffeeTabFragments.add(fragment);
        coffeeTabTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return coffeeTabFragments.get(position);
    }

    @Override
    public int getCount() {
        return coffeeTabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return coffeeTabTitles.get(position);
    }
}
