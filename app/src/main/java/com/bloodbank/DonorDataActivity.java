package com.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DonorDataActivity extends AppCompatActivity {

    TextView name, email, dob, bloodGroup, gender, mobileNo, address, zipCode;
    Button sendRequest;
    String toEmail, toUserName, toBloodGroup, fromUserName;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_data);
        auth = FirebaseAuth.getInstance();
        fromUserName = auth.getCurrentUser().getDisplayName();
        setTitle("Donor Details");

        Intent i = getIntent();

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        dob = findViewById(R.id.user_dob);
        bloodGroup = findViewById(R.id.user_bloodgroup);
        gender = findViewById(R.id.user_gender);
        mobileNo = findViewById(R.id.user_mobileno);
        address = findViewById(R.id.user_address);
        zipCode = findViewById(R.id.user_zipcode);

        sendRequest = findViewById(R.id.btn_send_request);

        toEmail = i.getStringExtra("email");
        toUserName = i.getStringExtra("name");
        toBloodGroup = i.getStringExtra("bloodGroup");

        name.setText(i.getStringExtra("name"));
        address.setText(i.getStringExtra("address"));
        bloodGroup.setText(i.getStringExtra("bloodGroup"));
        email.setText(i.getStringExtra("email"));
        mobileNo.setText(i.getStringExtra("mobileNo"));
        zipCode.setText(i.getStringExtra("zipCode"));
        dob.setText(i.getStringExtra("dob"));
        gender.setText(i.getStringExtra("gender"));

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",toEmail, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Need blood urgently");
                emailIntent.putExtra(Intent.EXTRA_TEXT,  "Hi " + toUserName + ",\n\nI need blood " + toBloodGroup + " urgently. Can you please donate!!.\n\nThanks,\n");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }
}
