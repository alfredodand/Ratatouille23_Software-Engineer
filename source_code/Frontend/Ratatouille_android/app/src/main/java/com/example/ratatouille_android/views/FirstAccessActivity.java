package com.example.ratatouille_android.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.FirstAccessController;
import com.example.ratatouille_android.models.User;

public class FirstAccessActivity extends AppCompatActivity {
    private FirstAccessController firstAccessController;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_access);
        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");
        firstAccessController = new FirstAccessController(this, loggedUser);
        Button confermaPasswordButton = findViewById(R.id.confermaPasswordButton);
        EditText nuovaPasswordInput =  findViewById(R.id.newPasswordField);

        View.OnClickListener firstAccessOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView errorLable = findViewById(R.id.errorLable);
                String password = nuovaPasswordInput.getText().toString();
                if((password.equals("")) || (password.length() > 29)) {
                    if (password.equals(""))
                        errorLable.setText("Non è possibile lasciare vuoto il campo");
                    if (password.length() > 29)
                        errorLable.setText("Inserire una password < 30 caratteri");
                }else{
                    firstAccessController.updatePassword(password);
                }
            }
        };

        confermaPasswordButton.setOnClickListener(firstAccessOnClickListener);

    }
}