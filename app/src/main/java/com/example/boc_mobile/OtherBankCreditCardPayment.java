package com.example.boc_mobile;

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


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


//public class OtherBankCreditCardPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

public class OtherBankCreditCardPayment extends AppCompatActivity {

    private Button continueButton;
    String uname;
    Spinner payFrom,payTo,paymentMethod;
    EditText amount,description;
    int check = 0;
    String from,to,method,pamount,des;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_credit_card_payment);

        // setting up toolbar
        //Toolbar trans_toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("BOC Mobile Banking - Transactions");

        // Buttons
        continueButton = (Button) findViewById(R.id.obcp_cont_btn);

        // input fields
        payFrom = findViewById(R.id.obcp_payFrom_spinner);
        payTo = findViewById(R.id.obcp_payTo_spinner);
        paymentMethod = findViewById(R.id.obcp_payMethod_spinner);
        amount = findViewById(R.id.obcp_amount_editText);
        description = findViewById(R.id.obcp_desc_editText);




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

        //uname = getIntent().getStringExtra("accNo");

        /*

        //add items to spinners
        ArrayAdapter<CharSequence> nameSequence = ArrayAdapter.createFromResource(this,R.array.customerIsuru,android.R.layout.simple_spinner_item);
        nameSequence.notifyDataSetChanged();
        nameSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payFrom.setAdapter(nameSequence);
        payFrom.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) OtherBankCreditCardPayment.this);

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

        */

    }

    // method to goto confirm transaction
    private void gotoConfirm(){

        //pamount = amount.getText().toString();
        //des = description.getText().toString();

        Intent intent = new Intent(this, OtherBankCreditConfirm.class);

        //intent.putExtra("accountNo",uname);
        //intent.putExtra("to",to);
        //intent.putExtra("from",from);
        //intent.putExtra("amount",pamount);
        //intent.putExtra("method",method);
        //intent.putExtra("description",des);

        startActivity(intent);
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


    /*
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
    */


    /*
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    */


}

