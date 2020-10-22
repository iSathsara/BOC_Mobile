package com.example.boc_mobile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class OtherBankAccountConfirm extends AppCompatActivity {

    private Button proceedBtn;
    private TextView payto_textView,payfrom_textView,amount_textView, desc_textView;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_other_bank_account_transaction);

        // Buttons
        proceedBtn = findViewById(R.id.oba_proceedBtn);

        // show details fields
        payto_textView = findViewById(R.id.oba_payto_confirm);
        payfrom_textView = findViewById(R.id.oba_payfrom_confirm);
        amount_textView = findViewById(R.id.oba_amount_confirm);
        desc_textView = findViewById(R.id.oba_desc_confirm);

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
                    Intent i = new Intent(OtherBankAccountConfirm.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    Intent i = new Intent(OtherBankAccountConfirm.this,MainActivity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                if(id == R.id.profile){
                    Toast.makeText(OtherBankAccountConfirm.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                }

                if(id == R.id.more){
                    startActivity(new Intent(OtherBankAccountConfirm.this, moreActivityFunction.class));
                    drawer.closeDrawers();
                }
                return true;

            }
        });


        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // show alert after clicking confirm.
                AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankAccountConfirm.this);

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

                        // redirect to dashboard
                        Intent i = new Intent(OtherBankAccountConfirm.this,dashboard.class);
                        startActivity(i);
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

    }

    // intent for launch other transaction
    private void launchOtherTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
