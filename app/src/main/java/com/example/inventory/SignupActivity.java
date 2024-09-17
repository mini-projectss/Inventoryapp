package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class SignupActivity extends AppCompatActivity {

    private EditText fullName, email, mobileNumber, password, rePassword;
    private Button signButton; // Corrected line
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.repassword);
        signButton = findViewById(R.id.sign_btn);


        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String confirmPassword = rePassword.getText().toString().trim();

                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userPassword.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user in Firebase
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Send verification email
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "Registered successfully. Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                } else {
                                                    String error = ((FirebaseAuthException) task1.getException()).getMessage();
                                                    Toast.makeText(SignupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            } else {
                                String error = ((FirebaseAuthException) task.getException()).getMessage();
                                Toast.makeText(SignupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
