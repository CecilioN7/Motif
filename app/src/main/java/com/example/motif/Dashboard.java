package com.example.motif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @SuppressLint({"MissingInflatedId", "RestrictedApi", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R. id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_main_menu) {
                Log.i("MENU DRAWER TAG", "Home is clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (itemId == R.id.nav_settings) {
                Log.i("MENU DRAWER TAG", "Settings is clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (itemId == R.id.nav_login) {
                Log.i("MENU DRAWER TAG", "login is clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (itemId == R.id.nav_transposition) {
                Log.i("MENU DRAWER TAG", "transpose is clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });
    }
}