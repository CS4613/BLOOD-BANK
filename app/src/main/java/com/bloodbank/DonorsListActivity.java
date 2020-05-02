package com.bloodbank;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DonorsListActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private DonorDetailsAdapter donorDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_list);
        setTitle("Donors List");

        Intent intent = getIntent();

        String zipCode = intent.getStringExtra("zipCode");
        String bloodGroup = intent.getStringExtra("bloodGroup");

        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("zip_code").document(zipCode).collection(bloodGroup);

        Query query = collectionReference.orderBy("name");

        FirestoreRecyclerOptions<DonorDetails> options = new FirestoreRecyclerOptions.Builder<DonorDetails>().setQuery(query, DonorDetails.class).build();

        donorDetailsAdapter = new DonorDetailsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(donorDetailsAdapter);

        donorDetailsAdapter.setOnItemClickListener(new DonorDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                DonorDetails donorDetails = documentSnapshot.toObject(DonorDetails.class);
                Intent i = new Intent(DonorsListActivity.this,DonorDataActivity.class);
                i.putExtra("name",donorDetails.getName());
                i.putExtra("address",donorDetails.getAddress());
                i.putExtra("bloodGroup",donorDetails.getBloodGroup());
                i.putExtra("email",donorDetails.getEmail());
                i.putExtra("mobileNo",donorDetails.getMobileNo());
                i.putExtra("zipCode",donorDetails.getZipCode());
                i.putExtra("dob",donorDetails.getDob());
                i.putExtra("gender",donorDetails.getGender());
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        donorDetailsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        donorDetailsAdapter.stopListening();
    }
}
