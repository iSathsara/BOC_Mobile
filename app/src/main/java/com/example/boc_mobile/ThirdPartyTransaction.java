package com.example.boc_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;


public class ThirdPartyTransaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button continueBtn;
    private EditText amount,desc;
    private Spinner payTo,payFrom;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private String payee,source,description,uname,pamount;
    String[] nameArray = new String[2];
    ArrayList<String> payeeList = new ArrayList<>();
    int check = 0;


    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_account_payment);

        // setting up toolbar
        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Transactions");
        /**
         * get customer name
         */

        getCustomerName();
        getPayeeName();

        // buttons
        continueBtn = findViewById(R.id.tp_cont_btn);

        // form fields
        payTo = findViewById(R.id.tp_payTo_spinner);
        payFrom = findViewById(R.id.tp_payFrom_spinner);
        amount = findViewById(R.id.tp_amount_editText);
        desc = findViewById(R.id.tp_desc_editText);



        navigationView = findViewById(R.id.drawerNavigation);

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
                    startActivity(new Intent(ThirdPartyTransaction.this, dashboard.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.transaction){
                    startActivity(new Intent(ThirdPartyTransaction.this, MainActivity.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.profile){
                    startActivity(new Intent(ThirdPartyTransaction.this, UserProfile.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.more){
                    startActivity(new Intent(ThirdPartyTransaction.this, moreActivityFunction.class));
                    drawer.closeDrawers();
                }
                return true;
            }
        });

        // continue button click
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(amount.getText()) || payee.equals("Select") || source.equals("Select")){
                    androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ThirdPartyTransaction.this);
                    dlgAlert.setMessage("Please fill required fields");
                    dlgAlert.setIcon(R.drawable.empty_warning);
                    dlgAlert.setTitle("Alert!!");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                else {

                    pamount = amount.getText().toString();
                    description = desc.getText().toString();
                    //intent
                    Intent intent = new Intent(getBaseContext(), confirmThirdPatyTransaction.class);
                    intent.putExtra("payee",payee);
                    intent.putExtra("source",source);
                    intent.putExtra("amount",pamount);
                    intent.putExtra("description",description);
                    startActivity(intent);
                }
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

    //for selected items on top bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(ThirdPartyTransaction.this);

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

                    Intent i = new Intent(ThirdPartyTransaction.this, Login.class);
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

                case R.id.tp_payTo_spinner:

                    payee = adapterView.getSelectedItem().toString();

                    break;

                case R.id.tp_payFrom_spinner:
                    source = adapterView.getSelectedItem().toString();
                    break;



            }





        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void getCustomerName(){

        uname = SaveSharedPreference.getUserName(ThirdPartyTransaction.this);
        final ProgressDialog progress = new ProgressDialog(ThirdPartyTransaction.this,R.style.MyAlertDialogStyle);
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


                    if (nameArray.length > 0) {
                        ArrayAdapter nameSequence = new ArrayAdapter<>(ThirdPartyTransaction.this, android.R.layout.simple_list_item_1, nameArray);
                        nameSequence.notifyDataSetChanged();
                        nameSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payFrom.setAdapter(nameSequence);
                        payFrom.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) ThirdPartyTransaction.this);
                        progress.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }  );
    }


    public void getPayeeName(){
        payeeList.add("Select");

        uname = SaveSharedPreference.getUserName(ThirdPartyTransaction.this);
        final ProgressDialog progress = new ProgressDialog(ThirdPartyTransaction.this,R.style.MyAlertDialogStyle);
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

                        if(type.equals("Third Party")){


                            payee = issue.child("name").getValue().toString();
                            payeeList.add(payee);

                        }



                    }


                    if (payeeList.size() > 0) {
                        ArrayAdapter PayeeSequence = new ArrayAdapter<>(ThirdPartyTransaction.this, android.R.layout.simple_list_item_1, payeeList);
                        PayeeSequence.notifyDataSetChanged();
                        PayeeSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payTo.setAdapter(PayeeSequence);
                        payTo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) ThirdPartyTransaction.this);
                        progress.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }  );
    }
}
