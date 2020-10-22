package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class moreActivityFunction extends AppCompatActivity {

    private Button logout_btn,offers_btn,help_btn,contact_btn;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_function);
        getSupportActionBar().setTitle("BOC Mobile Banking - More");

        // Buttons
        logout_btn = findViewById(R.id.logout_btn);
        offers_btn = findViewById(R.id.offers_btn);
        help_btn = findViewById(R.id.help_btn);
        contact_btn = findViewById(R.id.help_btn);

        // navigation components
        navigationView = findViewById(R.id.drawerNavigation);
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.dashboard){
                    Intent i = new Intent(moreActivityFunction.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    Intent i = new Intent(moreActivityFunction.this,MainActivity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                if(id == R.id.profile){
                    Toast.makeText(moreActivityFunction.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.more){

                    drawer.closeDrawers();
                }
                return true;

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutIntent();
            }
        });

    }

    private void LogoutIntent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(moreActivityFunction.this);

        alert.setTitle("Logout");
        alert.setIcon(R.drawable.ic_warning);
        alert.setMessage("You are about to logout. Please confirm");
        alert.setPositiveButton("Logout", null);
        alert.setNegativeButton("Cancel", null);

        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_design);


        // this will change the default behaviour of buttons
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // redirect to dashboard
                Intent i = new Intent(moreActivityFunction.this,Login.class);
                //i.putExtra("uname",uname);
                startActivity(i);
                finish();
            }
        });
    }


    // set logout icon in app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // set logout function
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.logout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(moreActivityFunction.this);

            alert.setTitle("Logout");
            alert.setIcon(R.drawable.ic_warning);
            alert.setMessage("You are about to logout. Please confirm");
            alert.setPositiveButton("Logout", null);
            alert.setNegativeButton("Cancel", null);

            AlertDialog dialog = alert.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_design);


            // this will change the default behaviour of buttons
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // redirect to dashboard
                    Intent i = new Intent(moreActivityFunction.this,Login.class);
                    //i.putExtra("uname",uname);
                    startActivity(i);
                    finish();
                }
            });

        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
