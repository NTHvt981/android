package com.example.giaodientest.Fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giaodientest.Activities.ActivityDSQA;
import com.example.giaodientest.Activities.ActivityThongTinGiaoHang;
import com.example.giaodientest.Activities.ActivityThongTinPKTT;
import com.example.giaodientest.Activities.ActivityTinTuc;
import com.example.giaodientest.Activities.ZUltility;
import com.example.giaodientest.Adapter.AdapterPKTT;
import com.example.giaodientest.DTO.DTOPKTT;
import com.example.giaodientest.DTO.DTOQuanAo;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.giaodientest.DTO.DTOPKTT.PK_NON;
import static com.example.giaodientest.DTO.DTOPKTT.PK_QUAN;
import static com.example.giaodientest.DTO.DTOPKTT.PK_BALO;
import static com.example.giaodientest.DTO.DTOPKTT.PK_AO;
import static com.example.giaodientest.DTO.DTOPKTT.PK_GIAY;

public class FragmentCTQA extends Fragment
        implements View.OnClickListener, AdapterPKTT.OnPkttListener {
    Context context;
    LinearLayout layoutPKTT;
    DocumentReference refPktt;
    Animation aniRightIn, aniRightOut;

    ImageView imgHat;
    ImageView imgShirt;
    ImageView imgPants;
    ImageView imgShoes;
    ImageView imgBackpack;

    ImageView imgCart;
    ImageView imgSave;
    ImageView imgOpen;
    ImageView imgNew;

    DTOPKTT pkttNon = null;
    DTOPKTT pkttAo = null;
    DTOPKTT pkttQuan = null;
    DTOPKTT pkttGiay = null;
    DTOPKTT pkttBalo = null;

    ConstraintLayout layoutScreenshot;
    DTOQuanAo quanAo;
    CollectionReference refQuanAo = FirebaseFirestore.getInstance().collection("QuanAo");
    StorageReference refQuanAoImage = FirebaseStorage.getInstance().getReference("QuanAo");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    RecyclerView recyclerView;
    AdapterPKTT adapterPKTT;
    List<DTOPKTT> phuKienThoiTrangList;
    Button btnCancel;
    String loai;

//    if you want to save data
    private static FragmentCTQA instance = null;
    private View root;

    public FragmentCTQA(Context context, DTOQuanAo quanAo) {
        this.context = context;
        this.quanAo = quanAo;
    }

    public static FragmentCTQA getInstance(@Nullable Context context,@Nullable DTOQuanAo quanAo) {
        if (quanAo != null)
            instance = new FragmentCTQA(context, quanAo);

        if (instance == null)
            instance = new FragmentCTQA(context, new DTOQuanAo());

        return instance;
    }

//    logic initial
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phuKienThoiTrangList = new ArrayList<>(0);

        adapterPKTT = new AdapterPKTT
                (this.context, phuKienThoiTrangList, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_ctqa, container, false);

        setComponents(this.root);
        setEvents();
        getSetAnimation();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        recyclerView.setAdapter(adapterPKTT);

        if (quanAo != null)
        {
            if (quanAo.Ma == null)
                quanAo.Ma = refQuanAo.document().getId();
            else
            {
                getPhuKienThoiTrang(quanAo.MaAo);
                getPhuKienThoiTrang(quanAo.MaQuan);
                getPhuKienThoiTrang(quanAo.MaNon);
                getPhuKienThoiTrang(quanAo.MaGiay);
                getPhuKienThoiTrang(quanAo.MaBalo);
            }
        }

        return root;
    }

    private void getSetAnimation() {
        aniRightIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        aniRightOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
    }


    @Override
    public void onStart() {
        super.onStart();
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
            case R.id.imgOpen:
                open();
                break;
            case R.id.imgNew:
                newFile();
                break;

            case R.id.btnCancel:
                clearPKTT(loai);
                hidePKTTPanel();
                break;
            case R.id.layoutScreenshot:
                hidePKTTPanel();
                break;
        }
    }

    private void newFile() {
        quanAo = new DTOQuanAo();
        quanAo.Ma = refQuanAo.document().getId();
        clearPKTT(PK_NON);
        clearPKTT(PK_AO);
        clearPKTT(PK_QUAN);
        clearPKTT(PK_GIAY);
        clearPKTT(PK_BALO);
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
            Toast.makeText(context,
                    "Không có phụ kiện nào được đặt hàng", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(context, ActivityThongTinGiaoHang.class);
        intent.putParcelableArrayListExtra("danh sách phụ kiện",
                lsPKTT);

        getActivity().startActivityForResult(intent, 300);
        getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    private void goToDSPKTT(String loai) {
        showPKTTPanel();
        this.loai = loai;

        db.collection("PhuKienThoiTrang")
                        .whereEqualTo("Loai", this.loai)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        phuKienThoiTrangList.clear();
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(context,
                                    "Có lỗi khi truy vấn dữ liệu",
                                    Toast.LENGTH_LONG).show();
                        }
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            phuKienThoiTrangList.add(doc.toObject(DTOPKTT.class));
                        }
                        adapterPKTT.notifyDataSetChanged();
                    }
                });
    }

