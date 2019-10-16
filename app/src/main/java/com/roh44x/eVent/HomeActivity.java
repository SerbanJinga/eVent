package com.roh44x.eVent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private BottomAppBar bottomBar;
    private BottomSheetDialog bottomSheetDialog;
    private FloatingActionButton floatingActionButton;
    private LinearLayout shareLinearLayout, uploadLinearLayout, copyLinearLayout;
    private View bottomSheet;
    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        floatingActionButton = findViewById(R.id.fab);
        bottomBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomBar);

        loadFragment(new HomeFragment());

        bottomBar.setOnMenuItemClickListener(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





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
                fragment = new AddFragment();
                break;

        }

        return loadFragment(fragment);
    }

}
