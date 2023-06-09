package com.barakadanie.bcd.pentopaperwritersltd.Common;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barakadanie.bcd.pentopaperwritersltd.Admin.AdminActivity;
import com.barakadanie.bcd.pentopaperwritersltd.R;
import com.barakadanie.bcd.pentopaperwritersltd.User.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mPassword;
    EditText mEmail;
    Button mlogin;
    TextView mforgerpassword, tvLogin;
    TextView mCreateBtn;
    FirebaseAuth fauth;
    ProgressBar mprogresspar;
    private static final int RC_SIGN_IN = 9001;
    ImageButton fb,goog;
    GoogleSignInOptions mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        fauth = FirebaseAuth.getInstance();
        mlogin = (Button) findViewById(R.id.login);
        mCreateBtn = findViewById(R.id.register);
        goog=findViewById(R.id.google);
        mforgerpassword = (TextView) findViewById(R.id.forgotpassword);
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                . requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        if (fauth.getCurrentUser() != null) {
            if (fauth.getCurrentUser().getEmail().equals("admin@gmail.com")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

        }

        mlogin.setOnClickListener(view -> {
            final String Email = mEmail.getText().toString().trim();
            final String Password = mPassword.getText().toString();
            if (TextUtils.isEmpty(Email)) {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(Password)) {
                mPassword.setError("Password is required");
                return;
            }
            if (Password.length() < 6) {
                mPassword.setError("Password must be bigger than or equal 6 characters");
                return;
            }
            // progress in background and i make it here visible.
            mprogresspar.setVisibility(View.VISIBLE);

            // Authenticate
            fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (Email.equals("admin@gmail.com") && Password.equals("password")) {
                        Toast.makeText(LoginActivity.this, "Welcome My Admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong User name Or Password", Toast.LENGTH_SHORT).show();
                    mprogresspar.setVisibility(View.GONE);
                }
            });

        });
        mCreateBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
           startActivity(intent);
        });

        mforgerpassword.setOnClickListener(view -> {
            // Here we will send a verification message
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
        goog.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = fauth.getCurrentUser();
                            // Open main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            try {
                // Google Sign In was successful, authenticate with Firebase
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
}