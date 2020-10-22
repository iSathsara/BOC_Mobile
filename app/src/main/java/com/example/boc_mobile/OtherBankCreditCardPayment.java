package com.example.boc_mobile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class OtherBankCreditCardPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button continueButton,backToTransMenu;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    String uname;
    Spinner payFrom,payTo,paymentMethod;
    EditText amount,description;
    int check = 0;
    String from = "Select",to = "Select",method = "Select",pamount  = "",des;
    ArrayList<String> payeeList = new ArrayList<>();

    String[] nameArray = new String[2];

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_credit_card_payment);

        // setting up toolbar

        getSupportActionBar().setTitle("Transactions");

        //Get customer name from DB
        getCustomerName();
        getPayeeName();

        //backToTransMenu = (Button) findViewById(R.id.obcp_cancel_btn);

        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");

        // Buttons

        continueButton = (Button) findViewById(R.id.obcp_cont_btn);

        // input fields
        payFrom = findViewById(R.id.obcp_payFrom_spinner);
        payTo = findViewById(R.id.obcp_payTo_spinner);
        paymentMethod = findViewById(R.id.obcp_payMethod_spinner);
        amount = findViewById(R.id.obcp_amount_editText);
        description = findViewById(R.id.obcp_desc_editText);




        // go back to transaction menu screen
        /*backToTransMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        // go to confirm the transaction
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoConfirm();
            }
        });



        /**
         * add items to spinners
         */

        ArrayAdapter<CharSequence> method = ArrayAdapter.createFromResource(this,R.array.pMethod,android.R.layout.simple_spinner_item);
        method.notifyDataSetChanged();
        method.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethod.setAdapter(method );
        paymentMethod.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);

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
                    Intent i = new Intent( OtherBankCreditCardPayment.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    Toast.makeText( OtherBankCreditCardPayment.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent( OtherBankCreditCardPayment.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });

    }

    /**
     * method to goto confirm transaction
      */

    private void gotoConfirm(){



        pamount = amount.getText().toString();
        des = description.getText().toString();

       if(to.equals("Select") || from.equals("Select") || method.equals("Select") || pamount.equals("") ){

          /*
             Empty fields
           */

            androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please fill required fields");
            dlgAlert.setIcon(R.drawable.empty_warning);
            dlgAlert.setTitle("Alert!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

        }else{


            Intent intent = new Intent(this, OtherBankCreditConfirm.class);
            intent.putExtra("to",to);
            intent.putExtra("from",from);
            intent.putExtra("amount",pamount);
            intent.putExtra("method",method);
            intent.putExtra("description",des);

            startActivity(intent);
        }





        //pamount = amount.getText().toString();
        //des = description.getText().toString();



    }


    // set logout icon in app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // set logout function
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.logout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankCreditCardPayment.this);

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
                    Intent i = new Intent(OtherBankCreditCardPayment.this,Login.class);
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



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(++check>1) {

            int id = adapterView.getId();

            switch(id){

                case R.id.obcp_payTo_spinner:

                   to = adapterView.getSelectedItem().toString();

                    break;

                case R.id.obcp_payFrom_spinner:
                    from = adapterView.getSelectedItem().toString();
                    break;

                case  R.id.obcp_payMethod_spinner:
                    method = adapterView.getSelectedItem().toString();
                    break;


            }





        }


    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    public void getCustomerName(){

        uname = SaveSharedPreference.getUserName(OtherBankCreditCardPayment.this);

        final ProgressDialog progress = new ProgressDialog(OtherBankCreditCardPayment.this,R.style.MyAlertDialogStyle);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

         /*
          DB query
         */

        Query query = dbRef.child("User").orderByChild("uname").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String customer;

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        customer = issue.child("Name").getValue().toString();

                        nameArray[0] = "Select";
                        nameArray[1] = customer;
                        break;
                    }


                    if (nameArray.length > 1) {
                        ArrayAdapter nameSequence = new ArrayAdapter<>(OtherBankCreditCardPayment.this, android.R.layout.simple_list_item_1, nameArray);
                        nameSequence.notifyDataSetChanged();
                        nameSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payFrom.setAdapter(nameSequence);
                        payFrom.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);
                        progress.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }  );
    }


    /**
     * get payee names
     */

    public void getPayeeName() {
        payeeList.add("Select");

        uname = SaveSharedPreference.getUserName(OtherBankCreditCardPayment.this);
        final ProgressDialog progress = new ProgressDialog(OtherBankCreditCardPayment.this, R.style.MyAlertDialogStyle);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

         /*
          DB query
         */

        Query query = dbRef.child("Beneficiaries").orderByChild("uname").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String payee;
                    String type;

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {


                        type = issue.child("type").getValue().toString();

                        if (type.equals("Credit Card")) {


                            payee = issue.child("name").getValue().toString();
                            payeeList.add(payee);

                        }


                    }


                    if (payeeList.size() > 1) {
                        ArrayAdapter PayeeSequence = new ArrayAdapter<>(OtherBankCreditCardPayment.this, android.R.layout.simple_list_item_1, payeeList);
                        PayeeSequence.notifyDataSetChanged();
                        PayeeSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payTo.setAdapter(PayeeSequence);
                        payTo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);
                        progress.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

