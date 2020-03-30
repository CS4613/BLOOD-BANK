package com.needblood.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.needblood.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SearchDonorFragment extends Fragment {
    public SearchDonorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_search_donor, container, false);

        Spinner spinner =  root.findViewById(R.id.spinner_choose_bloodgroup);
        ArrayAdapter<String> bloodGroupArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.blood_groups));
        bloodGroupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(bloodGroupArrayAdapter);
        spinner.setSelection(7);

        return root;
    }
}
