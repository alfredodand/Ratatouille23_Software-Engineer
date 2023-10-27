package com.example.ratatouille_android.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.SignUpController;

public class SignUpActivity extends AppCompatActivity {
    private SignUpController signUpController;
    private TextView error, underSignUpButton, underLoginButton;
    private EditText emailInput, ristoranteInput;
    private Button verificaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpController = new SignUpController(this);

        underSignUpButton = findViewById(R.id.sign_up_under_button2);
        underLoginButton = findViewById(R.id.login_under_button2);
        emailInput = findViewById(R.id.email_field3);
        ristoranteInput = findViewById(R.id.ristorante_field);
        verificaButton = findViewById(R.id.verifica_button);
        error = findViewById(R.id.errorVerifica);
        setUnderlineOnSignupText();

        View.OnClickListener onClickListenerVerifica = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String nomeRistorante = ristoranteInput.getText().toString();
                signUpController.verifyAccount(email, nomeRistorante);
            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        };

        verificaButton.setOnClickListener(onClickListenerVerifica);
        underLoginButton.setOnClickListener(onClickListener);
    }

    private void setUnderlineOnSignupText() {
        SpannableString content = new SpannableString("Account");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        underSignUpButton.setText(content);
    }

    public void goToLoginActivity(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
        this.finish();
    }

    public void setErrorVerificaOnError(){
        error.setText("L'account non è ancora stato creato,\n contattare l'amministratore del ristorante");
        error.setTextColor(Color.RED);
    }

    public void setErrorVerificaOnSuccess(){
        error.setText("L'account è presente nel database");
        error.setTextColor(Color.GREEN);
    }

}