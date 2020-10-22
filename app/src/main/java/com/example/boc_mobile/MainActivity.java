package com.example.boc_mobile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {


    private Button addBeneficieries;
    private Button thirdPartyTransaction;
    private Button otherBankCreditTransBtn;
    private Button otherBankAccTransBtn,bill_payment;
    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // setting up toolbar
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.drawerNavigation);

        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transacrions");


        addBeneficieries = findViewById(R.id.add_bene_btn);
        addBeneficieries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launchAddBeneficieries();
            }
        });

        otherBankCreditTransBtn = findViewById(R.id.other_bnk_credit_btn);
        otherBankCreditTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOtherBankCreditPay();
            }
        });

        otherBankAccTransBtn = findViewById(R.id.other_bnk_acc_btn);
        otherBankAccTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOtherBankAccountPay();
            }
        });

        thirdPartyTransaction = (Button) findViewById(R.id.third_prty_boc_btn);
        thirdPartyTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchThirdPartyAccountTransfer();
            }
        });


        bill_payment = findViewById(R.id.bill_pay_btn);
        bill_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBillPayments();
            }
        });

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

                    drawer.closeDrawers();
                }
                return true;
            }
        });


    }

    // close naviation drawer when clicks on back button

    // intent for launch other bank credit card

    /*
    private void launchAddBeneficieries(){

    }
    */

    private void launchOtherBankCreditPay(){
        Intent intent = new Intent(this, OtherBankCreditCardPayment.class);
        startActivity(intent);
    }

    private void launchOtherBankAccountPay(){
        Intent intent = new Intent(this, OtherBankAccountPayment.class);
        startActivity(intent);
    }

    private void launchThirdPartyAccountTransfer(){
        //Intent intent = new Intent(this, ThirdPartyTransaction.class);
        //intent.putExtra("accNo",accNo);
        //startActivity(intent);
    }


    private void launchBillPayments(){
        Intent intent = new Intent(this, billPayment.class);
        startActivity(intent);
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
            startActivity(new Intent(MainActivity.this, Login.class));
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // set logout function


}

