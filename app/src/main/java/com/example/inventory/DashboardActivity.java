

package com.example.inventory; // Change this to your actual package name

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Ensure this matches your layout file

        // Find the TextView by ID and set some text
        TextView textView = findViewById(R.id.textViewMessage);
        textView.setText("Welcome to the Dashboard!");

        // Add any other initialization code here
    }
}
