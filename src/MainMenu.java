package com.example.pokemaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnlistener();
    }

    public void btnlistener() {
        Button CTA = (Button)findViewById(R.id.CTA);
        //Button PC = (Button)findViewById(R.id.PC);
        Button Mine = (Button)findViewById(R.id.MyPokemon);

        CTA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, CatchThemAll.class);
                intent.putExtra("login_name", getIntent().getStringExtra("login_name"));
                startActivity(intent);
            }
        });

//        PC.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(MainMenu.this, TypeCalculator.class);
//                intent.putExtra("login_name", getIntent().getStringExtra("login_name"));
//                startActivity(intent);
//            }
//        });
        Mine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, ViewCollection.class);
                intent.putExtra("login_name", getIntent().getStringExtra("login_name"));
                startActivity(intent);
            }
        });
    }
}
