package com.bloodbank.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bloodbank.DonorDetailsAdapter;
import com.bloodbank.DonorsListActivity;
import com.bloodbank.R;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SearchDonorFragment extends Fragment {

    Button searh;
    AutoCompleteTextView zipCode;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    private DonorDetailsAdapter donorDetailsAdapter;

    public SearchDonorFragment() {
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

        final View root = inflater.inflate(R.layout.fragment_search_donor, container, false);

        searh = root.findViewById(R.id.btn_serach);
        zipCode = root.findViewById(R.id.edit_zip_code);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Arrays.asList("test"));
        zipCode.setAdapter(arrayAdapter);

        final Spinner spinnerChooseBloodGroup = root.findViewById(R.id.spinner_choose_bloodgroup);
        ArrayAdapter<String> bloodGroupArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.blood_groups));
        bloodGroupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseBloodGroup.setAdapter(bloodGroupArrayAdapter);
        spinnerChooseBloodGroup.setSelection(0);

        searh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String zipCodeString = zipCode.getText().toString().trim();
                final String bloodGroup = spinnerChooseBloodGroup.getSelectedItem().toString();

                if (TextUtils.isEmpty(zipCodeString)) {
                    Toast.makeText(getActivity(), "Enter zipcode", Toast.LENGTH_SHORT).show();
                    zipCode.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(bloodGroup) || bloodGroup.equals("Choose blood group")) {
                    Toast.makeText(getActivity(), "Choose blood group", Toast.LENGTH_SHORT).show();
                    spinnerChooseBloodGroup.requestFocus();
                    return;
                }

                Intent i = new Intent(getActivity(), DonorsListActivity.class);
                i.putExtra("bloodGroup", bloodGroup);
                i.putExtra("zipCode", zipCodeString);
                startActivity(i);

            }
        });

        return root;
    }
}
