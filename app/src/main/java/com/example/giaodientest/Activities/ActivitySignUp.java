package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivitySignUp extends AppCompatActivity
        implements View.OnClickListener, OnCompleteListener<AuthResult> {
    private Button btnSignUp;
    private Button btnGoToSignIn;
    EditText edtEmail, edtPassword;
    FirebaseAuth firebaseAuth;
    ProgressBar pbSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dang_ky);

        setComponents();
        setEvents();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setEvents() {
        btnSignUp.setOnClickListener(this);
        btnGoToSignIn.setOnClickListener(this);
    }

    private void setComponents() {
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoToSignIn = findViewById(R.id.btnGoToSignIn);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        pbSignUp = findViewById(R.id.pbLoading);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(this.getBaseContext(), ActivityDashboard.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSignUp:
                signUp();
                break;
            case R.id.btnGoToSignIn:
                goToSignIn();
                break;
        }
    }

    private void goToSignIn() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void signUp() {
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

        pbSignUp.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        pbSignUp.setVisibility(View.INVISIBLE);
        if (task.isSuccessful())
        {
            goToSignIn();
        }
        else
        {
            Toast.makeText(getBaseContext(), ZUltility.getNotifyException(task.getException())
                    , Toast.LENGTH_LONG).show();
            Log.e("Signin Error", "onCancelled", task.getException());
        }
    }
}