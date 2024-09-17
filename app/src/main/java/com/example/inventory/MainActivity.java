package com.example.inventory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, signUpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        signUpButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignupActivity.class)));



;
    }
}
