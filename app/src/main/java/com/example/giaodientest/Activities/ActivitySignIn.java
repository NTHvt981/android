package com.example.giaodientest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class ActivitySignIn extends AppCompatActivity
        implements View.OnClickListener, OnCompleteListener<AuthResult> {

    private Button btnSignIn;
    private Button btnGoToSignUp;
    EditText edtEmail, edtPassword;
    FirebaseAuth firebaseAuth;
    ProgressBar pbSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dang_nhap);

        setComponents();
        setEvents();
    }

    private void setEvents() {
        btnSignIn.setOnClickListener(this);
        btnGoToSignUp.setOnClickListener(this);
    }

    private void setComponents() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnGoToSignUp = findViewById(R.id.btnGoToSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        pbSignIn = findViewById(R.id.pbLoading);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSignIn:
                signIn();
                break;
            case R.id.btnGoToSignUp:
                goToSignUp();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null)
            goToDashboard();
    }

    private void signIn() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty())
        {
            Toast.makeText(getBaseContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty())
        {
            Toast.makeText(getBaseContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        pbSignIn.setVisibility(View.VISIBLE);
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this);
    }


    private void goToSignUp() {
        Intent intent = new Intent(ActivitySignIn.this, ActivitySignUp.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
//    direct user to dashboard, clear flag, set animation
    private void goToDashboard() {
//        Intent intent = new Intent(ActivitySignIn.this, ActivityDashboard.class);
        Intent intent = new Intent(getBaseContext(), ActivityDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
        finish();
    }


//    this function called when firebase auth finish signing user
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        pbSignIn.setVisibility(View.INVISIBLE);
        if (task.isSuccessful())
        {
            finish();
            goToDashboard();
        }
        else
        {
            String notiText = ZUltility.getNotifyException(task.getException());

            Toast.makeText(getBaseContext(), notiText, Toast.LENGTH_LONG).show();
            Log.e("Signin Error", "onCancelled", task.getException());
        }
    }
}