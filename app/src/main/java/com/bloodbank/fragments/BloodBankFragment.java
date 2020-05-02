package com.bloodbank.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.bloodbank.R;

public class BloodBankFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    TextView aPlus, aMinus, bPlus, bMinus, aBplus, aBMinus, oPlus, oMinus;

    public BloodBankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blood_bank, container, false);

        aPlus = root.findViewById(R.id.a_plus_count);
        aMinus = root.findViewById(R.id.a_minus_count);
        bPlus = root.findViewById(R.id.b_plus_count);
        bMinus = root.findViewById(R.id.b_minus_count);
        aBplus = root.findViewById(R.id.ab_plus_count);
        aBMinus = root.findViewById(R.id.ab_minus_count);
        oPlus = root.findViewById(R.id.o_plus_count);
        oMinus = root.findViewById(R.id.o_minus_count);


        final DocumentReference documentReference = firebaseFirestore.collection("blood_groups").document("groups");
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    aPlus.setText(documentSnapshot.get("A+").toString());
                    aMinus.setText(documentSnapshot.get("A-").toString());
                    bPlus.setText(documentSnapshot.get("B+").toString());
                    bMinus.setText(documentSnapshot.get("B-").toString());
                    aBplus.setText(documentSnapshot.get("AB+").toString());
                    aBMinus.setText(documentSnapshot.get("AB-").toString());
                    oPlus.setText(documentSnapshot.get("O+").toString());
                    oMinus.setText(documentSnapshot.get("O-").toString());
                }
            }
        });

        // Inflate the layout for this fragment
        return root;
    }
}