//    clear and set pktt (DTO and imageView)
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
                Glide.with(getActivity()).load(phuKien.Hinh).into(imgHat);
                break;
            case PK_AO:
                pkttAo = phuKien;
                Glide.with(context).load(phuKien.Hinh).into(imgShirt);
                break;
            case PK_QUAN:
                pkttQuan = phuKien;
                Glide.with(context).load(phuKien.Hinh).into(imgPants);
                break;
            case PK_GIAY:
                pkttGiay = phuKien;
                Glide.with(context).load(phuKien.Hinh).into(imgShoes);
                break;
            case PK_BALO:
                pkttBalo = phuKien;
                Glide.with(context).load(phuKien.Hinh).into(imgBackpack);
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
                Toast.makeText(context, "Tải ảnh thành công", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Có lỗi khi tải ảnh", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, "Lưu thành công", Toast.LENGTH_LONG).show();
                        else
                        {
                            FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                            Toast.makeText(context,
                                    ZUltility.getNotifyFirebaseExeption(exception),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void open() {
        Intent intent = new Intent(context, ActivityDSQA.class);
        getActivity().startActivityForResult(intent, 200);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setComponents(View root) {
        imgHat = root.findViewById(R.id.imgHat);
        imgShirt = root.findViewById(R.id.imgShirt);
        imgPants = root.findViewById(R.id.imgPants);
        imgShoes = root.findViewById(R.id.imgShoes);
        imgBackpack = root.findViewById(R.id.imgBackpack);

        imgCart = root.findViewById(R.id.imgCart);
        imgSave = root.findViewById(R.id.imgSave);
        imgOpen = root.findViewById(R.id.imgOpen);
        imgNew = root.findViewById(R.id.imgNew);

        layoutScreenshot = root.findViewById(R.id.layoutScreenshot);
        layoutPKTT = root.findViewById(R.id.layoutPKTT);
        recyclerView = root.findViewById(R.id.rvPKTT);
        btnCancel = root.findViewById(R.id.btnCancel);
    }
    private void setEvents() {
        imgHat.setOnClickListener(this);
        imgShirt.setOnClickListener(this);
        imgPants.setOnClickListener(this);
        imgShoes.setOnClickListener(this);
        imgBackpack.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        layoutScreenshot.setOnClickListener(this);

        imgCart.setOnClickListener(this);
        imgSave.setOnClickListener(this);
        imgOpen.setOnClickListener(this);
        imgNew.setOnClickListener(this);
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
        DTOPKTT pktt = phuKienThoiTrangList.get(position);

        Intent intent = new Intent(getActivity(), ActivityThongTinPKTT.class);
        intent.putExtra(DTOPKTT.INTENT_TAG, pktt);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

//    pktt panel animation hide and show
    private void hidePKTTPanel() {
        layoutPKTT.setVisibility(View.GONE);
    }
    private void showPKTTPanel() {
//        aniRightIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);

        layoutPKTT.setVisibility(View.VISIBLE);
//        layoutPKTT.setAnimation(aniRightIn);
//        layoutPKTT.animate().setDuration(1000);
    }

    @Override
    public void onStop() {
        super.onStop();

//        instance = this;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (pkttNon != null) Glide.with(context).load(pkttNon.Hinh).into(imgHat);
        if (pkttAo != null) Glide.with(context).load(pkttAo.Hinh).into(imgShirt);
        if (pkttQuan != null) Glide.with(context).load(pkttQuan.Hinh).into(imgPants);
        if (pkttGiay != null) Glide.with(context).load(pkttGiay.Hinh).into(imgShoes);
        if (pkttBalo != null) Glide.with(context).load(pkttBalo.Hinh).into(imgBackpack);
    }
}