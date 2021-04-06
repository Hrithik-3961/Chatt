package com.hrithik.chatt;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private TextView register;
    private EditText signin_emailId, signin_password;
    private Button signin_btn;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    private String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        signin_emailId = findViewById(R.id.emailId);
        signin_password = findViewById(R.id.password);
        signin_btn = findViewById(R.id.signin_btn);
        register = findViewById(R.id.register);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signin_emailId.getText().toString();
                String password = signin_password.getText().toString();

                if (!email.matches(regex) || TextUtils.isEmpty(email)) {
                    signin_emailId.setError("Invalid Email Id");
                    return;
                } else
                    signin_emailId.setError(null);

                if (TextUtils.isEmpty(password)) {
                    signin_password.setError("Invalid Password");
                    return;
                } else
                    signin_password.setError(null);

                progressDialog.show();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignIn.this, HomeActivity.class));
                            finishAffinity();
                        } else
                            Toast.makeText(SignIn.this, "Unable to Sign In", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, Register.class));
                finishAffinity();
            }
        });
    }
}