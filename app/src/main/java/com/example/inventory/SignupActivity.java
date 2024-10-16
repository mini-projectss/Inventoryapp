package com.example.inventory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

     EditText fullName, email, mobileNumber, password, rePassword;
     Button signButton;
     FirebaseAuth mAuth;
     TextView loginNow;
     FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.repassword);
        signButton = findViewById(R.id.sign_btn);
        loginNow = findViewById(R.id.login_Now);

        // Navigate to LoginActivity when the loginNow TextView is clicked
        loginNow.setOnClickListener(visib -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Handle the sign-up process
        signButton.setOnClickListener(v -> {
            String username = fullName.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String usermobileNumber = mobileNumber.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String confirmPassword = rePassword.getText().toString().trim();
            String userName = fullName.getText().toString().trim();


            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(usermobileNumber) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(userName)) {
                Toast.makeText(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!userPassword.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register the user in Firebase Authentication
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
                                                storeUserData(userName, userEmail,usermobileNumber ,user.getUid());  // Call Firestore to store user data
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            } else {
                                                String error = task1.getException().getMessage();
                                                Toast.makeText(SignupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(SignupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                        }
                    });

        });
    }
    private void storeUserData(String name, String email,String mobile, String userId) {
        // Create a new user object with the details
        Map<String, Object> user = new HashMap<>();
        user.put("Username", name);
        user.put("email", email);
        user.put("mobile no.", mobile);
        user.put("userId", userId);

        // Add the user data to Firestore under the "users" collection
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // User data added successfully
                    Toast.makeText(SignupActivity.this, "User data stored successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error storing user data
                    Toast.makeText(SignupActivity.this, "Error storing user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
