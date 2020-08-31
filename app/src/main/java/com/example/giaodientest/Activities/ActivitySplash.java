package com.example.giaodientest.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giaodientest.R;

public class ActivitySplash extends AppCompatActivity {
    private static int TIME_OUT = 5000;

    ImageView imglogo;
    TextView tvTitle, tvSubtitle;

    Animation aniTop, aniBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        //get components
        imglogo = findViewById(R.id.imgLogo);
        tvTitle = findViewById(R.id.tvAppTitle);
        tvSubtitle = findViewById(R.id.tvAppSubtitle);
        getSetAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplash.this, ActivitySignIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                finish();
            }
        },
                TIME_OUT);
    }

    @Override
    public void finish()
    {
        super.finish();
    }

    private void getSetAnimation() {
        //get animation
        aniTop = AnimationUtils.loadAnimation(this, R.anim.splash_top);
        aniBottom = AnimationUtils.loadAnimation(this, R.anim.splash_bottom);

        //set animation
        imglogo.setAnimation(aniTop);
        tvTitle.setAnimation(aniTop);
        tvSubtitle.setAnimation(aniBottom);
    }
}