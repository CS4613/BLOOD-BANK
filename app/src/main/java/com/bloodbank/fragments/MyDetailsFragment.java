package com.bloodbank.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.bloodbank.R;

import java.util.HashMap;
import java.util.Map;

public class MyDetailsFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userId;

    private TextView name, email, dob, bg, gender;
    private CheckBox donor;
    private EditText address, zipCode, mobileNo;
    private Button update;

    private String tAddress, tZipCode, tMobileNo;
    private boolean tIsDonor;
    public MyDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_my_details, container, false);
        name = root.findViewById(R.id.mydetails_name);
        email = root.findViewById(R.id.mydetails_email);
        bg = root.findViewById(R.id.mydetails_blood_group);
        gender = root.findViewById(R.id.mydetails_gender);
        dob = root.findViewById(R.id.mydetails_dob);

        donor = root.findViewById(R.id.mydetails_donor);

        address = root.findViewById(R.id.mydetails_address);
        zipCode = root.findViewById(R.id.mydetails_zip_code);
        mobileNo = root.findViewById(R.id.mydetails_mobile_no);

        update = root.findViewById(R.id.update_button);

        final DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    name.setText(documentSnapshot.getString("name"));
                    email.setText(documentSnapshot.getString("email"));
                    bg.setText(documentSnapshot.getString("bloodGroup"));
                    gender.setText(documentSnapshot.getString("gender"));
                    dob.setText(documentSnapshot.getString("dob"));

                    String fAddress, fZipCode, fMobileNo;
                    fAddress = documentSnapshot.getString("address");
                    fZipCode = documentSnapshot.getString("zipCode");
                    fMobileNo = documentSnapshot.getString("mobileNo");
                    boolean fIsDonor = documentSnapshot.getBoolean("isDonor");

                    address.setText(fAddress);
                    zipCode.setText(fZipCode);
                    mobileNo.setText(fMobileNo);
                    donor.setChecked(fIsDonor);


                    tAddress = fAddress;
                    tZipCode = fZipCode;
                    tMobileNo = fMobileNo;
                    tIsDonor = fIsDonor;

                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addressString = address.getText().toString().trim();
                String zipCodeString = zipCode.getText().toString().trim();
                String mobileNoString = mobileNo.getText().toString().trim();
                boolean isDonor = donor.isChecked();

                if (addressString.equals(tAddress) && zipCodeString.equals(tZipCode) && mobileNoString.equals(tMobileNo) && isDonor == tIsDonor) {
                    Toast.makeText(getActivity(), "Details updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("address", addressString);
                    updateMap.put("zipCode", zipCodeString);
                    updateMap.put("mobileNo", mobileNoString);
                    updateMap.put("isDonor", isDonor);
                    documentReference.update(updateMap);
                    Toast.makeText(getActivity(), "Details updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
