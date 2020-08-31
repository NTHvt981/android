package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giaodientest.Adapter.AdapterPKTT;
import com.example.giaodientest.DTO.DTOPKTT;
import com.example.giaodientest.DTO.DTOQuanAo;
import com.example.giaodientest.DTO.DTOTinTuc;
import com.example.giaodientest.Fragment.FragmentCTQA;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.giaodientest.DTO.DTOPKTT.PK_AO;
import static com.example.giaodientest.DTO.DTOPKTT.PK_BALO;
import static com.example.giaodientest.DTO.DTOPKTT.PK_GIAY;
import static com.example.giaodientest.DTO.DTOPKTT.PK_NON;
import static com.example.giaodientest.DTO.DTOPKTT.PK_QUAN;

public class ActivityCTQA extends AppCompatActivity
        implements View.OnClickListener, AdapterPKTT.OnPkttListener{
    private static ActivityCTQA instance;
    
    private LinearLayout layoutPKTT;
    private DocumentReference refPktt;
    private Animation aniRightIn, aniRightOut;
    private ImageView imgHat;
    private ImageView imgShirt;
    private ImageView imgPants;
    private ImageView imgShoes;
    private ImageView imgBackpack;
    private ImageView imgCart;
    private ImageView imgSave;

    private DTOPKTT pkttNon = null;
    private DTOPKTT pkttAo = null;
    private DTOPKTT pkttQuan = null;
    private DTOPKTT pkttGiay = null;
    private DTOPKTT pkttBalo = null;

    private ConstraintLayout layoutScreenshot;
    private CollectionReference refQuanAo = FirebaseFirestore.getInstance().collection("QuanAo");
    private StorageReference refQuanAoImage = FirebaseStorage.getInstance().getReference("QuanAo");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private RecyclerView recyclerView;
    private AdapterPKTT adapterPKTT;
    private DTOQuanAo quanAo;
    private List<DTOPKTT> phuKienThoiTrangList;
    private Button btnCancel;
    private String loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctqa);

        phuKienThoiTrangList = new ArrayList<>(0);

        adapterPKTT = new AdapterPKTT
                (getBaseContext(), phuKienThoiTrangList, this);

        Intent intent = getIntent();
        getQuanAo(intent);

        setComponents();
        setEvents();
        getSetAnimation();
        getSetNavigation();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapterPKTT);
    }

    public static ActivityCTQA getInstance() {
        if (instance == null)
            instance = new ActivityCTQA();

        return instance;
    }

    private void getQuanAo(Intent intent) {
        if (intent == null) return;
        this.quanAo = intent.getParcelableExtra("quần áo");
    }
    @Override
    protected void onStart() {
        super.onStart();
        String log = pkttAo==null? "null": pkttAo.toString();
        Log.d("CTQA", log);

        if (quanAo != null)
        {
            if (quanAo.Ma == null)
                quanAo.Ma = db.collection("QuanAo").document().getId();
            else
            {
                getPhuKienThoiTrang(quanAo.MaAo);
                getPhuKienThoiTrang(quanAo.MaQuan);
                getPhuKienThoiTrang(quanAo.MaNon);
                getPhuKienThoiTrang(quanAo.MaGiay);
                getPhuKienThoiTrang(quanAo.MaBalo);
            }
        }
    }
    private void getPhuKienThoiTrang(String ma)
    {
        if (ma == null) return;
        if (ma.isEmpty()) return;

        refPktt = FirebaseFirestore.getInstance()
                .collection("PhuKienThoiTrang")
                .document(ma);

        refPktt.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        setPKTT(Objects.requireNonNull(doc.toObject(DTOPKTT.class)));
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHat:
                goToDSPKTT(PK_NON);
                break;
            case R.id.imgShirt:
                goToDSPKTT(PK_AO);
                break;
            case R.id.imgPants:
                goToDSPKTT(PK_QUAN);
                break;
            case R.id.imgShoes:
                goToDSPKTT(PK_GIAY);
                break;
            case R.id.imgBackpack:
                goToDSPKTT(PK_BALO);
                break;
            case R.id.imgCart:
                order();
                break;
            case R.id.imgSave:
                uploadScreenshot();
                break;
            case R.id.btnCancel:
                clearPKTT(loai);
                hidePKTTPanel();
                break;
            case R.id.layoutScreenshot:
                Toast.makeText(getBaseContext(), "Click layout", Toast.LENGTH_LONG).show();
                hidePKTTPanel();
                break;
        }
    }

    private void order() {
        ArrayList<DTOPKTT> lsPKTT = new ArrayList<DTOPKTT>(0);

        lsPKTT.add(pkttNon);
        lsPKTT.add(pkttAo);
        lsPKTT.add(pkttQuan);
        lsPKTT.add(pkttGiay);
        lsPKTT.add(pkttBalo);
        //noinspection StatementWithEmptyBody
        while (lsPKTT.remove(null));
        if (lsPKTT.isEmpty()) {
            Toast.makeText(getBaseContext(),
                    "Không có phụ kiện nào được đặt hàng", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(getBaseContext(), ActivityThongTinGiaoHang.class);
        intent.putParcelableArrayListExtra("danh sách phụ kiện",
                lsPKTT);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    private void goToDSPKTT(String loai) {
        showPKTTPanel();
        this.loai = loai;

//        if (registerPKTT != null)
//            registerPKTT.remove();

        db.collection("PhuKienThoiTrang")
                .whereEqualTo("Loai", this.loai)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                phuKienThoiTrangList.clear();
                if (!task.isSuccessful())
                {
                    Toast.makeText(getBaseContext(), "Error in getting Pktt", Toast.LENGTH_LONG).show();
                }
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    phuKienThoiTrangList.add(doc.toObject(DTOPKTT.class));
                }
                adapterPKTT.notifyDataSetChanged();
            }
        });
    }

    private void clearPKTT(String loai)
    {
        switch (loai) {
            case PK_NON:
                pkttNon = null;
                imgHat.setImageResource(R.drawable.icon_clothes_hat);
                break;
            case PK_AO:
                pkttAo = null;
                imgShirt.setImageResource(R.drawable.icon_clothes_shirt);
                break;
            case PK_QUAN:
                pkttQuan = null;
                imgPants.setImageResource(R.drawable.icon_clothes_pants);
                break;
            case PK_GIAY:
                pkttGiay = null;
                imgShoes.setImageResource(R.drawable.icon_clothes_shoes);
                break;
            case PK_BALO:
                pkttBalo = null;
                imgBackpack.setImageResource(R.drawable.icon_clothes_backpack);
                break;
            default:
                break;
        }
    }
    private void setPKTT(@NonNull DTOPKTT phuKien)
    {
        switch (phuKien.Loai) {
            case PK_NON:
                pkttNon = phuKien;
                Glide.with(getBaseContext())
                        .load(phuKien.Hinh)
                        .into(imgHat);
                break;
            case PK_AO:
                pkttAo = phuKien;
                Glide.with(getBaseContext())
                        .load(phuKien.Hinh)
                        .into(imgShirt);
                break;
            case PK_QUAN:
                pkttQuan = phuKien;
                Glide.with(getBaseContext())
                        .load(phuKien.Hinh)
                        .into(imgPants);
                break;
            case PK_GIAY:
                pkttGiay = phuKien;
                Glide.with(getBaseContext())
                        .load(phuKien.Hinh)
                        .into(imgShoes);
                break;
            case PK_BALO:
                pkttBalo = phuKien;
                Glide.with(getBaseContext())
                        .load(phuKien.Hinh)
                        .into(imgBackpack);
                break;
            default:
                break;
        }
    }

    //    get image capture layout upload to storage then save the data
    private void uploadScreenshot() {
        layoutScreenshot.setDrawingCacheEnabled(true);
        layoutScreenshot.buildDrawingCache();
        Bitmap screenShot = Bitmap.createBitmap(layoutScreenshot.getDrawingCache());
        layoutScreenshot.setDrawingCacheEnabled(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        screenShot.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask task = refQuanAoImage.putBytes(data);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                refQuanAoImage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        quanAo.HinhAnh = task.getResult().toString();
                        save();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Failure in Uploading", Toast.LENGTH_LONG).show();
            }
        });
    }

    //    save quanao screenshot into firebase
    private void save() {
        quanAo.MaAo     = pkttAo == null?   null: pkttAo.Ma;
        quanAo.MaQuan   = pkttQuan == null? null: pkttQuan.Ma;
        quanAo.MaNon    = pkttNon == null?  null: pkttNon.Ma;
        quanAo.MaGiay   = pkttGiay == null? null: pkttGiay.Ma;
        quanAo.MaBalo   = pkttBalo == null? null: pkttBalo.Ma;

        quanAo.TenToanBoPKTT = pkttNon     == null? "": pkttNon.Ten + "\n";
        quanAo.TenToanBoPKTT += pkttAo      == null? "": pkttAo.Ten + "\n";
        quanAo.TenToanBoPKTT += pkttQuan    == null? "": pkttQuan.Ten + "\n";
        quanAo.TenToanBoPKTT += pkttGiay    == null? "": pkttGiay.Ten + "\n";
        quanAo.TenToanBoPKTT += pkttBalo    == null? "": pkttBalo.Ten;

        quanAo.MaKhachHang = auth.getUid();

        refQuanAo.document(quanAo.Ma).set(quanAo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getBaseContext(), "Save Successful", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getBaseContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setComponents() {
        imgHat = findViewById(R.id.imgHat);
        imgShirt = findViewById(R.id.imgShirt);
        imgPants = findViewById(R.id.imgPants);
        imgShoes = findViewById(R.id.imgShoes);
        imgBackpack = findViewById(R.id.imgBackpack);
        imgCart = findViewById(R.id.imgCart);
        imgSave = findViewById(R.id.imgSave);

        layoutScreenshot = findViewById(R.id.layoutScreenshot);
        layoutPKTT = findViewById(R.id.layoutPKTT);
        recyclerView = findViewById(R.id.rvPKTT);
        btnCancel = findViewById(R.id.btnCancel);
    }
    private void setEvents() {
        imgHat.setOnClickListener(this);
        imgShirt.setOnClickListener(this);
        imgPants.setOnClickListener(this);
        imgShoes.setOnClickListener(this);
        imgBackpack.setOnClickListener(this);
        imgCart.setOnClickListener(this);
        imgSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        layoutScreenshot.setOnClickListener(this);
    }
    private void getSetAnimation() {
        aniRightIn = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_right);
        aniRightOut = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_right);
    }

    //    Phu kien thoi trang click event
    @Override
    public void onHinhClick(int position) {
        setPKTT(phuKienThoiTrangList.get(position));
        hidePKTTPanel();
    }

    @Override
    public void onTenClick(int position) {

    }
    @Override
    public void onHinhLongClick(int position) {
    }

    private void hidePKTTPanel() {
        layoutPKTT.setAnimation(aniRightOut);
        layoutPKTT.animate().setDuration(2000).withEndAction(new Runnable() {
            @Override
            public void run() {
        layoutPKTT.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void showPKTTPanel() {
        layoutPKTT.setVisibility(View.VISIBLE);
        layoutPKTT.setAnimation(aniRightIn);
        layoutPKTT.animate().setDuration(2000);
    }

    private void getSetNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navBar);
        navigation.setSelectedItemId(R.id.itemClothes);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemCart:
                        break;
                    case R.id.itemClothes:
                        break;
                    case R.id.itemCustomize:
                        break;
                    case R.id.itemNews:
                        Intent intent = new Intent(getBaseContext(), ActivityTinTuc.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }
}