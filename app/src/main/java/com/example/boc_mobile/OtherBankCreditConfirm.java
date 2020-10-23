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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.boc_mobile.Models.CreditCardPayments;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class OtherBankCreditConfirm extends AppCompatActivity {


    String uname;
    String amount;

    private Button confirmBtn;
    private TextView amountDetails,To,From,Method,Description;

    int balance,flag = 0;
    String from,to,method,pamount,des;
    private Button continueButton,backToTransMenu;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;


    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_credit_confirm);
        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Transactions");

        uname =  SaveSharedPreference.getUserName(OtherBankCreditConfirm.this);
        from = getIntent().getStringExtra("from");
        to= getIntent().getStringExtra("to");
        method = getIntent().getStringExtra("method");
        pamount = getIntent().getStringExtra("amount");
        des = getIntent().getStringExtra("description");


        // Buttons
        confirmBtn = (Button) findViewById(R.id.obcp_prcd_btn);

        uname =  SaveSharedPreference.getUserName(OtherBankCreditConfirm.this);
        from = getIntent().getStringExtra("from");
        to= getIntent().getStringExtra("to");
        method = getIntent().getStringExtra("method");
        pamount = getIntent().getStringExtra("amount");
        des = getIntent().getStringExtra("description");

        // show detail fields
        amountDetails = (TextView) findViewById(R.id.obcp_amount_detail);
        To = (TextView) findViewById(R.id.obcp_payto_detail);
        From = (TextView) findViewById(R.id.obcp_payfrom_detail);
        Method = (TextView) findViewById(R.id.obcp_method_detail);
        Description = (TextView) findViewById(R.id.obcp_desc_detail);


        navigationView = findViewById(R.id.drawerNavigation);
        //for side drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.dashboard){
                    Intent i = new Intent( OtherBankCreditConfirm.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    startActivity(new Intent(OtherBankCreditConfirm.this, MainActivity.class));
                    drawer.closeDrawers();
                    
                }
                else if(id == R.id.profile){
                    startActivity(new Intent(OtherBankCreditConfirm.this, UserProfile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent( OtherBankCreditConfirm.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });





        amountDetails.setText(pamount);
        amount = amountDetails.getText().toString();

        To = findViewById(R.id.obcp_payto_detail);
        To.setText(to);
        From = findViewById(R.id.obcp_payfrom_detail);
        From.setText(from);
        Method = findViewById(R.id.obcp_method_detail);
        Method.setText(method);
        Description = findViewById(R.id.obcp_desc_detail);
        Description.setText(des);



      /*  cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });*/
        // set onclick listener to Continue button
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                saveDetails();
            }
        });

    }


    // intent for launch other transaction
    private void launchOtherTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // set logout icon in app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    //for selected items on top bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankCreditConfirm.this);

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

                    Intent i = new Intent(OtherBankCreditConfirm.this, Login.class);
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


    /**
     * save details
     */
    public void saveDetails(){


        updateBalance();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("CrediCardPayments");

        CreditCardPayments payments = new CreditCardPayments();


        payments.setUname(uname);
        payments.setPayee(to);
        payments.setCustomerName(from);
        payments.setAmount(Integer.parseInt(pamount));
        payments.setDescription(des);
        payments.setMethod(method);

        db.push().setValue(payments);


        AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankCreditConfirm.this);

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

                if(flag == 1) {
                    Intent i = new Intent(OtherBankCreditConfirm.this, dashboard.class);
                    startActivity(i);

                }
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

    /**
     * update balance
     */

    public void updateBalance(){


        Query query = dbRef.child("User").orderByChild("uname").equalTo(uname);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String bal;
                    int newBal;


                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        String child = issue.getKey();
                        bal = issue.child("balance").getValue().toString();



                        balance =Integer.parseInt(bal);
                        newBal = balance-Integer.parseInt(amount);
                        String x = Integer.toString(newBal);


                        dbRef.child("User").child(child).child("balance").setValue(newBal);

                        flag = 1;


                        break;
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }




        });


    }
}
