package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.app_common_activities.ChooseRole;

public class Login extends AppCompatActivity {

    EditText userName,password;
    AppCompatButton loginBtn,cancelBtn;
    TextView goToRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = findViewById(R.id.userLoginUserName);
        password = findViewById(R.id.userLoginPassword);
        loginBtn = findViewById(R.id.userLoginBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        goToRegisterLink = findViewById(R.id.regPageLink);

        //login button logic
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //cancel button logic
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this statement set user back to choose role page
                Intent intent = new Intent(Login.this, ChooseRole.class);
                startActivity(intent);
                finish();
            }
        });


        //link logic
        goToRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, UserRegisteration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}