package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.giaodientest.DTO.DTOQuanAo;
import com.example.giaodientest.Fragment.FragmentDSDH;
import com.example.giaodientest.Fragment.FragmentCTQA;
import com.example.giaodientest.Fragment.FragmentCaNhan;
import com.example.giaodientest.Fragment.FragmentTinTuc;
import com.example.giaodientest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityDashboard extends AppCompatActivity {
    FirebaseAuth auth;
    BottomNavigationView navBar;
    Fragment fragmentCart;
    Fragment fragmentClothes;
    Fragment fragmentCustomize;
    Fragment fragmentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_trang_chu);

        navBar = findViewById(R.id.navBar);
        navBar.setOnNavigationItemSelectedListener(navListener);

        setFragments();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClothes).commit();
        navBar.setSelectedItemId(R.id.itemClothes);
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                {
                    Intent intent = new Intent(getBaseContext(), ActivitySignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setFragments() {
        fragmentCart = new FragmentDSDH();
        fragmentClothes = FragmentCTQA.getInstance(getBaseContext(), null);
        fragmentCustomize = new FragmentCaNhan(getBaseContext());
        fragmentNews = new FragmentTinTuc();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragmentSelected = null;

                    switch (item.getItemId())
                    {
                        case R.id.itemCart:
                            fragmentSelected = fragmentCart;
                            break;
                        case R.id.itemClothes:
                            fragmentSelected = fragmentClothes;
                            break;
                        case R.id.itemCustomize:
                            fragmentSelected = fragmentCustomize;
                            break;
                        case R.id.itemNews:
                            fragmentSelected = fragmentNews;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, fragmentSelected).commit();

                    return true;
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case ZUltility.QUAN_AO_REQUEST:
                if (resultCode == RESULT_OK)
                {
                    DTOQuanAo quanAo = data.getParcelableExtra(DTOQuanAo.INTENT_TAG);
                    openCTQAWithQuanAo(quanAo);
                }
                break;
            case ZUltility.DON_HANG_REQUEST:
                if (resultCode == RESULT_OK)
                    openDSDonHang();
                break;
            default:
                break;
        }
    }

    private void openCTQAWithQuanAo(DTOQuanAo quanAo) {
        fragmentClothes = FragmentCTQA.getInstance(getBaseContext(), quanAo);
        navBar.setSelectedItemId(R.id.itemClothes);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClothes).commit();
    }

    private void openDSDonHang() {
        navBar.setSelectedItemId(R.id.itemCart);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragmentCart).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}