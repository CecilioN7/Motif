package com.example.motif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawerLayout);
        materialToolbar = findViewById(R.id.materialToolbar);
        frameLayout = findViewById(R.id.frameLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Dashboard.this, drawerLayout, materialToolbar, R.string.drawer_close, R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId()==R.id.share){
                    Toast.makeText(Dashboard.this, "share", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.home){
                    Toast.makeText(Dashboard.this, "Home", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuItem.getItemId()==R.id.profile) {
                    Intent intent = new Intent(Dashboard.this, Login.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(Dashboard.this, "Logout", Toast.LENGTH_SHORT).show();
                    //drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.transpose) {
                    Intent intent = new Intent(Dashboard.this, TransposeActivity.class);
                    startActivity(intent);
                    //Toast.makeText(Dashboard.this, "Transpose", Toast.LENGTH_SHORT).show();
                    //drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuItem.getItemId()==R.id.settings) {
                    Intent intent = new Intent(Dashboard.this, Settings.class);
                    intent.putExtra("user", getIntent().getStringExtra("user"));

                    startActivity(intent);
                    //Toast.makeText(Dashboard.this, "Settings", Toast.LENGTH_SHORT).show();
                    //drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }
}


