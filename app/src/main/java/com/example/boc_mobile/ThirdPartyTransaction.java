package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;


public class ThirdPartyTransaction extends AppCompatActivity {

    private Button continueBtn;
    private EditText amount,desc;
    private Spinner payTo,payFrom;



    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_account_payment);

        // setting up toolbar
        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");


        // buttons
        continueBtn = findViewById(R.id.tp_cont_btn);

        // form fields
        payTo = findViewById(R.id.tp_payTo_spinner);
        payFrom = findViewById(R.id.tp_payFrom_spinner);
        amount = findViewById(R.id.tp_amount_editText);
        desc = findViewById(R.id.tp_desc_editText);



        navigationView = findViewById(R.id.drawerNavigation);

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
                    startActivity(new Intent(ThirdPartyTransaction.this, dashboard.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.transaction){
                    startActivity(new Intent(ThirdPartyTransaction.this, MainActivity.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.profile){
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.more){
                    //startActivity(new Intent(MainActivity.this, more.class));
                    drawer.closeDrawers();
                }
                return true;
            }
        });

        // continue button click
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(amount.getText())){
                    amount.setError( "Amount field is required!" );
                }
                else {
                    //intent
                    Intent intent = new Intent(getBaseContext(), confirmThirdPatyTransaction.class);
                    startActivity(intent);
                }
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

            AlertDialog.Builder alert = new AlertDialog.Builder(ThirdPartyTransaction.this);

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
                    Intent i = new Intent(ThirdPartyTransaction.this, Login.class);
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
