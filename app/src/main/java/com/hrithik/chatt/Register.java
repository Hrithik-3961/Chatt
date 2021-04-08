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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private TextView signin;
    private EditText register_name, register_email, register_password, register_confirmPassword;
    private Button register_btn;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        register_name = findViewById(R.id.name);
        register_email = findViewById(R.id.emailId);
        register_password = findViewById(R.id.password);
        register_confirmPassword = findViewById(R.id.confirmPassword);
        register_btn = findViewById(R.id.register_btn);
        signin = findViewById(R.id.signin);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = register_name.getText().toString();
                final String email = register_email.getText().toString();
                String password = register_password.getText().toString();
                String confirmPassword = register_confirmPassword.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    register_name.setError("Please enter valid name");
                    return;
                } else
                    register_name.setError(null);

                if (!email.matches(regex) || TextUtils.isEmpty(email)) {
                    register_email.setError("Invalid email id");
                    return;
                } else
                    register_email.setError(null);

                if (!password.equals(confirmPassword)) {
                    register_password.setError("Password does not match");
                    register_confirmPassword.setError("Password does not match");
                    return;
                } else {
                    register_password.setError(null);
                    register_confirmPassword.setError(null);
                }

                if (password.length() < 6) {
                    register_password.setError("Password should be at least 6 characters");
                    return;
                } else
                    register_password.setError(null);

                progressDialog.show();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference reference = database.getReference().child("user").child(auth.getCurrentUser().getUid());
                            final Users user = new Users(auth.getCurrentUser().getUid(), name, email);
                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful())
                                        startActivity(new Intent(Register.this, HomeActivity.class));
                                    else
                                        Toast.makeText(Register.this, "Unable to Register user", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Unable to Register user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, SignIn.class));
                finishAffinity();
            }
        });
    }
}