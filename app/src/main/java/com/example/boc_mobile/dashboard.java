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
    Button creditCard;
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
        getSupportActionBar().setTitle("BOC Mobile Banking - Dashboard");

        //change the topbar title
        getSupportActionBar().setTitle("Dashboard");

        // Toast.makeText(this, uname, Toast.LENGTH_LONG).show();
        bal = findViewById(R.id.accNo);
        acc = findViewById(R.id.balance);
        Name = findViewById(R.id.pay);
        branch = findViewById(R.id.branch);

        creditCard = findViewById(R.id.creditCard);
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

                }else if(id == R.id.transaction){

                    Intent i = new Intent(dashboard.this,MainActivity.class);
                    //i.putExtra("accountNo",uname);

                    startActivity(i);

                }
                return true;
            }
        });
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



        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkCreditCards(View view){


    }





}
