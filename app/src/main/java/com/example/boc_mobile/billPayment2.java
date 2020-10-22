package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
*/

public class billPayment2 extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    String customerName,billerName,accountNumber,invoice,pamount,userAccount;
    TextView account;
    EditText invoiceNo,amount;
    Button pay,cancel;

    //Database
    //DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment2);
        getSupportActionBar().setTitle("BOC Mobile Banking - Bill Payments");

        invoice = "";
        pamount = "";

        //userAccount = getIntent().getStringExtra("accNo");

        //Toast.makeText(billPayment2.this, userAccount, Toast.LENGTH_LONG).show();
        //---get details from prevoius activity-----------

        //customerName = getIntent().getStringExtra("customer");
        //billerName = getIntent().getStringExtra("biller");



        account = findViewById(R.id.account);
        invoiceNo = findViewById(R.id.type1);
        amount = findViewById(R.id.amount);
        pay = findViewById(R.id.pay);
        cancel = findViewById(R.id.popupCancel);


        navigationView = findViewById(R.id.drawerNavigation);


        //for side drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //------------------------------------------------------------------------------------------------------------------------------
        // function by ISURU to test NEXT button
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(billPayment2.this, billPayment3.class));
            }
        });
        //-------------------------------------------------------------------------------------------------------------------------------


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.dashboard){
                    Intent i = new Intent(billPayment2.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    Intent i = new Intent(billPayment2.this,MainActivity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    Toast.makeText(billPayment2.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(billPayment2.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });

       //getAccountNumber();
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
            AlertDialog.Builder alert = new AlertDialog.Builder(billPayment2.this);

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
                    Intent i = new Intent(billPayment2.this,Login.class);
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


    public void cancelOnClick(View view){

        finish();
    }

/*
    public void getAccountNumber(){
        Query query = dbRef.child("Biller").orderByChild("customerName").equalTo(customerName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String bill = "";
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        bill = issue.child("biller").getValue().toString();
                        if(bill.equals(billerName)){

                           accountNumber = issue.child("accNo").getValue().toString();
                            account.setText(accountNumber);
                            break;
                        }



                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

*/

/*
    public void payButtonClick(View view){
        invoice = invoiceNo.getText().toString();
        pamount = amount.getText().toString() ;

        if(!invoice.equals("") && !pamount.equals("")){

            Intent i = new Intent(billPayment2.this,billPayment3.class);
            i.putExtra("customer",customerName);
            i.putExtra("amount",pamount);
            i.putExtra("biller",billerName);
            i.putExtra("account",accountNumber);
            i.putExtra("accNo",userAccount);
            i.putExtra("invoice",invoice);
            startActivity(i);
        }else{
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please fill required fields");
            dlgAlert.setIcon(R.drawable.ic_error_black_24dp);
            dlgAlert.setTitle("Alert!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

    }
*/

}
