package coffeebase.app.activity.groups;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coffeebase.app.activity.BuildConfig;
import coffeebase.app.activity.MainActivity;
import coffeebase.app.activity.R;
import coffeebase.app.api.GroupApi;
import coffeebase.app.model.CoffeeGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MyGroups extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;
    private GroupApi groupApi;
    private GroupRecViewAdapter adapter;
    private RecyclerView groupRecView;
    private ArrayList<CoffeeGroup> groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        initViews();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        groupApi = retrofit.create(GroupApi.class);

        Call<List<CoffeeGroup>> call = groupApi.getGroups();

        call.enqueue(new Callback<List<CoffeeGroup>>() {
            @Override
            public void onResponse(final Call<List<CoffeeGroup>> call, final Response<List<CoffeeGroup>> response) {
                System.out.println("" + response.body());
                if (!response.isSuccessful()) {
                    Toast.makeText(MyGroups.this, "Code " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                groups = new ArrayList<>(response.body());
                adapter = new GroupRecViewAdapter(MyGroups.this, groups);
                groupRecView.setAdapter(adapter);
            }

            @Override
            public void onFailure(final Call<List<CoffeeGroup>> call, final Throwable t) {
                Toast.makeText(MyGroups.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        groupRecView = findViewById(R.id.groupRecView);
        gridLayoutManager= new GridLayoutManager(this, 2);
        groupRecView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}