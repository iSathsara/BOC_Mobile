package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.boc_mobile.Models.ThirdPartyTransactions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class confirmThirdPatyTransaction extends AppCompatActivity {

    private Button proceedBtn;
    private TextView payfrom_view,payto_view,amount_view,desc_view;
    private String payee,source,description = "",uname,pamount;
     DrawerLayout drawer;
     ActionBarDrawerToggle drawerToggle;
     NavigationView navigationView;
     int balance,flag = 0;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ThirdPartyTransactions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_account_confirm);

        // setting up toolbar
        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");


        //Get data from intent
        payee = getIntent().getStringExtra("payee");
        uname = SaveSharedPreference.getUserName(confirmThirdPatyTransaction.this);
        source = getIntent().getStringExtra("source");
        description = getIntent().getStringExtra("description");
        pamount = getIntent().getStringExtra("amount");

        // buttons
        proceedBtn = findViewById(R.id.tp_prcd_btn);

        // text fields
        payto_view = findViewById(R.id.tp_payto_confirm);
        payto_view.setText(payee);
        payfrom_view = findViewById(R.id.tp_payfrom_confirm);
        payfrom_view.setText(source);
        amount_view = findViewById(R.id.tp_amount_confirm);
        amount_view.setText(pamount);
        desc_view = findViewById(R.id.tp_desc_confirm);
        desc_view.setText(description);

        // navigation view
        navigationView = findViewById(R.id.drawerNavigation);

        //for side drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(confirmThirdPatyTransaction.this,drawer,R.string.open,R.string.close);
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
                saveDetails();

                AlertDialog.Builder alert = new AlertDialog.Builder(confirmThirdPatyTransaction.this);

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

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.logout:

                // implement function here
                //Toast.makeText(this, "Logout selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * save details in database
     */

    public void saveDetails(){

        updateAccountBalance();
        ThirdPartyTransactions transaction = new ThirdPartyTransactions();

        transaction.setPayee(payee);
        transaction.setAmount(Integer.parseInt(pamount));
        transaction.setUname(uname);
        transaction.setDescription(description);
        transaction.setName(source);

        dbRef.push().setValue(transaction);


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
