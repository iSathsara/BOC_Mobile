package com.example.boc_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class Login extends AppCompatActivity {

    private Button loginbtn;
    private Button faq;

    EditText username,password;
    String uname,pwd;


    //database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = (Button) findViewById(R.id.login);
        faq = (Button) findViewById(R.id.faq_btn);
        username =  findViewById(R.id.username);
        password = findViewById(R.id.password);


        // go back to transaction menu screen
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenu();
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faq();
            }
        });

    }

    private void faq(){
        String url = "https://online.boc.lk/terms/boc/FAQ.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }



    private void MainMenu(){

        uname = username.getText().toString();
        pwd = password.getText().toString();

        if(uname.equals("")){

            username.requestFocus();
            username.setError("FIELD CANNOT BE EMPTY");

        }else if(pwd.equals("")){

            password.requestFocus();
            password.setError("FIELD CANNOT BE EMPTY");

        }else{


            SaveSharedPreference.setUserName(Login.this,uname);
            /*
              Progress dialog
             */
            final ProgressDialog progress = new ProgressDialog(Login.this,R.style.MyAlertDialogStyle);
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
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {


                              String pass = issue.child("password").getValue().toString();

                              if( pass.equals(pwd)){

                                  SaveSharedPreference.setUserName(Login.this,uname);

                                  progress.dismiss();
                                  Intent intent = new Intent(Login.this, dashboard.class);
                                  startActivity(intent);

                                  break;

                              }else{

                                  progress.dismiss();


                                  /*
                                    Wrong username or password msg
                                   */

                                  AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);

                                  dlgAlert.setMessage("Incorrect username or password");
                                  dlgAlert.setTitle("Error");
                                  dlgAlert.setIcon(R.drawable.empty_warning);
                                  dlgAlert.setPositiveButton("OK", null);
                                  dlgAlert.setCancelable(false);
                                  dlgAlert.create().show();

                                  dlgAlert.setPositiveButton("Ok",
                                          new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog, int which) {

                                              }
                                          });

                              }



                        }


                    }else{


                        progress.dismiss();
                                  /*
                                    Wrong username or password msg
                                   */

                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);

                        dlgAlert.setMessage("Incorrect username or password");
                        dlgAlert.setIcon(R.drawable.empty_warning);
                        dlgAlert.setTitle("Error");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();

                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }







    }


}
