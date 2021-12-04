package com.vivek.feedindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.rom4ek.arcnavigationview.ArcNavigationView;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    //   NavigationView navigationView
    NavigationView navigationView;
    Toolbar tool;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setStatusBarColor(Color.parseColor("#E86D6D"));
        setContentView(R.layout.activity_dash_board);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tool = findViewById(R.id.toolbar);

        menu = navigationView.getMenu();
        //  menu.findItem(R.id.nav_logout).setVisible(false);
        //menu.findItem(R.id.nav_profile).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this,
                drawerLayout,
                tool,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // navigationView.setCheckedItem(R.id.nav_home);





        LinearLayout donate = (LinearLayout) findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, DonateActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout recieve = (LinearLayout) findViewById(R.id.recieve);
        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, RecieveActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          /*/  case R.id.nav_home:
                Intent intent = new Intent(Main_Dashboard.this, Main_Dashboard.class);
                startActivity(intent);
                break;
     */       case R.id.donate:
                Intent semester = new Intent(DashBoard.this, DonateActivity.class);
                startActivity(semester);
                break;
            case R.id.recieve:
                Intent intenet = new Intent(DashBoard.this, RecieveActivity.class);
                startActivity(intenet);
                break;

            case R.id.logout:

                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),FirstActivity.class));
                finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}