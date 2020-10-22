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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.boc_mobile.Models.Biller;
import com.google.android.material.navigation.NavigationView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class addBiller2 extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    TextView customer,category,biller,acc,type;
    String customerName,billerCategory,billerName,accountNumber,bType,uname;

    Dialog dialog,popup;

    Button cancelPopup;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Biller");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_biller2);

        //---related to popup---------------
        popup = new Dialog(this);
        popup.setContentView(R.layout.success_popup);
        cancelPopup = popup.findViewById(R.id.popupCancel);
        popup.setCanceledOnTouchOutside(false);

        //---get details from previous activity-----------
        uname = SaveSharedPreference.getUserName(addBiller2.this);
        customerName = getIntent().getStringExtra("customer");
        billerCategory = getIntent().getStringExtra("category");
        billerName = getIntent().getStringExtra("biller");
        accountNumber = getIntent().getStringExtra("account");
      //  Toast.makeText(this, accountNumber, Toast.LENGTH_LONG).show();
        bType = getIntent().getStringExtra("type");


        //------set details in textView--------------
        customer = findViewById(R.id.name);
        customer.setText(customerName);

        category = findViewById(R.id.billCat);
        category.setText(billerCategory );


        biller = findViewById(R.id.biller);
        biller.setText(billerName);

        acc = findViewById(R.id.accNo);
        acc.setText(accountNumber);

        type =  findViewById(R.id.type);
        type.setText(bType);


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
                    Intent i = new Intent(addBiller2.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    Toast.makeText(addBiller2.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(addBiller2.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });


        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addBiller2.this,billPayment.class);
                startActivity(i);
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
            startActivity(new Intent(addBiller2.this, Login.class));
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

    Biller biller = new Biller();

    biller.setuname(uname);
    biller.setAccNo(accountNumber);
    biller.setBiller(billerName);
    biller.setBillerCategory(billerCategory);
    biller.setbType(bType);

    dbRef.push().setValue(biller);
    AlertDialog.Builder alert = new AlertDialog.Builder(addBiller2.this);

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


                launchDashboard();


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


}
