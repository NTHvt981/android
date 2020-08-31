package com.example.giaodientest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodientest.Activities.ActivityCTDH;
import com.example.giaodientest.Adapter.AdapterCTDH;
import com.example.giaodientest.Adapter.AdapterDonHang;
import com.example.giaodientest.DTO.DTODonHang;
import com.example.giaodientest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FragmentDSDH extends Fragment implements AdapterDonHang.OnDonHangListener {
    RecyclerView recyclerView;
    AdapterDonHang adapter;

    CollectionReference refDonHang = FirebaseFirestore.getInstance()
            .collection("DonHang");

    List<DTODonHang> donHangList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dsdh, container, false);

        donHangList = new ArrayList<>(0);
        recyclerView = root.findViewById(R.id.rvCarts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AdapterDonHang(getActivity(), donHangList, this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        //code này cần cải tiến
        String uid = FirebaseAuth.getInstance().getUid();

        try {
            refDonHang
                .whereEqualTo("MaKhachHang", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e)
                    {
                    donHangList.clear();

                    for (QueryDocumentSnapshot document : value) {
                        DTODonHang donHang = document.toObject(DTODonHang.class);

                        if (!donHang.TinhTrang.equals(DTODonHang.DA_HUY))
                            donHangList.add(donHang);
                    }

                    adapter.notifyDataSetChanged();
                    }
                });
        }
        catch (Exception ignored) {}
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getActivity(), ActivityCTDH.class);
        intent.putExtra("đơn hàng", donHangList.get(position));

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
}