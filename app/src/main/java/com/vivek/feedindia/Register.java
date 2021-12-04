package com.vivek.feedindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Register extends AppCompatActivity {
    private static final String TAG = ">";
    private EditText ETemail;
    private EditText ETpassword;
    private FirebaseAuth mAuth;
    EditText mFullName,  mPhoneNO;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    CircularProgressButton mRegisterBtn;
    ProgressBar progressBAr;
    FirebaseFirestore fStore;
    TextView back;
    String userID;

    protected void registerUser(){
        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();
        String name = mFullName.getText().toString().trim();
        String phone = mPhoneNO.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            mFullName.setError("Full name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            ETemail.setError("email is required");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            mPhoneNO.setError("phone number is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ETpassword.setError("password is required");
            return;
        }
        if (password.length() < 6) {
            ETpassword.setError("password must be greater than 5 characters");
            return;
        }
        progressBAr.setVisibility(View.VISIBLE);
        createUser();
    }

    protected void createUser(){

        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();
        String phone = mPhoneNO.getText().toString().trim();
        String name = mFullName.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Register.this, "user created", Toast.LENGTH_SHORT).show();
                userID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("fname", name);
                user.put("email", email);
                user.put("phone", phone);
                user.put("password", password);
                documentReference.set(user).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess:user Profile is created for " + userID)).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                progressBAr.setVisibility(View.GONE);
            } else {
                Toast.makeText(Register.this, "error" + task.getException(), Toast.LENGTH_SHORT).show();
                progressBAr.setVisibility(View.GONE);
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setStatusBarColor(Color.parseColor("#E86D6D"));
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ETemail = (EditText)findViewById(R.id.email);
        ETpassword = (EditText)findViewById(R.id.password);

        mFullName = findViewById(R.id.editTextName);
        mPhoneNO = findViewById(R.id.editTextMobile);
        progressBAr = findViewById(R.id.progressBar2);

        back = findViewById(R.id.dsf);
        fStore = FirebaseFirestore.getInstance();

        CircularProgressButton register = (CircularProgressButton) findViewById(R.id.cirRegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,FirstActivity.class);
                startActivity(intent);
            }
        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
