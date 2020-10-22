package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class dashboard extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    Button creditCard;
    TextView myAcc;
    String uname,name,accNo,balance;
    TextView bal,acc,branch,Name;

    //Database
   // DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setTitle("BOC Mobile Banking - Dashboard");

       // Toast.makeText(this, uname, Toast.LENGTH_LONG).show();
        bal = findViewById(R.id.accNo);
        acc = findViewById(R.id.balance);
        Name = findViewById(R.id.pay);

        creditCard = findViewById(R.id.creditCard);
        navigationView = findViewById(R.id.drawerNavigation);



        //for side drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        //-------get name---------------------
        //uname = getIntent().getStringExtra("uname");
        /*
        Query query = dbRef.child("User").orderByChild("uname").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        name = issue.child("Name").getValue().toString();
                        accNo = issue.child("account").getValue().toString();
                        balance = issue.child("balance").getValue().toString();

                        acc.setText(accNo);
                        bal.setText("LKR "+balance);
                        Name.setText(name);
                        break;
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.dashboard){
                   drawer.closeDrawers();

                }else if(id == R.id.transaction){

                    Intent i = new Intent(dashboard.this,MainActivity.class);
                    //i.putExtra("accountNo",uname);
                    startActivity(i);

                }
                else if(id == R.id.profile){
                    Toast.makeText(dashboard.this,"Profile Selected", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this, myprofile.class));
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
                    Intent i = new Intent(dashboard.this,Login.class);
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

    public void checkCreditCards(View view){


    }


}
