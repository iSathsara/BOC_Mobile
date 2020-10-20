package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;


public class ThirdPartyTransaction extends AppCompatActivity {

    Button transfer;
    EditText pay,source,amount,desc;
    int childCount;
    String descTxt;


    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_account_payment);

        // setting up toolbar
        Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Transactions");

        //get input data
        pay  = findViewById(R.id.pay_txt);
        source = findViewById(R.id.source_txt);
        amount = findViewById(R.id.amount_txt);
        desc = findViewById(R.id.desc_test);
        descTxt=desc.toString();

        transfer = findViewById(R.id.tp_cont_btn);

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

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(TextUtils.isEmpty(desc.getText())){
                    descTxt = "No Notes..!";
                }

                if(TextUtils.isEmpty(pay.getText())){
                   // Toast.makeText(ThirdPartyTransaction.this, "All fields required...", Toast.LENGTH_LONG).show();
                    pay.setError( "Pay field is required!" );
                }
                else if(TextUtils.isEmpty(source.getText())) {
                    source.setError( "Source field is required!" );
                }
                else if(TextUtils.isEmpty(amount.getText())){
                    amount.setError( "Amount field is required!" );
                }
                else {
                    //intent
                    Intent intent = new Intent(getBaseContext(), confirmThirdPatyTransaction.class);
                    intent.putExtra("pay", pay.getText().toString().trim());
                    intent.putExtra("source", source.getText().toString().trim());
                    intent.putExtra("amount", amount.getText().toString().trim());
                    intent.putExtra("desc", descTxt);
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

    // set logout function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
