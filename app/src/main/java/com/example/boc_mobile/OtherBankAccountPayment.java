package com.example.boc_mobile;

import android.app.ProgressDialog;
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
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherBankAccountPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button continueBtn;
    private Spinner payto_spinner,payfrom_spinner;
    private EditText amount_editText, desc_editText;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private EditText description_text,amount_text;
    private int amount;
    private String from = "Select",to = "Select",pamount  = "",des,uname;
    private int flag = 0,check = 0;
    private ArrayList<String> payeeList = new ArrayList<>();
    private String[] nameArray = new String[2];


    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_account_payment);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");

        // Buttons
        continueBtn = findViewById(R.id.tp_cont_btn);

        // input fields
        payto_spinner = findViewById(R.id.oba_payTo_spinner);
        payfrom_spinner = findViewById(R.id.oba_payFrom_spinner);
        amount_editText = findViewById(R.id.oba_amount_editText);
        desc_editText = findViewById(R.id.oba_desc_editText);


        //get details from DB
        getPayeeName();
        getCustomerName();


        navigationView = findViewById(R.id.drawerNavigation);

        //for side drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        description_text = findViewById(R.id.oba_amount_editText);
        amount_text = findViewById(R.id.oba_desc_editText);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.dashboard){
                    startActivity(new Intent(OtherBankAccountPayment.this, dashboard.class));
                    drawer.closeDrawers();
                }

                if(id == R.id.transaction){
                    startActivity(new Intent(OtherBankAccountPayment.this, MainActivity.class));
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

        // launch confirm
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCnonfirmActivity();
            }
        });

    }

    // launch confirm
    private void launchCnonfirmActivity(){

        pamount = amount_editText.getText().toString();
        des = desc_editText.getText().toString();


        if(to.equals("Select") || from.equals("Select") ||  pamount.equals("") ){

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


            Intent intent = new Intent(OtherBankAccountPayment.this, OtherBankAccountConfirm.class);
            intent.putExtra("to",to);
            intent.putExtra("from",from);
            intent.putExtra("amount",pamount);
            intent.putExtra("description",des);

            startActivity(intent);
        }



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

            AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankAccountPayment.this);

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
                    Intent i = new Intent(OtherBankAccountPayment.this,Login.class);
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

                case R.id.oba_payTo_spinner:

                    to = adapterView.getSelectedItem().toString();

                    break;

                case R.id.oba_payFrom_spinner:
                    from = adapterView.getSelectedItem().toString();
                    break;



            }





        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    public void getPayeeName() {
        payeeList.add("Select");

        uname = SaveSharedPreference.getUserName(OtherBankAccountPayment.this);
        final ProgressDialog progress = new ProgressDialog(OtherBankAccountPayment.this, R.style.MyAlertDialogStyle);
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

                        if (type.equals("Other Bank")) {


                            payee = issue.child("name").getValue().toString();
                            payeeList.add(payee);

                        }


                    }


                    if (payeeList.size() > 1) {
                        ArrayAdapter PayeeSequence = new ArrayAdapter<>(OtherBankAccountPayment.this, android.R.layout.simple_list_item_1, payeeList);
                        PayeeSequence.notifyDataSetChanged();
                        PayeeSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payto_spinner.setAdapter(PayeeSequence);
                        payto_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankAccountPayment.this);
                        progress.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * get customer name
     */

    public void getCustomerName(){

        uname = SaveSharedPreference.getUserName(OtherBankAccountPayment.this);

        final ProgressDialog progress = new ProgressDialog(OtherBankAccountPayment.this,R.style.MyAlertDialogStyle);
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
                        ArrayAdapter nameSequence = new ArrayAdapter<>(OtherBankAccountPayment.this, android.R.layout.simple_list_item_1, nameArray);
                        nameSequence.notifyDataSetChanged();
                        nameSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payfrom_spinner .setAdapter(nameSequence);
                        payfrom_spinner .setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankAccountPayment.this);
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

