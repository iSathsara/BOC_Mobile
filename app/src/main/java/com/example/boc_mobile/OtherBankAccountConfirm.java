package com.example.boc_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boc_mobile.Models.OtherBankPayemnts;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OtherBankAccountConfirm extends AppCompatActivity {

    private Button proceedBtn;
    private TextView payto_textView,payfrom_textView,amount_textView, desc_textView;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private String from ,to ,pamount ,des = "",uname;
    int flag = 0,balance;
    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_other_bank_account_transaction);
        getSupportActionBar().setTitle("Transactions");

        //details from previous activity
        uname = SaveSharedPreference.getUserName(OtherBankAccountConfirm .this);
        from = getIntent().getStringExtra("from");
        to= getIntent().getStringExtra("to");
        pamount = getIntent().getStringExtra("amount");
        des = getIntent().getStringExtra("description");


        // Buttons
        proceedBtn = findViewById(R.id.oba_proceedBtn);

        // show details fields
        payto_textView = findViewById(R.id.oba_payto_confirm);
        payto_textView.setText(to);
        payfrom_textView = findViewById(R.id.oba_payfrom_confirm);
        payfrom_textView.setText(from);
        amount_textView = findViewById(R.id.oba_amount_confirm);
        amount_textView.setText(pamount);
        desc_textView = findViewById(R.id.oba_desc_confirm);
        desc_textView.setText(des);

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
                    startActivity(new Intent(OtherBankAccountConfirm.this, UserProfile.class));
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

                saveDetails();



            }
        });

    }

    // set logout icon in app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // intent for launch other transaction
    private void launchOtherTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * save details
     */
    public void saveDetails(){


        updateBalance();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("OtherBankAccountPayments");

        OtherBankPayemnts payments = new  OtherBankPayemnts();


        payments.setUname(uname);
        payments.setPayee(to);
        payments.setCustomerName(from);
        payments.setAmount(Integer.parseInt(pamount));
        payments.setDescription(des);


        db.push().setValue(payments);


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

                if(flag == 1) {
                    Intent i = new Intent(OtherBankAccountConfirm.this, dashboard.class);
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
                        newBal = balance-Integer.parseInt(pamount);
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
}
