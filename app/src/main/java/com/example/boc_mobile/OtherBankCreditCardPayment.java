package com.example.boc_mobile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;


public class OtherBankCreditCardPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //private Button backToTransMenu;
    private Button continueButton;
    String uname;
    Spinner payFrom,payTo,paymentMethod;
    EditText amount,description;
    int check = 0;
    String from = "Select",to = "Select",method = "Select",pamount,des;

    String[] nameArray = new String[2];

    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_credit_card_payment);

        // setting up toolbar
        getSupportActionBar().setTitle("Transactions");

        //Get customer name from DB
        getCustomerName();

        //backToTransMenu = (Button) findViewById(R.id.obcp_cancel_btn);
        continueButton = (Button) findViewById(R.id.obcp_cont_btn);
        payFrom = findViewById(R.id.obcp_payFrom_spinner);
        payTo = findViewById(R.id.obcp_payTo_spinner);
        paymentMethod = findViewById(R.id.obcp_payMethod_spinner);
        amount = findViewById(R.id.obcp_amount_et);
        description = findViewById(R.id.obcp_desc_et);




        // go back to transaction menu screen
        /*backToTransMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        // go to confirm the transaction
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoConfirm();
            }
        });




        /**
         * add items to spinners
         */



        ArrayAdapter<CharSequence> payto = ArrayAdapter.createFromResource(this,R.array.payTo,android.R.layout.simple_spinner_item);
        payto.notifyDataSetChanged();
        payto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payTo.setAdapter(payto );
        payTo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);

        ArrayAdapter<CharSequence> method = ArrayAdapter.createFromResource(this,R.array.pMethod,android.R.layout.simple_spinner_item);
        method.notifyDataSetChanged();
        method.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethod.setAdapter(method );
        paymentMethod.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);

    }

    /**
     * method to goto confirm transaction
      */

    private void gotoConfirm(){


        pamount = amount.getText().toString();
        des = description.getText().toString();

       if(to.equals("Select") || from.equals("Select") || method.equals("Select") || pamount.equals("") || des.equals("")){

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


            Intent intent = new Intent(this, OtherBankCreditConfirm.class);
            intent.putExtra("to",to);
            intent.putExtra("from",from);
            intent.putExtra("amount",pamount);
            intent.putExtra("method",method);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                // implement function here
                //Toast.makeText(this, "Logout selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(++check>1) {

            int id = adapterView.getId();

            switch(id){

                case R.id.obcp_payTo_spinner:

                   to = adapterView.getSelectedItem().toString();

                    break;

                case R.id.obcp_payFrom_spinner:
                    from = adapterView.getSelectedItem().toString();
                    break;

                case  R.id.obcp_payMethod_spinner:
                    method = adapterView.getSelectedItem().toString();
                    break;


            }





        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void getCustomerName(){

        uname = SaveSharedPreference.getUserName(OtherBankCreditCardPayment.this);

        final ProgressDialog progress = new ProgressDialog(OtherBankCreditCardPayment.this,R.style.MyAlertDialogStyle);
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


                    if (nameArray.length > 0) {
                        ArrayAdapter nameSequence = new ArrayAdapter<>(OtherBankCreditCardPayment.this, android.R.layout.simple_list_item_1, nameArray);
                        nameSequence.notifyDataSetChanged();
                        nameSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payFrom.setAdapter(nameSequence);
                        payFrom.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);
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

