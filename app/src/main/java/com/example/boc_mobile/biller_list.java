package com.example.boc_mobile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;

public class biller_list extends AppCompatActivity {

    String[] strArray = new String[] {"one","two","three","four","5","6","7","8","9","10"};
    ArrayList<String> billerList = new ArrayList<>();
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    Button refresh,cancel,cancelPopup,delete;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    Dialog popup;
    TextView bName,accout;
    String billerName,accountNo,uname;
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biller_list);


        uname = SaveSharedPreference.getUserName(biller_list.this);

        //---related to popup---------------
        popup = new Dialog(this);
        popup.setContentView(R.layout.biller_details);
        cancelPopup = popup.findViewById(R.id.popupCancel);
        delete = popup.findViewById(R.id.delete);
        bName = popup.findViewById(R.id.billerName);
        accout = popup.findViewById(R.id.accNo);
        popup.setCanceledOnTouchOutside(false);

        progress = new ProgressDialog(biller_list.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();




        cancel = findViewById(R.id.exit);


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
                    Intent i = new Intent(biller_list.this,dashboard.class);
                    startActivity(i);
                    drawer.closeDrawers();

                }else if(id == R.id.transaction){
                    Intent i = new Intent(biller_list.this,MainActivity.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                else if(id == R.id.profile){
                    startActivity(new Intent(biller_list.this, UserProfile.class));
                    drawer.closeDrawers();
                }

                else if(id == R.id.more){
                    Intent i = new Intent(biller_list.this,moreActivityFunction.class);
                    startActivity(i);
                    drawer.closeDrawers();
                }
                return true;
            }
        });



        //--get data from db---------------
        Query query = dbRef.child("Biller").orderByChild("uname").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot bill : dataSnapshot.getChildren()) {
                        String biller = bill.child("biller").getValue().toString();
                        billerList.add(biller);



                    }

                    if(billerList.size()> 0){

                        progress.dismiss();
                    }
                    //----set data in list view--------

                    final ArrayAdapter adapter = new ArrayAdapter<String>(biller_list.this,
                            R.layout.biller_listview,billerList);
                    adapter.notifyDataSetChanged();
                    final ListView listView = (ListView) findViewById(R.id.biller_list);
                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object i = listView.getItemAtPosition(position);
                           billerName= i.toString();





                            //-------get details from db-----------------
                            final Query query1 = dbRef.child("Biller").orderByChild("uname").equalTo(uname);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){

                                        for(DataSnapshot ds:dataSnapshot.getChildren()){

                                            String bill = ds.child("biller").getValue().toString();

                                            if(bill.equals(billerName)){

                                                accountNo = ds.child("accNo").getValue().toString();
                                                bName.setText(billerName);
                                                accout.setText(accountNo);
                                                break;
                                            }

                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });






                                                popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popup.show();


                        }
                    });







                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
    @Override
      public void onClick(View view) {

        final Query query1 = dbRef.child("Biller").orderByChild("accNo").equalTo(accountNo);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                        String c = ds.getKey();

                        dbRef = FirebaseDatabase.getInstance().getReference().child("Biller").child(c);
                        dbRef.removeValue();

                        Toast.makeText(biller_list.this, "Biller Removed Succesfull", Toast.LENGTH_LONG).show();


                        popup.dismiss();


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       }
     });



        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(biller_list.this,addBiller.class);
                startActivity(i);
            }
        });
    }



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

            AlertDialog.Builder alert = new AlertDialog.Builder(biller_list.this);

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

                    Intent i = new Intent(biller_list.this, Login.class);
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

    public void refresh(View view){

        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popup.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();

        progress.dismiss();
    }
}
