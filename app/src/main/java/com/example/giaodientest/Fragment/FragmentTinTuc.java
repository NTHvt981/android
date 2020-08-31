package com.example.giaodientest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodientest.Activities.ActivityTinTuc;
import com.example.giaodientest.Activities.ZUltility;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.giaodientest.Adapter.AdapterTinTuc;
import com.example.giaodientest.DTO.DTOTinTuc;


public class FragmentTinTuc extends Fragment
        implements AdapterTinTuc.OnTinTucListener, OnCompleteListener<QuerySnapshot> {
    RecyclerView recyclerView;
    AdapterTinTuc adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<DTOTinTuc> tinTucList;

//    init non graphic variables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tinTucList = new ArrayList<>(0);
    }

    //    init graphic variables
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tin_tuc, container, false);

        recyclerView = root.findViewById(R.id.rvNews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdapterTinTuc(getContext(), tinTucList, this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

//        we get data from firebase only once
        db.collection("TinTuc")
                .get()
                .addOnCompleteListener(this);
    }

//    when we click image of tintuc item
    @Override
    public void OnImageClick(int position) {
        DTOTinTuc tinTuc = tinTucList.get(position);

        Intent intent = new Intent(getActivity(), ActivityTinTuc.class);
        intent.putExtra("đường dẫn", tinTuc.DuongDan);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

//    this function is called when getting data tintuc from firebase is done
//    we add data from task (or snapshot) into tinTucList
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        tinTucList.clear();
        if (task.isSuccessful())
        {
            for (QueryDocumentSnapshot document :
                    Objects.requireNonNull(task.getResult())) {
                tinTucList.add( document.toObject(DTOTinTuc.class));
            }
            adapter.notifyDataSetChanged();
        }
        else
        {
            FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
            //do that
            Toast.makeText(getContext(),
                    ZUltility.getNotifyFirebaseExeption(exception),
                    Toast.LENGTH_LONG).show();
        }
    }
}