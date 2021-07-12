package coffeebase.app.activity.groups;

import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coffeebase.app.activity.R;
import coffeebase.app.api.GroupApi;
import coffeebase.app.model.CoffeeGroup;

import java.util.ArrayList;
import java.util.List;

public class MyGroups extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GridLayoutManager gridLayoutManager;
    private GroupApi groupApi;
    private GroupRecViewAdapter adapter;
    private RecyclerView groupRecView;
    private List<CoffeeGroup> groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
    }


}