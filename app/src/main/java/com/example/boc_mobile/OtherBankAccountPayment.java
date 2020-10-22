package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class OtherBankAccountPayment extends AppCompatActivity {

    private Button continueBtn;
    private Spinner payto_spinner,payfrom_spinner;
    private EditText amount_editText, desc_editText;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_account_payment);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");

        // Buttons
        continueBtn = findViewById(R.id.tp_cont_btn);

        // input fields
        payto_spinner = findViewById(R.id.oba_payTo_spinner);
        payfrom_spinner = findViewById(R.id.oba_payFrom_spinner);
        amount_editText = findViewById(R.id.oba_amount_editText);
        desc_editText = findViewById(R.id.oba_desc_editText);


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
                    startActivity(new Intent(OtherBankAccountPayment.this, dashboard.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.transaction){
                    startActivity(new Intent(OtherBankAccountPayment.this, MainActivity.class));
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

        // launch confirm
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCnonfirmActivity();
            }
        });

    }

    // launch confirm
    private void launchCnonfirmActivity(){
        Intent intent = new Intent(OtherBankAccountPayment.this, OtherBankAccountConfirm.class);
        startActivity(intent);
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

            AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankAccountPayment.this);

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
                    Intent i = new Intent(OtherBankAccountPayment.this,Login.class);
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

