package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class confirmThirdPatyTransaction extends AppCompatActivity {

    private Button proceedBtn;
    private TextView payfrom_view,payto_view,amount_view,desc_view;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_account_payment);

        // setting up toolbar
        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        //getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");

        // buttons
        proceedBtn = findViewById(R.id.tp_prcd_btn);

        // text fields
        payto_view = findViewById(R.id.tp_payto_confirm);
        payfrom_view = findViewById(R.id.tp_payfrom_confirm);
        amount_view = findViewById(R.id.tp_amount_confirm);
        desc_view = findViewById(R.id.tp_desc_confirm);

        // navigation view
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
                    startActivity(new Intent(confirmThirdPatyTransaction.this, dashboard.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.transaction){
                    startActivity(new Intent(confirmThirdPatyTransaction.this, MainActivity.class));
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

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(confirmThirdPatyTransaction.this);

                alert.setTitle("SUCCESSFUL");
                alert.setIcon(R.drawable.transaction_okay);
                alert.setMessage("The amount is transferred successfully");
                alert.setPositiveButton("DONE", null);
                alert.setNegativeButton("Another Transaction", null);

                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_design);

                // this will change the default behaviour of buttons
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchDashboard();
                    }
                });

                // this will change the default behaviour of buttons
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchOtherTransaction();
                    }
                });
            }
        });

        /*
        cancel = findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelIntent();
            }
        });*/
    }

   /* private void confirmIntent(){
        Intent intent = new Intent(this, SuccessMsgThirdPartyTransaction.class);
        startActivity(intent);
    } */

    private void launchDashboard(){
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }

    // intent for launch other transaction
    private void launchOtherTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
    private void cancelIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    */

    // set logout icon in app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // set logout function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.logout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(confirmThirdPatyTransaction.this);

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
                    Intent i = new Intent(confirmThirdPatyTransaction.this,Login.class);
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
