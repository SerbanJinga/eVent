package com.roh44x.eVent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    BottomSheetBehavior bottomSheetBehavior;
    private BottomAppBar bottomBar;
    private BottomSheetDialog bottomSheetDialog;
    private FloatingActionButton floatingActionButton;
    private LinearLayout shareLinearLayout, uploadLinearLayout, copyLinearLayout;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        floatingActionButton = findViewById(R.id.fab);
        bottomBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomBar);

        loadFragment(new HomeFragment());

        bottomBar.setOnMenuItemClickListener(this);
//
//        Query query = mDatabase.orderByChild("title").startAt("[a-zA-Z0-9]*")
//              .endAt(search.getText().toString());






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.botton_app_home:
                fragment = new HomeFragment();
                break;
            case R.id.bottom_app_add:
                startActivity(new Intent(this, AddActivity.class));
                break;


        }

        return loadFragment(fragment);
    }

}
