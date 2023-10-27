package com.example.ratatouille_android.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.LoginController;
import com.example.ratatouille_android.views.jfragment.HomeFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Observable;
import java.util.Observer;


public class LoginActivity extends AppCompatActivity {
    private TextView error, underLoginButton;
    private LoginController loginController;
    private Button btn;
    private EditText emailField, passwordField;
    private FirebaseAnalytics analytics = MainActivity.analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseLog();

        error = findViewById(R.id.error);
        underLoginButton = findViewById(R.id.login_under_button);
        btn = findViewById(R.id.login_button);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        setUnderlineOnLoginText();

        TextView underSignupButton = findViewById(R.id.sign_up_under_button);
        View.OnClickListener onClickListenerSignup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpActivity();
            }
        };


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(emailField.getText());
                String password = String.valueOf(passwordField.getText());
                loginController = new LoginController(LoginActivity.this, email, password, error);
                loginController.requestToServer();

            }
        };

        underSignupButton.setOnClickListener(onClickListenerSignup);
        btn.setOnClickListener(onClickListener);
    }

    private void firebaseLog() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "login");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "logged")  ;
        analytics.logEvent("login", bundle);
    }

    private void setUnderlineOnLoginText() {
        SpannableString content = new SpannableString("Login");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        underLoginButton.setText(content);
    }

    public void goToSignUpActivity(){
        Intent switchActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(switchActivityIntent);
        this.finish();
    }

}
