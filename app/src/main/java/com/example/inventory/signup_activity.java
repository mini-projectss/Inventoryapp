package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signup_activity extends AppCompatActivity {

    EditText edname, edmobileno, edmail,edpass,edconpass;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        edname=findViewById(R.id.username);
        edmobileno=findViewById(R.id.mobileno);
        edmail=findViewById(R.id.email);
        edpass=findViewById(R.id.password);
        edconpass=findViewById(R.id.repassword);
        btn=findViewById(R.id.signupbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edname.getText().toString();
                String password=edpass.getText().toString();
                String cpass=edconpass.getText().toString();
                String mail=edmail.getText().toString();
                String mobile=edmobileno.getText().toString();
                if(name.length()==0||password.length()==0||cpass.length()==0||mail.length()==0||mobile.length()==0){
                    Toast.makeText(getApplicationContext(),"Please fill all details",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!password.equals(cpass)) {
                        Toast.makeText(getApplicationContext(), "Password and confirm password must be same", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        startActivity(new Intent(signup_activity.this, login_activity.class
                        ));
                    }
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}