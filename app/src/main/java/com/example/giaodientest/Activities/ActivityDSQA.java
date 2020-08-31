package com.example.giaodientest.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.giaodientest.Adapter.AdapterQuanAo;
import com.example.giaodientest.DTO.DTOQuanAo;
import com.example.giaodientest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityDSQA extends AppCompatActivity implements AdapterQuanAo.OnQuanAoListener {
    RecyclerView recyclerView;
    AdapterQuanAo adapter;

    CollectionReference refQuanAo = FirebaseFirestore.getInstance()
            .collection("QuanAo");

    List<DTOQuanAo> quanAoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsqa);

        quanAoList = new ArrayList<>(0);
        recyclerView = findViewById(R.id.rvClothes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        adapter = new AdapterQuanAo(getBaseContext(), quanAoList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //code này cần cải tiến
        String uid = FirebaseAuth.getInstance().getUid();

        refQuanAo.whereEqualTo("MaKhachHang", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> value) {
                        quanAoList.clear();

                        for (QueryDocumentSnapshot document : value.getResult()) {
                            DTOQuanAo quanAo = document.toObject(DTOQuanAo.class);

                            quanAoList.add(quanAo);
                        }

//                        dùng cái này nhanh
                        adapter.notifyDataSetChanged();
//                        dùng cái này đẹp hơn
//                        adapter.notifyItemRangeInserted(0, quanAoList.size());
                    }
                });
    }

    @Override
    public void OnItemClick(int position) {
        Log.d("DSQA", "click item");

        Intent intent = new Intent();
        intent.putExtra(DTOQuanAo.INTENT_TAG, quanAoList.get(position));
        setResult(RESULT_OK, intent);
        finishActivity(200);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void OnDeleteClick(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("Có",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteQuanAo(position);
                    }
                })
                .setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .setTitle("Xác nhận")
                .setMessage("Bạn có đồng ý xóa quần áo?");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteQuanAo(final int position)
    {
        String ma = quanAoList.get(position).Ma;
        refQuanAo.document(ma).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getBaseContext(), "Delete Successful!", Toast.LENGTH_LONG).show();
                            quanAoList.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                        else
                        {
                            FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                            //do that
                            Toast.makeText(getBaseContext(),
                                    ZUltility.getNotifyFirebaseExeption(exception),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}