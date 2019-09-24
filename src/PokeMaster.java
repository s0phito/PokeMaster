/**
 * Login screen
 */

package com.example.pokemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PokeMaster extends AppCompatActivity {

    private UserDBHelper database;
    private String login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemaster);

        database = new UserDBHelper(this);

        btnlistener();
    }

    public void btnlistener() {
        Button createbtn = (Button)findViewById(R.id.create);
        Button loginbtn = (Button)findViewById(R.id.login);
        final EditText usernameinput = (EditText) findViewById(R.id.username);
        final EditText passwordinput = (EditText) findViewById(R.id.password);
        
        createbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                create_btnHandler(view);
            }
        });
        
        loginbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                login_name = usernameinput.getText().toString().trim();
                String pass = passwordinput.getText().toString().trim();
                Boolean res = database.verifyUser(login_name, pass);

                if (res) {

                    Toast.makeText(PokeMaster.this, "Logging in...", Toast.LENGTH_SHORT).show();
                    login_btnHandler(view);
                }
                else
                    Toast.makeText(PokeMaster.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void create_btnHandler(View view){

        Intent intent = new Intent(PokeMaster.this, NewAccount.class);
        startActivity(intent);
    }

    public void login_btnHandler(View view){
        Intent intent = new Intent(PokeMaster.this, MainMenu.class);
        intent.putExtra("login_name", login_name);
        startActivity(intent);
    }
}
