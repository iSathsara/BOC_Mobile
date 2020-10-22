package com.example.boc_mobile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.google.android.material.navigation.NavigationView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class billPayment3 extends AppCompatActivity {


    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    String customerName,billerName,accountNumber,invoice,pamount,userAccount,uname;
    TextView customer,payee,amount,invoiceNum,accNo;
    int balance,flag = 0;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("BillPayments");

    Dialog popup;

    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment3);
        getSupportActionBar().setTitle("BOC Mobile Banking - Bill Payments");

        //userAccount = getIntent().getStringExtra("accNo");

        //---related to popup---------------
        popup = new Dialog(this);
        popup.setContentView(R.layout.transaction_success_popup);
        done = popup.findViewById(R.id.popupCancel);
        popup.setCanceledOnTouchOutside(false);
        //---get details from prevoius activity-----------

        uname = SaveSharedPreference.getUserName(billPayment3.this);
        customerName = getIntent().getStringExtra("customer");
        invoice = getIntent().getStringExtra("invoiceNo");
        billerName = getIntent().getStringExtra("biller");
        accountNumber = getIntent().getStringExtra("accNo");
        pamount  = getIntent().getStringExtra("amount");



        customer = findViewById(R.id.name);
        payee = findViewById(R.id.billCat);
        amount = findViewById(R.id.amount);
        invoiceNum = findViewById(R.id.type1);
        accNo = findViewById(R.id.accNo);


        customer.setText(customerName);
        payee.setText(billerName);
        invoiceNum.setText(invoice);
        amount.setText(pamount);
        accNo.setText(accountNumber);


        navigationView = findViewById(R.id.drawerNavigation);

        //change the topbar title
        getSupportActionBar().setTitle("Transactions");



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
                    Intent i = new Intent(billPayment3.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    Intent i = new Intent(billPayment3.this,MainActivity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    Toast.makeText(billPayment3.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(billPayment3.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Query query = dbRef.child("User").orderByChild("uname").equalTo(userAccount);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            String bal;
                            int newBal;


                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                String child = issue.getKey();
                                bal = issue.child("balance").getValue().toString();


                                Toast.makeText(billPayment3.this,child ,Toast.LENGTH_LONG).show();
                                balance =Integer.parseInt(bal);
                                newBal = balance-Integer.parseInt(pamount);
                                String x = Integer.toString(newBal);


                                dbRef.child("User").child(child).child("balance").setValue(newBal);

                                Intent i = new Intent(billPayment3.this,dashboard.class);
                                i.putExtra("uname",userAccount);
                                startActivity(i);
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });

    }

    //menu on top bar
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
            startActivity(new Intent(billPayment3.this, Login.class));
            finish();
        }


        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void cancelOnClick(View view){

        finish();
    }


    public void confirmButtonClick(View view){
        updateAccountBalance();
        PaidBills pb = new PaidBills();

        pb.setCustomerId(uname);
        pb.setAccNo(accountNumber);
        pb.setBiller(billerName);
        pb.setInvoice(invoice);
        pb.setAmount(new Integer(pamount));

        dbRef.push().setValue(pb);

        AlertDialog.Builder alert = new AlertDialog.Builder(billPayment3.this);

        alert.setTitle("SUCCESSFUL");
        alert.setIcon(R.drawable.transaction_okay);
        alert.setMessage("The amount is transferred successfully");
        alert.setPositiveButton("DONE", null);
        alert.setNegativeButton("Another Transaction", null);

        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_design);

        //updateAccountBalance();

        // this will change the default behaviour of buttons
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 1){

                    launchDashboard();
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

    private void launchDashboard(){
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }

    // intent for launch other transaction
    private void launchOtherTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * Update balance
     */

    public void updateAccountBalance(){

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query query = db.child("User").orderByChild("uname").equalTo(uname);

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
                        newBal = balance-Integer.parseInt(pamount);


                        db.child("User").child(child).child("balance").setValue(newBal);
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
