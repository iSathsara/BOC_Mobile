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
    String customerName,billerName,accountNumber,invoice,pamount,userAccount;
    TextView customer,payee,amount,invoiceNum,accNo;
    int balance;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    Dialog popup;

    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment3);

        userAccount = getIntent().getStringExtra("accNo");

        //---related to popup---------------
        popup = new Dialog(this);
        popup.setContentView(R.layout.transaction_success_popup);
        done = popup.findViewById(R.id.popupCancel);
        popup.setCanceledOnTouchOutside(false);
        //---get details from prevoius activity-----------

        customerName = getIntent().getStringExtra("customer");
        invoice = getIntent().getStringExtra("invoice");
        billerName = getIntent().getStringExtra("biller");
        accountNumber = getIntent().getStringExtra("account");
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
        getSupportActionBar().setTitle("Bill Payments");


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


        if (id == R.id.help) {


            //Toast.makeText(dashboard.this, "Action clicked", Toast.LENGTH_LONG).show();

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

        PaidBills pb = new PaidBills();

        pb.setCustomerId(customerName);
        pb.setAccNo(accountNumber);
        pb.setBiller(billerName);
        pb.setInvoice(invoice);
        pb.setAmount(new Integer(pamount));

        dbRef.push().setValue(pb);

        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();

    }
}
