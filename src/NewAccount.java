package com.example.pokemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

public class NewAccount extends AppCompatActivity {
    UserDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        helper = new UserDBHelper(this);

        newaccount_listener();
    }

    public void newaccount_listener() {

        Button signupbtn = (Button)findViewById(R.id.signup);
        final EditText usernameinput = (EditText) findViewById(R.id.username);
        final EditText passwordinput = (EditText) findViewById(R.id.password);
        final EditText confirminput = (EditText) findViewById(R.id.confirm);
        final EditText nicknameinput = (EditText) findViewById(R.id.nickname);
        final EditText birthdateinput = (EditText) findViewById(R.id.birthdate);
        final RadioGroup genderinput = (RadioGroup) findViewById(R.id.radiogroup);

        final String[] gender = new String[1];
        genderinput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb =(RadioButton)findViewById(checkedId);
                gender[0] = rb.getText().toString().trim();

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String user = usernameinput.getText().toString().trim();
                final String pass = passwordinput.getText().toString().trim();
                final String confirm = confirminput.getText().toString().trim();
                final String nick = nicknameinput.getText().toString().trim();
                final String birth = birthdateinput.getText().toString().trim();
                if (!pass.equals(confirm)){
                    Toast.makeText(NewAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                else if(user.isEmpty() || pass.isEmpty() || nick.isEmpty() || birth.isEmpty() || gender[0].isEmpty()){
                    Toast.makeText(NewAccount.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
                else if (!birth.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    Toast.makeText(NewAccount.this, "Incorrect format for birthday", Toast.LENGTH_SHORT).show();
                }
                else{
                    long userid = helper.insertUser(user, pass, nick, birth);
                    if (userid > 0) {
                        Toast.makeText(NewAccount.this, "User registration successful", Toast.LENGTH_SHORT).show();
                        signup_btnhandler(view);
                    }else{
                        Toast.makeText(NewAccount.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void signup_btnhandler(View view){
        Intent intent = new Intent(NewAccount.this, PokeMaster.class);
        startActivity(intent);
    }

}
