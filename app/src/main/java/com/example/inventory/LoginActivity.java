package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private CheckBox rememberMe;
    private Button loginButton;
    private TextView forgotPassword, signUpNow;
    private ImageView showHidePassword; // ImageView for password visibility toggle
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);


        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.remember_me);
        loginButton = findViewById(R.id.login_btn);
        forgotPassword = findViewById(R.id.password_recovery);
        signUpNow = findViewById(R.id.tv_signup);
        showHidePassword = findViewById(R.id.show_hide_password); // Find the ImageView for show/hide password

        // Handle password visibility toggle
        showHidePassword.setOnClickListener(v -> {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                // Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHidePassword.setImageResource(R.drawable.ic_visibility); // Change icon to show password
            } else {
                // Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showHidePassword.setImageResource(R.drawable.ic_visibility_off); // Change icon to hide password
            }
            // Move cursor to the end of the password field
            password.setSelection(password.length());
        });

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String userEmail = username.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt Firebase login
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login success, navigate to the next screen (MainActivity or Dashboard)
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Handle forgot password
        forgotPassword.setOnClickListener(v -> {
            // Handle forgot password logic here, possibly navigating to a password reset screen
            Toast.makeText(LoginActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
        });

        // Handle "Sign Up Now" click
        signUpNow.setOnClickListener(v -> {
            // Navigate to SignupActivity when the TextView is clicked
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}

