package com.example.giaodientest.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.giaodientest.Adapter.AdapterPKTT;
import com.example.giaodientest.DTO.DTOPKTT;
import com.example.giaodientest.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentDSPKTT extends Fragment
        implements AdapterPKTT.OnPkttListener, EventListener<QuerySnapshot>, View.OnClickListener {
    Context context;
    ListenerRegistration registerPKTT = null;
    OnFragmentDSPKTTListener listener;

    Button btnCancel;
    RecyclerView recyclerView;
    AdapterPKTT adapterPKTT;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String loai = "";

    List<DTOPKTT> phuKienThoiTrangList;

    public FragmentDSPKTT(Context context, OnFragmentDSPKTTListener listener)
    {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phuKienThoiTrangList = new ArrayList<>(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ds_pktt, container, false);

        btnCancel = root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        recyclerView = root.findViewById(R.id.rvPKTT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapterPKTT = new AdapterPKTT
                (context, phuKienThoiTrangList, this);
        recyclerView.setAdapter(adapterPKTT);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onEvent(@Nullable QuerySnapshot task, @Nullable FirebaseFirestoreException e) {
        phuKienThoiTrangList.clear();
        if (task == null) return;
        for (DocumentSnapshot doc : task.getDocuments()) {
            phuKienThoiTrangList.add(doc.toObject(DTOPKTT.class));
        }
        adapterPKTT.notifyDataSetChanged();
    }

    public void resetLoai(String loai) {
        if (registerPKTT != null)
            registerPKTT.remove();

        registerPKTT = db.collection("PhuKienThoiTrang")
                .whereEqualTo("Loai", this.loai)
                .addSnapshotListener(this);
    }

    @Override
    public void onHinhClick(int position) {
        listener.choosePKTT(phuKienThoiTrangList.get(position), this.loai);
    }

    @Override
    public void onTenClick(int position) {
        listener.choosePKTT(phuKienThoiTrangList.get(position), this.loai);
    }

    @Override
    public void onHinhLongClick(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnCancel:
                cancelPKTT();
                break;
        }
    }

    private void cancelPKTT() {
        listener.choosePKTT(null, this.loai);
    }

    public interface OnFragmentDSPKTTListener {
        public void choosePKTT(DTOPKTT pktt, String loai);
    }
}