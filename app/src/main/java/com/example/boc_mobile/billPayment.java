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
import android.widget.Spinner;
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

import java.util.ArrayList;

//public class billPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

public class billPayment extends AppCompatActivity{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    Spinner customer,billerSpinner;
    Button next,cancel,addBiller;
    String name,biller,accNo;
    ArrayList<String> billList = new ArrayList<>();
    int check = 0;

    ProgressDialog progress;

    //Database
    //DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        getSupportActionBar().setTitle("BOC Mobile Banking - Bill Payments");

        billerSpinner = findViewById(R.id.type1);
        next = findViewById(R.id.pay);
        addBiller = (Button)findViewById(R.id.addBiller);

        //---------------------------------------------------------------------------------------------------------
        // function by ISURU to test next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextOnClick();
            }
        });
        //---------------------------------------------------------------------------------------------------------

        //---------------------------------------------------------------------------------------------------------
        // function by ISURU to test add biller button
        addBiller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillerOnClick();
            }
        });
        //---------------------------------------------------------------------------------------------------------


        /*
        accNo = getIntent().getStringExtra("accNo");


        name = "Select";

        next.setOnClickListener((View.OnClickListener) this);
        customer = findViewById(R.id.customer);
        ArrayAdapter<CharSequence> sequence = ArrayAdapter.createFromResource(this,R.array.customer,android.R.layout.simple_spinner_item);
        sequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customer.setAdapter(sequence);
        customer.setOnItemSelectedListener(this);

        */

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
                    Toast.makeText(billPayment.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
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
            startActivity(new Intent(billPayment.this, Login.class));
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
    public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {

        if (++check > 1) {

            int ID = parent.getId();

            switch (ID) {

                case R.id.customer:
                    name = parent.getSelectedItem().toString();
                    progress = new ProgressDialog(billPayment.this);
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
                    break;

            }
        }
    }
*/

    //@Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void cancelOnClick(View view){

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void adBillerOnClick(View view){

        Intent i = new Intent(this, addBiller.class);
        //i.putExtra("accNo",accNo);
        startActivity(i);
    }

    //---------------------------------------------------------------------------------------------------------
    // function by ISURU, for testing NEXT button
    public void addBillerOnClick(){
        startActivity(new Intent(billPayment.this, billPayment2.class));
    }
    //---------------------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------------------------------------
    // function by ISURU, for testing NEXT button
    public void nextOnClick(){
        startActivity(new Intent(billPayment.this, billPayment2.class));
    }
    //---------------------------------------------------------------------------------------------------------


/*
    public void getBillerNames(String name){

        Query query = dbRef.child("Biller").orderByChild("customerName").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                       String str = issue.child("biller").getValue().toString();
                       billList.add(str);
                        //Toast.makeText(billPayment.this, str, Toast.LENGTH_LONG).show();
                    }

                    if(billList.size() > 0){

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
*/


    public void nextButtonClick(View view){

        if(!name.equals("Select")){

            Intent i = new Intent(billPayment.this,billPayment2.class);
            //i.putExtra("customer",name);
            //i.putExtra("biller",biller);
            //i.putExtra("accNo",accNo);
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

}
