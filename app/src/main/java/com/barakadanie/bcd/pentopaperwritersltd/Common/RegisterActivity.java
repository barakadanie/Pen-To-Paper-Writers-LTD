package com.barakadanie.bcd.pentopaperwritersltd.Common;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barakadanie.bcd.pentopaperwritersltd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
ImageView back;
TextView backtext;
Button register;
EditText email,name,phone,password;
FirebaseAuth auth;
private FirebaseDatabase mFirebaseDatabase;
private DatabaseReference mDatabaseReference;
FirebaseFirestore fStore;
String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back=findViewById(R.id.backB);
        backtext=findViewById(R.id.backText);
        email=findViewById(R.id.email);
        name=findViewById(R.id.names);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        back.setOnClickListener(view -> {
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        register.setOnClickListener(v -> {
            // Get the registration data from the UI components
            String names = name.getText().toString().trim();
            String emailaddress = email.getText().toString().trim();
            String phonenumber = phone.getText().toString().trim();
            String userpassword = password.getText().toString().trim();

            // Validate the registration data

            if (TextUtils.isEmpty(names)) {
                name.setError("Please enter your full name");
                return;
            }
            if (TextUtils.isEmpty(phonenumber)) {
                phone.setError("Please enter your phone number");
                return;
            }
            if (TextUtils.isEmpty(emailaddress)) {
                email.setError("Please enter your email address");
                return;
            }
            if (TextUtils.isEmpty(userpassword)) {
                password.setError("Please enter a password");
                return;
            }

            // Register the user with Firebase
            auth.createUserWithEmailAndPassword(emailaddress,userpassword).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT) .show();
                    userID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("FullNames",names);
                    user.put("Email Address",email);
                    user.put("Phone Number",phone);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG,"onSuccess: Profile is created for "+ userID);
                            Toast.makeText(RegisterActivity.this, "Registered Successfully.", Toast.LENGTH_SHORT) .show();
                        }
                    });
                    //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}