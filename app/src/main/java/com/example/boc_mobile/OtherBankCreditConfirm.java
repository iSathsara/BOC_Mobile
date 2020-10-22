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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OtherBankCreditConfirm extends AppCompatActivity {

    private Button confirmBtn;
    //private Button cancelBtn;
    String uname;
    String amount;
    TextView amountDetails,To,From,Method,Description;
    int balance;
    String from,to,method,pamount,des;


    //Database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_credit_confirm);



        uname =  SaveSharedPreference.getUserName(OtherBankCreditConfirm.this);
        from = getIntent().getStringExtra("from");
        to= getIntent().getStringExtra("to");
        method = getIntent().getStringExtra("method");
        pamount = getIntent().getStringExtra("amount");
        des = getIntent().getStringExtra("description");

        // setting up toolbar
        Toolbar trans_toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(trans_toolbar);
        getSupportActionBar().setTitle("Transactions");

        confirmBtn = findViewById(R.id.obcp_prcd_btn);
        //cancelBtn = findViewById(R.id.obcp_prcd_cancel_btn);
        amountDetails = findViewById(R.id.obcp_amount_detail);
        amountDetails.setText(pamount);
        amount = amountDetails.getText().toString();

        To = findViewById(R.id.obcp_payto_detail);
        To.setText(to);
        From = findViewById(R.id.obcp_payfrom_detail);
        From.setText(from);
        Method = findViewById(R.id.obcp_method_detail);
        Method.setText(method);
        Description = findViewById(R.id.obcp_desc_detail);
        Description.setText(des);



        /*cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        }); */

        // set onclick listener to Continue button
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*
                  Update balance
                 */
                Query query = dbRef.child("User").orderByChild("uname").equalTo(uname);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            String bal;
                            int newBal;


                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                String child = issue.getKey();
                                bal = issue.child("balance").getValue().toString();



                                balance =Integer.parseInt(bal);
                                newBal = balance-Integer.parseInt(amount);
                                String x = Integer.toString(newBal);


                                dbRef.child("User").child(child).child("balance").setValue(newBal);


                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                /*
                  Save details
                 */
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("CrediCardPayments");

                CreditCardPayments payments = new CreditCardPayments();

                payments.setUname(uname);
                payments.setPayee(to);
                payments.setCustomerName(from);
                payments.setAmount(Integer.parseInt(pamount));
                payments.setDescription(des);
                payments.setMethod(method);

                db.push().setValue(payments);

                AlertDialog.Builder alert = new AlertDialog.Builder(OtherBankCreditConfirm.this);

                alert.setTitle("SUCCESSFUL");
                alert.setIcon(R.drawable.transaction_okay);
                alert.setMessage("The amount is transferred successfully");
                alert.setPositiveButton("DONE", null);
                alert.setNegativeButton("Another Transaction", null);

                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_design);

                // this will change the default behaviour of buttons
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // redirect to dashboard
                        Intent i = new Intent(OtherBankCreditConfirm.this,dashboard.class);
                        i.putExtra("uname",uname);
                        startActivity(i);
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
        });

    }


    // intent for launch other transaction
    private void launchOtherTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // intent for back to form
    private void goBack(){
        Intent intent = new Intent(this, OtherBankCreditCardPayment.class);
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
                Toast.makeText(this, "Logout selected", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
