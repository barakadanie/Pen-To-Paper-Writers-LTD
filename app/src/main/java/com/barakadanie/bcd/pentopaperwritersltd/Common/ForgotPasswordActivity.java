package com.barakadanie.bcd.pentopaperwritersltd.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.barakadanie.bcd.pentopaperwritersltd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    MaterialButton reset;
    FirebaseAuth auth;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        reset=findViewById(R.id.reset);
        email=findViewById(R.id.email);
        auth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();

                // Validate the email address
                if (TextUtils.isEmpty(mail)) {
                    email.setError("Please enter your email address");
                    return;
                }
                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Password reset email sent successfully
                                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                    // Navigate to the login screen
                                    Intent intent=new Intent(view.getContext(),SentLinkActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Password reset email failed to send
                                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email failed to send.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}