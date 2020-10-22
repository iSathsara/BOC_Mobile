package com.example.boc_mobile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {


    private Button addBeneficieries,thirdPartyTransaction,otherBankCreditTransBtn,bill_payment,otherBankAccTransBtn;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;



    String accNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //accNo = getIntent().getStringExtra("accountNo");

        navigationView = findViewById(R.id.drawerNavigation);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");


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
                    Intent i = new Intent(MainActivity.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    Toast.makeText(MainActivity.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(MainActivity.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });


    }


    private void launchOtherBankCreditPay(){
        Intent intent = new Intent(this, OtherBankCreditCardPayment.class);
        //intent.putExtra("accNo",accNo);
        startActivity(intent);
    }

    private void launchOtherBankAccountPay(){
        Intent intent = new Intent(this, OtherBankAccountPayment.class);
        startActivity(intent);
    }

    private void launchThirdPartyAccountTransfer(){
        Intent intent = new Intent(this, ThirdPartyTransaction.class);
        //intent.putExtra("accNo",accNo);
        startActivity(intent);
    }


    private void launchBillPayments(){
        Intent intent = new Intent(this, billPayment.class);
        //intent.putExtra("accNo",accNo);
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

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

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
                    Intent i = new Intent(MainActivity.this,Login.class);
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

