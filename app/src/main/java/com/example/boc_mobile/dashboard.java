package com.example.boc_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import
        android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;



public class dashboard extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    Button creditCard,loans,cheques,fixedDepo;
    TextView myAcc;
    String uname,name,accNo,balance,Branch;
    TextView bal,acc,branch,Name;
    ProgressDialog progress;
    View containerView;


    //Database

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //getSupportActionBar().setTitle("Dashboard");

        //change the topbar title
        getSupportActionBar().setTitle("Dashboard");

        // Toast.makeText(this, uname, Toast.LENGTH_LONG).show();
        bal = findViewById(R.id.accNo);
        acc = findViewById(R.id.balance);
        Name = findViewById(R.id.pay);
        branch = findViewById(R.id.branch);

        creditCard = findViewById(R.id.creditCard);
        loans = findViewById(R.id.loans);
        cheques = findViewById(R.id.cheques);
        fixedDepo = findViewById(R.id.fixedDepo);

        navigationView = findViewById(R.id.drawerNavigation);




        progress = new ProgressDialog(dashboard.this,R.style.MyAlertDialogStyle);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();



        //for side drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        /*
         Get user details from DB
         */
        uname = SaveSharedPreference.getUserName(dashboard.this);

        Query query = dbRef.child("User").orderByChild("uname").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        name = issue.child("Name").getValue().toString();
                        accNo = issue.child("account").getValue().toString();
                        balance = issue.child("balance").getValue().toString();
                        Branch = issue.child("branch").getValue().toString();

                        int accountBal = Integer.parseInt(balance);

                        //format balance
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String formattedBalance= formatter.format(accountBal);

                        acc.setText(accNo);
                        bal.setText("LKR "+formattedBalance);
                        Name.setText(name);
                        branch.setText(Branch);
                        if(!name.equals("") && !accNo.equals("") && !balance.equals("")){

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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.dashboard){

                    drawer.closeDrawers();

                }else if(id == R.id.transaction){

                    Intent i = new Intent(dashboard.this,MainActivity.class);
                    startActivity(i);
                   drawer.closeDrawers();

                }
                else if(id == R.id.profile){
                    startActivity(new Intent(dashboard.this, UserProfile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(dashboard.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });

        cheques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCheques();
            }
        });

        fixedDepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFixedDepo();
            }
        });

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreditCards();
            }
        });

        loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoans();
            }
        });

    }

    public void showLoans(){

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("You do not have any Loans yet..");
        dlgAlert.setIcon(R.drawable.ic_monetization_on_black_24dp);
        dlgAlert.setTitle("Loans");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void showCreditCards(){

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("You do not have any BOC credit cards yet..");
        dlgAlert.setIcon(R.drawable.ic_credit_card_black_24dp);
        dlgAlert.setTitle("BOC Cards");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void showFixedDepo(){

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("You do not have any fixed deposits yet..");
        dlgAlert.setIcon(R.drawable.ic_lock_black_24dp);
        dlgAlert.setTitle("Fixed Deposits");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void showCheques(){

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("You do not have any cheques yet..");
        dlgAlert.setIcon(R.drawable.ic_library_books_black_24dp);
        dlgAlert.setTitle("Cheques");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

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

            AlertDialog.Builder alert = new AlertDialog.Builder(dashboard.this);

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

                    Intent i = new Intent(dashboard.this, Login.class);
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

    public void checkCreditCards(View view){


    }





}
