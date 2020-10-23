package com.example.boc_mobile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserProfile extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private TextView name,address,contact,email,c1,c2,c3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        navigationView = findViewById(R.id.drawerNavigation);

        name = findViewById(R.id.profile_name_textView);
        address = findViewById(R.id.profile_address_textView);
        contact = findViewById(R.id.profile_contact_textView);
        email = findViewById(R.id.profile_email_textView);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);

        setProfile();
        //for side drawer
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
                    Intent i = new Intent(UserProfile.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    startActivity(new Intent(UserProfile.this, MainActivity.class));
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(UserProfile.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
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

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.logout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(UserProfile.this);

            alert.setTitle("LOGOUT");
            alert.setIcon(R.drawable.ic_warning);
            alert.setMessage("You are about to logout. Please Confirm...");
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

                    Intent i = new Intent(UserProfile.this, Login.class);
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

    public void setProfile(){

        String uname = SaveSharedPreference.getUserName(UserProfile.this);

        switch (uname){

            case "763224512":
                name.setText("Dulini Gunasekara");
                contact.setText("0763224512");
                email.setText("dulinig@yahoo.com");
                address.setText("Negombo road,Narammala");
                c1.setText("2020-05-20 | I Sathsara - 0321 4584 4554 7884 | Rs.5000");
                c2.setText("2020-05-21 | D Dilanjan - 0854123654 | Rs.1500");
                c3.setText("2020-05-22 | P Navodya - 2532644564 | Rs.200");
                break;

            case "769152716":
                name.setText("Isuru Sathsara");
                contact.setText("0769152716");
                email.setText("isuru.s@gmail.com");
                address.setText("Lake Rd, Kurunegala");
                c1.setText("2020-05-20 | D Gunasekara - 0321 4584 4554 7884 | Rs.500");
                c2.setText("2020-05-21 | D Dilanjan - 0854123654 | Rs.1500");
                c3.setText("2020-05-22 | P Navodya - 2532644564 | Rs.200");
                break;

            case "770885712":
                name.setText("Damsiri Dilanjan");
                contact.setText("0770885712");
                email.setText("damsiri.d@gmail.com");
                address.setText("Main road,Kuliyapitiya");
                c1.setText("2020-05-20 | D Gunasekara - 0321 4584 4554 7884 | Rs.500");
                c2.setText("2020-05-21 | I Sathsara - 0854123654 | Rs.1500");
                c3.setText("2020-05-22 | P Navodya - 2532644564 | Rs.200");
                break;

            case "715443619":
                name.setText("Pasindu Navodya");
                contact.setText("0715443619");
                email.setText("pasindu.n@gmail.com");
                address.setText("Main road,Alawwa");
                c1.setText("2020-05-20 | D Gunasekara - 0321 4584 4554 7884 | Rs.500");
                c2.setText("2020-05-21 | I Sathsara - 0854123654 | Rs.1500");
                c3.setText("2020-05-22 | P Navodya - 2532644564 | Rs.200");
                break;


        }

    }
}
