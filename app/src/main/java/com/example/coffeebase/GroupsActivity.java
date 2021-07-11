package com.example.coffeebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GroupsActivity extends AppCompatActivity {

    private Button addGroupBtn, myGroupsBtn;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        initViews();

        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(GroupsActivity.this, AddGroup.class);
                startActivity(intent);
            }
        });

        myGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(GroupsActivity.this, MyGroups.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        addGroupBtn = findViewById(R.id.btnAddGroup);
        myGroupsBtn = findViewById(R.id.btnMyGroups);
    }
}
