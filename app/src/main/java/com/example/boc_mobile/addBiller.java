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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class addBiller extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    Spinner custom,billCat,biller,billerType;
    EditText accNo;
    int flag;
    int check = 0;
    String[] billerArray = {"Select"};
    String customerName,billerCategory,billerName,type,account;
    Button viewBiller,next,cancel;
    ProgressDialog progress;

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();





   // final ListView listView = (ListView) findViewById(R.id.mobile_list);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_biller);

        accNo = findViewById(R.id.accNo);
        custom = findViewById(R.id.customer);
        billCat = findViewById(R.id.category);
        biller = findViewById(R.id.biller);
        billerType = findViewById(R.id.type1);
        viewBiller = findViewById(R.id.viewBiller);
        cancel = findViewById(R.id.popupCancel);
        customerName = "Select";
        billerCategory = "Select";
        type = "Select";


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

                    drawer.closeDrawers();
                }
                return true;
            }
        });



        //add items to spinners
        ArrayAdapter<CharSequence> billerSequence = ArrayAdapter.createFromResource(this,R.array.biller_category,android.R.layout.simple_spinner_item);
        billerSequence.notifyDataSetChanged();
        billerSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billCat.setAdapter(billerSequence);
       billCat.setOnItemSelectedListener(addBiller.this);


        ArrayAdapter<CharSequence> customerSequence = ArrayAdapter.createFromResource(this,R.array.customer,android.R.layout.simple_spinner_item);
        customerSequence.notifyDataSetChanged();
        customerSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        custom.setAdapter(customerSequence);
        custom.setOnItemSelectedListener(addBiller.this);


        ArrayAdapter<CharSequence> billType = ArrayAdapter.createFromResource(this,R.array.billerType,android.R.layout.simple_spinner_item);
        billType.notifyDataSetChanged();
        billType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerType.setAdapter(billType);
        billerType.setOnItemSelectedListener(addBiller.this);

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

    public void viewBiller(View view){

        Intent i = new Intent(addBiller.this,biller_list.class);
        startActivity(i);
    }

    public void nextButtonClick(View view){



       account = accNo.getText().toString();
        if(!customerName.equals("Select") && !billerCategory.equals("Select") && !account.equals("") && !type.equals("Select")){

            Intent i = new Intent(addBiller.this,addBiller2.class);
            i.putExtra("customer",customerName);
            i.putExtra("category",billerCategory);
            i.putExtra("biller",billerName);
            i.putExtra("account",account);
            i.putExtra("type",type);
            startActivity(i);
       }else{
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please fill required fields");
            dlgAlert.setIcon(R.drawable.empty_warning);
            dlgAlert.setTitle("Alert!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }



    }

    public void CancelButtonclick(View view){

        Intent i = new Intent(addBiller.this,billPayment.class);
        startActivity(i);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(++check>1) {

            int id = adapterView.getId();

            switch(id){

                case R.id.category:
                    String item = adapterView.getSelectedItem().toString();
                    billerCategory = adapterView.getSelectedItem().toString();
                    if(!item.equals("Select")){
                       // Toast.makeText(addBiller.this,"3", Toast.LENGTH_LONG).show();
                        getBillerNames(item);
                        progress = new ProgressDialog(addBiller.this,R.style.MyAlertDialogStyle);
                        progress.setTitle("Loading");
                        progress.setMessage("Wait while loading...");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();
                    }
                    break;

                case R.id.customer:
                    customerName = adapterView.getSelectedItem().toString();
                    break;

                case  R.id.biller:
                    billerName = adapterView.getSelectedItem().toString();
                    break;

                case R.id.type1:
                    type =  adapterView.getSelectedItem().toString();
                    break;
            }





        }

        //Toast.makeText(addBiller.this, item, Toast.LENGTH_LONG).show();

            //Toast.makeText(addBiller.this, item, Toast.LENGTH_LONG).show();
           // Toast.makeText(addBiller.this, item, Toast.LENGTH_LONG).show();






    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //-----get biller names from the db---------


    public void getBillerNames(String category){
       // Toast.makeText(addBiller.this,category, Toast.LENGTH_LONG).show();
           Query query = dbRef.child("BillerList").orderByChild("category").equalTo(category);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String x = "";
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            x = issue.child("biller").getValue().toString();
                            break;
                        }

                        billerArray = x.split(",");
                        if(billerArray.length > 0){
                            progress.dismiss();
                        }
                        List<String> list = Arrays.asList(billerArray);
                        ArrayAdapter biller_list = new ArrayAdapter<>(addBiller.this, android.R.layout.simple_list_item_1, list);
                        biller_list.notifyDataSetChanged();
                        biller_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        biller.setAdapter(biller_list);
                        // biller.setEnabled(true);
                        biller.setOnItemSelectedListener(addBiller.this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

       // Toast.makeText(addBiller.this,  "4", Toast.LENGTH_LONG).show();





    }

}
