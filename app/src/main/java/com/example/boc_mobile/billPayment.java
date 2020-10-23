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

/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
*/

import java.util.ArrayList;


public class billPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    TextView acc;
    EditText invoice,amount;
    NavigationView navigationView;
    Spinner customer,billerSpinner;
    Button next,cancel,addBiller;
    String name,biller,accNo,uname,invoiceNum,pamount;
    ArrayList<String> billList = new ArrayList<>();
    int check = 0;
    String[] nameArray = new String[2];

    ProgressDialog progress;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        getSupportActionBar().setTitle("Transactions");

        uname = SaveSharedPreference.getUserName(billPayment.this);

        billerSpinner = findViewById(R.id.type1);
        next = findViewById(R.id.pay);
        addBiller = (Button)findViewById(R.id.addBiller);
        acc = findViewById(R.id.account);
        amount = findViewById(R.id.amount);
        invoice = findViewById(R.id.invoice);

        name = "Select";
        biller = "Select";
        invoiceNum = "";
        pamount = "";
       // next.setOnClickListener((View.OnClickListener) this);
        customer = findViewById(R.id.customer);
        getCustomerName();


        navigationView = findViewById(R.id.drawerNavigation);
        //change the topbar title
        getSupportActionBar().setTitle("Transactions");




        // navigation components
        navigationView = findViewById(R.id.drawerNavigation);

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
                    Intent i = new Intent(billPayment.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    Intent i = new Intent(billPayment.this,MainActivity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    startActivity(new Intent(billPayment.this, UserProfile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(billPayment.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
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
            AlertDialog.Builder alert = new AlertDialog.Builder(billPayment.this);

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

                    Intent i = new Intent(billPayment.this, Login.class);
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


    public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {

        if (++check > 1) {

            int ID = parent.getId();

            switch (ID) {

                case R.id.customer:
                    name = parent.getSelectedItem().toString();
                    progress = new ProgressDialog(billPayment.this,R.style.MyAlertDialogStyle);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    if (!name.equals("Select")) {
                        // Toast.makeText(addBiller.this,"3", Toast.LENGTH_LONG).show();
                        getBillerNames(name);
                    }
                    break;

                case R.id.type1:
                    biller = parent.getSelectedItem().toString();
                    if(!biller.equals("Select")){

                        getAccountNumber();
                    }
                    break;

            }
        }
    }


    //@Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void adBillerOnClick(View view){

        Intent i = new Intent(this, addBiller.class);
        //i.putExtra("accNo",accNo);
        startActivity(i);
    }






    public void getBillerNames(String name){
        billList.add("Select");

        Query query = dbRef.child("Biller").orderByChild("uname").equalTo(uname);
        ((Query) query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                       String str = issue.child("biller").getValue().toString();
                       billList.add(str);
                        //Toast.makeText(billPayment.this, str, Toast.LENGTH_LONG).show();
                    }

                    if(billList.size() > 1){

                        progress.dismiss();
                    }
                    ArrayAdapter biller_list = new ArrayAdapter<>(billPayment.this, android.R.layout.simple_list_item_1, billList);
                    biller_list.notifyDataSetChanged();
                    biller_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    billerSpinner.setAdapter(biller_list);
                    // biller.setEnabled(true);
                    billerSpinner.setOnItemSelectedListener(billPayment.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Toast.makeText(addBiller.this,  "4", Toast.LENGTH_LONG).show();





    }



    public void nextButtonClick(View view){

         invoiceNum = invoice.getText().toString();
         pamount = amount.getText().toString();


        if(name.equals("Select") || biller.equals("Select") || invoiceNum.equals("") || pamount.equals("")){


            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please fill required fields");
            dlgAlert.setIcon(R.drawable.empty_warning);
            dlgAlert.setTitle("Alert!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

        }else{
            Intent i = new Intent(billPayment.this,billPayment3.class);
            i.putExtra("customer",name);
            i.putExtra("biller",biller);
            i.putExtra("accNo",accNo);
            i.putExtra("invoiceNo",invoiceNum);
            i.putExtra("amount",pamount);
            startActivity(i);
        }



    }


    public void getAccountNumber(){




        Query query = dbRef.child("Biller").orderByChild("biller").equalTo(biller);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String no = "";
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        no = issue.child("accNo").getValue().toString();
                        acc.setText(no);
                        accNo = acc.getText().toString();

                        if(!accNo.equals("")){

                            progress.dismiss();
                        }

                        break;



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

        uname = SaveSharedPreference.getUserName(billPayment.this);
        final ProgressDialog progress = new ProgressDialog(billPayment.this,R.style.MyAlertDialogStyle);
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

                    String customers;

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        customers = issue.child("Name").getValue().toString();

                        nameArray[0] = "Select";
                        nameArray[1] = customers;
                        break;
                    }


                    if (nameArray.length > 1) {
                        ArrayAdapter nameSequence = new ArrayAdapter<>(billPayment.this, android.R.layout.simple_list_item_1, nameArray);
                        nameSequence.notifyDataSetChanged();
                        nameSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        customer.setAdapter(nameSequence);
                        customer.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) billPayment.this);
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
