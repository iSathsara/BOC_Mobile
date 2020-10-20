package com.example.boc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {

    private Button loginbtn;
    EditText username;
    String uname;
    //database
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = (Button) findViewById(R.id.login);
        username =  findViewById(R.id.username);


        // go back to transaction menu screen
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenu();
            }
        });

    }
    private void MainMenu(){
        uname = username.getText().toString();
        Intent intent = new Intent(this, dashboard.class);
        intent.putExtra("uname",uname);
        startActivity(intent);
    }


}
