package com.vivek.feedindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";
    private FirebaseAuth mAuth;
    private EditText ETemail;
    private EditText ETpassword;
    ProgressBar progressBar1;
    TextView forgotText;
    long maxid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setStatusBarColor(Color.parseColor("#E86D6D"));
        setContentView(R.layout.activity_first);



        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        }

        ETemail = (EditText)findViewById(R.id.editText);
        ETpassword = (EditText)findViewById(R.id.editText2);
        forgotText = (TextView) findViewById(R.id.Forget);
        progressBar1 =  findViewById(R.id.progressBar1);

        forgotText.setOnClickListener(v -> {
            final EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordRestDialog = new AlertDialog.Builder(v.getContext());
            passwordRestDialog.setMessage("Enter your Email to received Reset Link.");
            passwordRestDialog.setView(resetMail);

            passwordRestDialog.setPositiveButton("Yes", (dialog, which) -> {
                //extract the mail and send reset link
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(FirstActivity.this,"Reset Link send to your Email",Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(FirstActivity.this,"Error! Reset Link is Not send" +e.getMessage(),Toast.LENGTH_SHORT).show());
            });
            passwordRestDialog.setNegativeButton("NO", (dialog, which) -> {
                //closing Dialog
            });
            passwordRestDialog.create().show();
        });

        final CircularProgressButton signIn = (CircularProgressButton) findViewById(R.id.sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInIntoApp();
            }
        });

        TextView signUp = (TextView) findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(FirstActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }

    private void signInIntoApp(){
        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this," Invalid Email ",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this," Invalid Password ",Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(FirstActivity.this, DashBoard.class);
                            startActivity(intent);
                            progressBar1.setVisibility(View.GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(FirstActivity.this," Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

}